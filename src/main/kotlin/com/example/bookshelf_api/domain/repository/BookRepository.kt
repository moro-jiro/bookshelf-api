package com.example.bookshelf_api.domain.repository

import com.example.bookshelf_api.domain.model.Book
import com.example.bookshelf_api.domain.model.Author

interface BookRepository {
    fun createBook(book: Book): Book
    fun findBookById(id: Int): Pair<Book, Author>?
    fun findBooksByTitle(title: String): List<Book>
    fun updateBook(book: Book): Book
    fun findBooksByAuthorId(authorId: Int): List<Book>
}
