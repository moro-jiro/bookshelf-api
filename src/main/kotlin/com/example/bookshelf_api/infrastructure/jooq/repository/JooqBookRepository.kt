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
        val result = dsl.insertInto(BookTable.BOOK)
            .set(BookTable.BOOK.TITLE, book.title)
            .set(BookTable.BOOK.AUTHOR_ID, book.authorId)
            .set(BookTable.BOOK.PUBLICATION_DATE, book.publicationDate)
            .set(BookTable.BOOK.PUBLISHER, book.publisher)
            .set(BookTable.BOOK.UPDATED_AT, now)
            .returning(BookTable.BOOK.ID, BookTable.BOOK.TITLE, BookTable.BOOK.AUTHOR_ID, BookTable.BOOK.PUBLICATION_DATE, BookTable.BOOK.PUBLISHER, BookTable.BOOK.CREATED_AT, BookTable.BOOK.UPDATED_AT)
            .fetchOne()

        if (result == null) {
            throw RuntimeException("書籍の登録に失敗しました")
        }

        return Book(
            id = result[BookTable.BOOK.ID],
            title = result[BookTable.BOOK.TITLE],
            authorId = result[BookTable.BOOK.AUTHOR_ID],
            publicationDate = result[BookTable.BOOK.PUBLICATION_DATE],
            publisher = result[BookTable.BOOK.PUBLISHER],
            createdAt = result[BookTable.BOOK.CREATED_AT],
            updatedAt = result[BookTable.BOOK.UPDATED_AT]
        )
    }

    override fun findBookById(id: Int): Pair<Book, Author>? {
        val bookTable = BookTable.BOOK
        val authorTable = AuthorTable.AUTHOR

        return dsl.select()
            .from(bookTable)
            .join(authorTable).on(bookTable.AUTHOR_ID.eq(authorTable.ID))
            .where(bookTable.ID.eq(id))
            .fetchOne { record ->
                val book = Book(
                    id = record[bookTable.ID],
                    title = record[bookTable.TITLE],
                    authorId = record[bookTable.AUTHOR_ID],
                    publicationDate = record[bookTable.PUBLICATION_DATE],
                    publisher = record[bookTable.PUBLISHER],
                    createdAt = record[bookTable.CREATED_AT],
                    updatedAt = record[bookTable.UPDATED_AT]
                )

                val author = record.into(authorTable).into(Author::class.java)
                Pair(book, author)
            }
    }

    override fun findBooksByTitle(title: String): List<Book> {
        return dsl.select(
            BookTable.BOOK.ID,
            BookTable.BOOK.TITLE,
            BookTable.BOOK.AUTHOR_ID,
            BookTable.BOOK.PUBLICATION_DATE,
            BookTable.BOOK.PUBLISHER,
            BookTable.BOOK.CREATED_AT,
            BookTable.BOOK.UPDATED_AT
        )
            .from(DSL.table("book"))
            .where(DSL.field("title").likeIgnoreCase("%$title%"))
            .fetch()
            .map { record ->
                Book(
                    id = record.get(BookTable.BOOK.ID),
                    title = record.get(BookTable.BOOK.TITLE),
                    authorId = record.get(BookTable.BOOK.AUTHOR_ID),
                    publicationDate = record.get(BookTable.BOOK.PUBLICATION_DATE),
                    publisher = record.get(BookTable.BOOK.PUBLISHER),
                    createdAt = record.get(BookTable.BOOK.CREATED_AT),
                    updatedAt = record.get(BookTable.BOOK.UPDATED_AT)
                )
            }
    }

    override fun updateBook(book: Book): Book {
        val now = LocalDateTime.now()
        val updatedBook = dsl.update(BookTable.BOOK)
            .set(BookTable.BOOK.TITLE, book.title)
            .set(BookTable.BOOK.PUBLICATION_DATE, book.publicationDate)
            .set(BookTable.BOOK.PUBLISHER, book.publisher)
            .set(BookTable.BOOK.UPDATED_AT, now)
            .where(BookTable.BOOK.ID.eq(book.id ?: throw IllegalArgumentException("Book ID cannot be null")))
            .returning()
            .fetchOne()
            ?.into(Book::class.java)
            ?: throw RuntimeException("書籍の更新に失敗しました")

        return updatedBook
    }

    override fun findBooksByAuthorId(authorId: Int): List<Book> {
        return dsl.select(
            BookTable.BOOK.ID,
            BookTable.BOOK.TITLE,
            BookTable.BOOK.AUTHOR_ID,
            BookTable.BOOK.PUBLICATION_DATE,
            BookTable.BOOK.PUBLISHER,
            BookTable.BOOK.CREATED_AT,
            BookTable.BOOK.UPDATED_AT
        )
            .from(BookTable.BOOK)
            .where(BookTable.BOOK.AUTHOR_ID.eq(authorId))
            .fetch()
            .map { record ->
                Book(
                    id = record.get(BookTable.BOOK.ID),
                    title = record.get(BookTable.BOOK.TITLE),
                    authorId = record.get(BookTable.BOOK.AUTHOR_ID),
                    publicationDate = record.get(BookTable.BOOK.PUBLICATION_DATE),
                    publisher = record.get(BookTable.BOOK.PUBLISHER),
                    createdAt = record.get(BookTable.BOOK.CREATED_AT),
                    updatedAt = record.get(BookTable.BOOK.UPDATED_AT)
                )
            }
    }
}
