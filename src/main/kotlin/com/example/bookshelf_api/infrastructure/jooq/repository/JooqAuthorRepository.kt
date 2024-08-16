package com.example.bookshelf_api.infrastructure.jooq.repository

import com.example.bookshelf_api.application.dto.AuthorSearchDto
import com.example.bookshelf_api.domain.model.Author
import com.example.bookshelf_api.domain.repository.AuthorRepository
import com.example.bookshelf_api.infrastructure.jooq.generated.Tables.AUTHOR
import org.jooq.Condition
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.Author as AuthorTable
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

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

    override fun findAuthorById(id: Int): Author? {
        return dsl.selectFrom(AUTHOR)
            .where(AUTHOR.ID.eq(id))
            .fetchOneInto(Author::class.java)
    }

    override fun updateAuthor(author: Author) {
        val authorTable = AuthorTable.AUTHOR
        dsl.update(authorTable)
            .set(authorTable.FIRST_NAME, author.firstName)
            .set(authorTable.LAST_NAME, author.lastName)
            .set(authorTable.BIRTH_DATE, author.birthDate)
            .set(authorTable.GENDER, author.gender)
            .set(authorTable.UPDATED_AT, LocalDateTime.now())
            .where(authorTable.ID.eq(author.id))
            .execute()
    }

    override fun searchAuthors(searchDto: AuthorSearchDto): List<Author> {
        val authorTable = AuthorTable.AUTHOR

        val conditions = mutableListOf<Condition>()

        val nameCondition = authorTable.LAST_NAME.likeIgnoreCase("%${searchDto.lastName}%")
            .or(authorTable.FIRST_NAME.likeIgnoreCase("%${searchDto.firstName}%"))
        conditions.add(nameCondition)

        searchDto.birthDate?.let {
            conditions.add(authorTable.BIRTH_DATE.eq(it))
        }
        searchDto.gender?.let {
            conditions.add(authorTable.GENDER.eq(it))
        }

        return dsl.selectFrom(authorTable)
            .where(conditions)
            .orderBy(authorTable.LAST_NAME, authorTable.FIRST_NAME)
            .fetchInto(Author::class.java)
    }
}