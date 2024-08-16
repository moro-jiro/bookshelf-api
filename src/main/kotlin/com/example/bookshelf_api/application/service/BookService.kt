package com.example.bookshelf_api.application.service

import com.example.bookshelf_api.infrastructure.jooq.generated.tables.daos.BookDao
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService @Autowired constructor(
    private val bookDao: BookDao
) {

    @Transactional
    fun createBook(book: Book) {
        bookDao.insert(book)
    }

    @Transactional
    fun updateBook(book: Book) {
        bookDao.update(book)
    }

    @Transactional(readOnly = true)
    fun findBookById(id: Int): Book? {
        return bookDao.fetchOneById(id)
    }

    @Transactional(readOnly = true)
    fun findBooksByAuthorId(authorId: Int): List<Book> {
        return bookDao.fetchByAuthorId(authorId)
    }

    @Transactional
    fun deleteBookById(id: Int) {
        bookDao.deleteById(id)
    }
}