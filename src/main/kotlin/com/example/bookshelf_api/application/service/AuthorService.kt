package com.example.bookshelf_api.application.service

import com.example.bookshelf_api.infrastructure.jooq.generated.tables.daos.AuthorDao
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Author
import com.example.bookshelf_api.infrastructure.repository.CustomAuthorDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class AuthorService @Autowired constructor(
    private val authorDao: AuthorDao,
    private val customAuthorDao: CustomAuthorDao
) {

    @Transactional
    fun createAuthor(author: Author) {
        val existingAuthors = customAuthorDao.fetchByFirstNameAndLastName(author.firstName, author.lastName)
        if (existingAuthors.isNotEmpty()) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "同じ名前の著者が既に登録されています")
        }

        customAuthorDao.insert(author)
    }

    @Transactional
    fun updateAuthor(author: Author) {
        authorDao.update(author)
    }

    @Transactional(readOnly = true)
    fun findAuthorById(id: Int): Author? {
        return authorDao.fetchOneById(id)
    }

    @Transactional
    fun deleteAuthorById(id: Int) {
        authorDao.deleteById(id)
    }
}