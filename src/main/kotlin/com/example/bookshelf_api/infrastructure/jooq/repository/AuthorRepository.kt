package com.example.bookshelf_api.infrastructure.jooq.repository

import com.example.bookshelf_api.infrastructure.jooq.generated.tables.Author.AUTHOR
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.records.AuthorRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class AuthorRepository(private val dsl: DSLContext) {

    fun findById(id: Int): AuthorRecord? {
        return dsl.selectFrom(AUTHOR)
            .where(AUTHOR.ID.eq(id))
            .fetchOne()
    }

    fun save(author: AuthorRecord): Int {
        return dsl.insertInto(AUTHOR)
            .set(author)
            .execute()
    }

    fun update(author: AuthorRecord): Int {
        return dsl.update(AUTHOR)
            .set(author)
            .where(AUTHOR.ID.eq(author.id))
            .execute()
    }
}
