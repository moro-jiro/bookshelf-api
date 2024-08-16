package com.example.bookshelf_api.application.service

import com.example.bookshelf_api.infrastructure.jooq.generated.tables.daos.AuthorDao
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Author
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthorService @Autowired constructor(
    private val authorDao: AuthorDao
) {

    @Transactional
    fun createAuthor(author: Author) {
        authorDao.insert(author)
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