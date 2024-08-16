package com.example.bookshelf_api.infrastructure.jooq.repository

import com.example.bookshelf_api.infrastructure.jooq.generated.tables.Book.BOOK
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.records.BookRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class BookRepository(private val dsl: DSLContext) {

    fun findById(id: Int): BookRecord? {
        return dsl.selectFrom(BOOK)
            .where(BOOK.ID.eq(id))
            .fetchOne()
    }

    fun findByAuthorId(authorId: Int): List<BookRecord> {
        return dsl.selectFrom(BOOK)
            .where(BOOK.AUTHOR_ID.eq(authorId))
            .fetch()
    }

    fun save(book: BookRecord): Int {
        return dsl.insertInto(BOOK)
            .set(book)
            .execute()
    }

    fun update(book: BookRecord): Int {
        return dsl.update(BOOK)
            .set(book)
            .where(BOOK.ID.eq(book.id))
            .execute()
    }
}
