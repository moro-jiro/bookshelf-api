package com.example.bookshelf_api.infrastructure.jooq.repository

import com.example.bookshelf_api.domain.model.Book
import com.example.bookshelf_api.domain.model.Author
import com.example.bookshelf_api.domain.repository.BookRepository
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.Book as BookTable
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.Author as AuthorTable
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class JooqBookRepository(private val dsl: DSLContext) : BookRepository {

    private fun Book.toRecord() = mapOf(
        BookTable.BOOK.TITLE to title,
        BookTable.BOOK.AUTHOR_ID to authorId,
        BookTable.BOOK.PUBLICATION_DATE to publicationDate,
        BookTable.BOOK.PUBLISHER to publisher
    )

    private fun org.jooq.Record.toBook() = Book(
        id = get(BookTable.BOOK.ID),
        title = get(BookTable.BOOK.TITLE),
        authorId = get(BookTable.BOOK.AUTHOR_ID),
        publicationDate = get(BookTable.BOOK.PUBLICATION_DATE),
        publisher = get(BookTable.BOOK.PUBLISHER),
        createdAt = get(BookTable.BOOK.CREATED_AT),
        updatedAt = get(BookTable.BOOK.UPDATED_AT)
    )

    override fun createBook(book: Book): Book {
        val now = LocalDateTime.now()
        return dsl.insertInto(BookTable.BOOK)
            .set(book.toRecord())
            .set(BookTable.BOOK.CREATED_AT, now)
            .set(BookTable.BOOK.UPDATED_AT, now)
            .returning()
            .fetchOne()
            ?.toBook()
            ?: throw RuntimeException("書籍の登録に失敗しました")
    }

    override fun findBookById(id: Int): Pair<Book, Author>? {
        return dsl.select()
            .from(BookTable.BOOK)
            .join(AuthorTable.AUTHOR).on(BookTable.BOOK.AUTHOR_ID.eq(AuthorTable.AUTHOR.ID))
            .where(BookTable.BOOK.ID.eq(id))
            .fetchOne { record ->
                Pair(record.toBook(), record.into(AuthorTable.AUTHOR).into(Author::class.java))
            }
    }

    override fun findBooksByTitle(title: String): List<Book> {
        return dsl.selectFrom(BookTable.BOOK)
            .where(BookTable.BOOK.TITLE.likeIgnoreCase("%$title%"))
            .fetch()
            .map { it.toBook() }
    }

    override fun updateBook(book: Book): Book {
        val now = LocalDateTime.now()
        return dsl.update(BookTable.BOOK)
            .set(book.toRecord())
            .set(BookTable.BOOK.UPDATED_AT, now)
            .where(BookTable.BOOK.ID.eq(book.id ?: throw IllegalArgumentException("Book ID cannot be null")))
            .returning()
            .fetchOne()
            ?.toBook()
            ?: throw RuntimeException("書籍の更新に失敗しました")
    }

    override fun findBooksByAuthorId(authorId: Int): List<Book> {
        return dsl.selectFrom(BookTable.BOOK)
            .where(BookTable.BOOK.AUTHOR_ID.eq(authorId))
            .fetch()
            .map { it.toBook() }
    }
}
