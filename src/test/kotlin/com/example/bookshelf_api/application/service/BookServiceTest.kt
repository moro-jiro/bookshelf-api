package com.example.bookshelf_api.application.service

import com.example.bookshelf_api.infrastructure.jooq.generated.tables.daos.BookDao
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class BookServiceTest {

    @Mock
    private lateinit var bookDao: BookDao

    @InjectMocks
    private lateinit var bookService: BookService

    @Test
    fun `test createBook`() {
        val book = Book().apply {
            id = 1
            title = "Sample Book"
            authorId = 1
        }

        bookService.createBook(book)

        Mockito.verify(bookDao).insert(book)
    }

    @Test
    fun `test updateBook`() {
        val book = Book().apply {
            id = 1
            title = "Updated Book"
            authorId = 1
        }

        bookService.updateBook(book)

        Mockito.verify(bookDao).update(book)
    }

    @Test
    fun `test findBookById`() {
        val bookId = 1
        val expectedBook = Book().apply {
            id = bookId
            title = "Sample Book"
            authorId = 1
        }

        Mockito.`when`(bookDao.fetchOneById(bookId)).thenReturn(expectedBook)

        val actualBook = bookService.findBookById(bookId)

        assertEquals(expectedBook, actualBook)
    }

    @Test
    fun `test findBookById returns null`() {
        val bookId = 1

        Mockito.`when`(bookDao.fetchOneById(bookId)).thenReturn(null)

        val actualBook = bookService.findBookById(bookId)

        assertNull(actualBook)
    }

    @Test
    fun `test findBooksByAuthorId`() {
        val authorId = 1
        val expectedBooks = listOf(
            Book().apply {
                id = 1
                title = "Sample Book 1"
                this.authorId = authorId
            },
            Book().apply {
                id = 2
                title = "Sample Book 2"
                this.authorId = authorId
            }
        )

        Mockito.`when`(bookDao.fetchByAuthorId(authorId)).thenReturn(expectedBooks)

        val actualBooks = bookService.findBooksByAuthorId(authorId)

        assertEquals(expectedBooks, actualBooks)
    }

    @Test
    fun `test findBooksByAuthorId returns empty list`() {
        val authorId = 1

        Mockito.`when`(bookDao.fetchByAuthorId(authorId)).thenReturn(emptyList())

        val actualBooks = bookService.findBooksByAuthorId(authorId)

        assertEquals(emptyList<Book>(), actualBooks)
    }

    @Test
    fun `test deleteBookById`() {
        val bookId = 1

        bookService.deleteBookById(bookId)

        Mockito.verify(bookDao).deleteById(bookId)
    }
}