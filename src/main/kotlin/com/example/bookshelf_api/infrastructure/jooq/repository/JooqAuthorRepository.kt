package com.example.bookshelf_api.infrastructure.jooq.repository

import com.example.bookshelf_api.domain.model.Author
import com.example.bookshelf_api.domain.repository.AuthorRepository
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.Author as AuthorTable
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class JooqAuthorRepository(private val dsl: DSLContext) : AuthorRepository {

    override fun createAuthor(author: Author): Int {
        val now = LocalDate.now()

        return dsl.insertInto(AuthorTable.AUTHOR)
            .set(AuthorTable.AUTHOR.FIRST_NAME, author.firstName)
            .set(AuthorTable.AUTHOR.LAST_NAME, author.lastName)
            .set(AuthorTable.AUTHOR.BIRTH_DATE, author.birthDate)
            .set(AuthorTable.AUTHOR.GENDER, author.gender)
            .set(AuthorTable.AUTHOR.CREATED_AT, author.createdAt ?: now.atStartOfDay())
            .set(AuthorTable.AUTHOR.UPDATED_AT, author.updatedAt ?: now.atStartOfDay())
            .returning(AuthorTable.AUTHOR.ID)
            .fetchOne()
            ?.getValue(AuthorTable.AUTHOR.ID)
            ?: throw RuntimeException("著者の登録に失敗しました")
    }

    override fun findAuthorByDetails(firstName: String, lastName: String, birthDate: LocalDate): Author? {
        val authorTable = AuthorTable.AUTHOR

        return dsl.selectFrom(authorTable)
            .where(authorTable.FIRST_NAME.eq(firstName))
            .and(authorTable.LAST_NAME.eq(lastName))
            .and(authorTable.BIRTH_DATE.eq(birthDate))
            .fetchOneInto(Author::class.java)
    }
}