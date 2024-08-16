package com.example.bookshelf_api.infrastructure.jooq.repository

import com.example.bookshelf_api.domain.model.Book
import com.example.bookshelf_api.domain.model.Author
import com.example.bookshelf_api.domain.repository.BookRepository
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.Book as BookTable
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.Author as AuthorTable
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class JooqBookRepository(private val dsl: DSLContext) : BookRepository {

    override fun createBook(book: Book): Book {
        val now = LocalDateTime.now()
        val insertedBook = dsl.insertInto(BookTable.BOOK)
            .set(BookTable.BOOK.TITLE, book.title)
            .set(BookTable.BOOK.AUTHOR_ID, book.authorId)
            .set(BookTable.BOOK.PUBLICATION_DATE, book.publicationDate)
            .set(BookTable.BOOK.PUBLISHER, book.publisher)
            .set(BookTable.BOOK.UPDATED_AT, now)
            .returning()
            .fetchOne()
            ?.into(Book::class.java)
            ?: throw RuntimeException("書籍の登録に失敗しました")

        return insertedBook
    }

    override fun findBookById(id: Int): Pair<Book, Author>? {
        val bookTable = BookTable.BOOK
        val authorTable = AuthorTable.AUTHOR

        return dsl.select()
            .from(bookTable)
            .join(authorTable).on(bookTable.AUTHOR_ID.eq(authorTable.ID))
            .where(bookTable.ID.eq(id))
            .fetchOne { record ->
                val book = record.into(bookTable).into(Book::class.java)
                val author = record.into(authorTable).into(Author::class.java)
                Pair(book, author)
            }
    }

    override fun findBooksByTitle(title: String): List<Book> {
        return dsl.selectFrom(DSL.table("book"))
            .where(DSL.field("title").likeIgnoreCase("%$title%"))
            .fetchInto(Book::class.java)
    }

}
