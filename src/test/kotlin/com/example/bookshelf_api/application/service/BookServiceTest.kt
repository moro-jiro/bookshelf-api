package com.example.bookshelf_api.application.service

import com.example.bookshelf_api.application.dto.AuthorDto
import com.example.bookshelf_api.application.dto.BookCreationRequest
import com.example.bookshelf_api.domain.model.Author
import com.example.bookshelf_api.domain.model.Book
import com.example.bookshelf_api.domain.repository.AuthorRepository
import com.example.bookshelf_api.domain.repository.BookRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.LocalDateTime

class BookServiceTest {

    private val bookRepository: BookRepository = mock(BookRepository::class.java)
    private val authorRepository: AuthorRepository = mock(AuthorRepository::class.java)
    private val bookService = BookService(bookRepository, authorRepository)

    @Test
    fun `createBook should return BookResponse when book is created successfully`() {
        val authorDto = AuthorDto(
            firstName = "John",
            lastName = "Doe",
            birthDate = LocalDate.of(1990, 1, 1),
            gender = "Male"
        )

        val bookRequest = BookCreationRequest(
            title = "Sample Book",
            author = authorDto,
            publicationDate = LocalDate.of(2024, 8, 1),
            publisher = "Sample Publisher"
        )

        val author = Author(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            birthDate = LocalDate.of(1990, 1, 1),
            gender = "Male"
        )

        val book = Book(
            id = 1,
            title = "Sample Book",
            authorId = author.id!!,
            publicationDate = LocalDate.of(2024, 8, 1),
            publisher = "Sample Publisher"
        )

        `when`(authorRepository.findAuthorByDetails("John", "Doe", LocalDate.of(1990, 1, 1)))
            .thenReturn(author)
        `when`(bookRepository.createBook(any(Book::class.java) ?: Book())).thenReturn(book)
        `when`(bookRepository.findBookById(book.id!!))
            .thenReturn(Pair(book, author))

        val response = bookService.createBook(bookRequest)

        assertNotNull(response)
        assertEquals("Sample Book", response.title)
        assertEquals(1, response.id)
        assertEquals("John", response.author.firstName)
        assertEquals("Doe", response.author.lastName)
    }

    @Test
    fun `getBookById should return BookResponse when book is found`() {
        val author = Author(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            birthDate = LocalDate.of(1990, 1, 1),
            gender = "Male",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val book = Book(
            id = 1,
            title = "Sample Book",
            authorId = author.id!!,
            publicationDate = LocalDate.of(2024, 8, 1),
            publisher = "Sample Publisher",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        `when`(bookRepository.findBookById(1)).thenReturn(Pair(book, author))

        val response = bookService.getBookById(1)

        assertNotNull(response)
        assertEquals("Sample Book", response.title)
        assertEquals(1, response.id)
        assertEquals("John", response.author.firstName)
        assertEquals("Doe", response.author.lastName)
    }

    @Test
    fun `getBookById should throw ResponseStatusException when book is not found`() {
        `when`(bookRepository.findBookById(1)).thenReturn(null)

        val exception = assertThrows<ResponseStatusException> {
            bookService.getBookById(1)
        }

        assertEquals("404 NOT_FOUND \"書籍が見つかりません\"", exception.message)
    }

    @Test
    fun `createBook should create new author if not found`() {
        val authorDto = AuthorDto(
            firstName = "Jane",
            lastName = "Doe",
            birthDate = LocalDate.of(1990, 1, 1),
            gender = "Female"
        )

        val bookRequest = BookCreationRequest(
            title = "New Book",
            author = authorDto,
            publicationDate = LocalDate.of(2024, 8, 1),
            publisher = "New Publisher"
        )

        val newAuthor = Author(
            id = 2,
            firstName = "Jane",
            lastName = "Doe",
            birthDate = LocalDate.of(1990, 1, 1),
            gender = "Female"
        )

        val book = Book(
            id = 2,
            title = "New Book",
            authorId = newAuthor.id!!,
            publicationDate = LocalDate.of(2024, 8, 1),
            publisher = "New Publisher"
        )

        `when`(authorRepository.findAuthorByDetails("Jane", "Doe", LocalDate.of(1990, 1, 1)))
            .thenReturn(null)
        `when`(authorRepository.createAuthor(any(Author::class.java) ?: Author())).thenReturn(2)
        `when`(bookRepository.createBook(any(Book::class.java) ?: Book())).thenReturn(book)
        `when`(bookRepository.findBookById(2)).thenReturn(Pair(book, newAuthor))

        val response = bookService.createBook(bookRequest)

        assertNotNull(response)
        assertEquals("New Book", response.title)
        assertEquals(2, response.id)
        assertEquals("Jane", response.author.firstName)
        assertEquals("Doe", response.author.lastName)
    }
}
