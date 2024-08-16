package com.example.bookshelf_api.application.service

import com.example.bookshelf_api.application.dto.AuthorDto
import com.example.bookshelf_api.application.dto.BookDto
import com.example.bookshelf_api.application.dto.UpdateBookDto
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

        val bookRequest = BookDto(
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

        val bookRequest = BookDto(
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

    @Test
    fun `updateBook should update the book's fields and return OnlyBookResponse`() {
        val bookId = 1
        val updateBookDto = UpdateBookDto(
            title = "Updated Title",
            publicationDate = LocalDate.of(2021, 1, 1),
            publisher = "Updated Publisher"
        )
        val existingBook = Book(
            id = bookId,
            title = "Old Title",
            authorId = 1,
            publicationDate = LocalDate.of(2020, 1, 1),
            publisher = "Old Publisher",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val updatedBook = existingBook.copy(
            title = updateBookDto.title,
            publicationDate = updateBookDto.publicationDate,
            publisher = updateBookDto.publisher
        )

        val author = Author(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            birthDate = LocalDate.of(1970, 1, 1),
            gender = "Male",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        `when`(bookRepository.findBookById(bookId)).thenReturn(Pair(existingBook, author))
        `when`(bookRepository.updateBook(updatedBook)).thenReturn(updatedBook)

        val response = bookService.updateBook(bookId, updateBookDto)

        assertNotNull(response)
        assertEquals(updateBookDto.title, response.title)
        assertEquals(updateBookDto.publicationDate, response.publicationDate)
        assertEquals(updateBookDto.publisher, response.publisher)
        assertEquals(bookId, response.id)

        verify(bookRepository).findBookById(bookId)
        verify(bookRepository).updateBook(updatedBook)
    }

    @Test
    fun `updateBook should throw ResponseStatusException when book is not found`() {
        val bookId = 1
        val updateBookDto = UpdateBookDto(
            title = "Updated Title",
            publicationDate = LocalDate.of(2021, 1, 1),
            publisher = "Updated Publisher"
        )

        val fictitiousBook = Book(
            id = bookId,
            title = "Old Title",
            authorId = 1,
            publicationDate = LocalDate.of(2020, 1, 1),
            publisher = "Old Publisher",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        `when`(bookRepository.findBookById(bookId)).thenReturn(null)

        val exception = assertThrows<ResponseStatusException> {
            bookService.updateBook(bookId, updateBookDto)
        }

        assertEquals("404 NOT_FOUND \"書籍が見つかりません\"", exception.message)

        verify(bookRepository).findBookById(bookId)
        verify(bookRepository, never()).updateBook(fictitiousBook)
    }

    @Test
    fun `findBooksByTitle should return a list of OnlyBookResponse when books are found`() {
        val title = "Book Title"
        val book1 = Book(
            id = 1,
            title = title,
            authorId = 1,
            publicationDate = LocalDate.of(2020, 1, 1),
            publisher = "Publisher",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val book2 = Book(
            id = 2,
            title = title,
            authorId = 2,
            publicationDate = LocalDate.of(2021, 2, 2),
            publisher = "Another Publisher",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        `when`(bookRepository.findBooksByTitle(title)).thenReturn(listOf(book1, book2))

        val responses = bookService.findBooksByTitle(title)

        assertEquals(2, responses.size)
        assertEquals(title, responses[0].title)
        assertEquals(title, responses[1].title)
    }

    @Test
    fun `findBooksByTitle should throw ResponseStatusException when title is blank`() {
        val title = ""

        val exception = assertThrows<ResponseStatusException> {
            bookService.findBooksByTitle(title)
        }

        assertEquals("400 BAD_REQUEST \"書籍名は必須です\"", exception.message)
    }

    @Test
    fun `getBooksByAuthorId should return a list of OnlyBookResponse`() {
        val authorId = 1
        val books = listOf(
            Book(
                id = 1,
                title = "Book Title 1",
                authorId = authorId,
                publicationDate = LocalDate.of(2022, 1, 1),
                publisher = "Publisher 1",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            Book(
                id = 2,
                title = "Book Title 2",
                authorId = authorId,
                publicationDate = LocalDate.of(2023, 1, 1),
                publisher = "Publisher 2",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        `when`(bookRepository.findBooksByAuthorId(authorId)).thenReturn(books)

        val response = bookService.getBooksByAuthorId(authorId)

        assertNotNull(response)
        assertEquals(2, response.size)

        val firstBookResponse = response[0]
        assertEquals(1, firstBookResponse.id)
        assertEquals("Book Title 1", firstBookResponse.title)
        assertEquals(authorId, firstBookResponse.authorId)
        assertEquals(LocalDate.of(2022, 1, 1), firstBookResponse.publicationDate)
        assertEquals("Publisher 1", firstBookResponse.publisher)

        val secondBookResponse = response[1]
        assertEquals(2, secondBookResponse.id)
        assertEquals("Book Title 2", secondBookResponse.title)
        assertEquals(authorId, secondBookResponse.authorId)
        assertEquals(LocalDate.of(2023, 1, 1), secondBookResponse.publicationDate)
        assertEquals("Publisher 2", secondBookResponse.publisher)

        verify(bookRepository).findBooksByAuthorId(authorId)
    }

    @Test
    fun `getBooksByAuthorId should return an empty list when no books are found`() {
        val authorId = 1

        `when`(bookRepository.findBooksByAuthorId(authorId)).thenReturn(emptyList())

        val response = bookService.getBooksByAuthorId(authorId)

        assertNotNull(response)
        assertTrue(response.isEmpty())

        verify(bookRepository).findBooksByAuthorId(authorId)
    }
}
