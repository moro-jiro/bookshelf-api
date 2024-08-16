package com.example.bookshelf_api.infrastructure.repository

import com.example.bookshelf_api.infrastructure.jooq.generated.tables.Author.AUTHOR
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.daos.AuthorDao
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Author
import org.jooq.DSLContext

class CustomAuthorDao(
    private val dslContext: DSLContext
) : AuthorDao(dslContext.configuration()) {

    fun fetchByFirstNameAndLastName(firstName: String, lastName: String): List<Author> {
        return dslContext
            .selectFrom(AUTHOR)
            .where(AUTHOR.FIRST_NAME.eq(firstName))
            .and(AUTHOR.LAST_NAME.eq(lastName))
            .fetchInto(Author::class.java)
    }
}