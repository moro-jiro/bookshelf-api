package com.example.bookshelf_api.application.service

import com.example.bookshelf_api.application.dto.AuthorDto
import com.example.bookshelf_api.domain.model.Author
import com.example.bookshelf_api.domain.repository.AuthorRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthorServiceTest {

    private val authorRepository: AuthorRepository = mock(AuthorRepository::class.java)
    private val authorService = AuthorService(authorRepository)

    @Test
    fun `createAuthor should return AuthorResponse when author is created successfully`() {
        val authorDto = AuthorDto(
            firstName = "John",
            lastName = "Doe",
            birthDate = LocalDate.of(1970, 1, 1),
            gender = "Male"
        )

        val author = Author(
            id = 1,
            firstName = authorDto.firstName,
            lastName = authorDto.lastName,
            birthDate = authorDto.birthDate,
            gender = authorDto.gender,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        `when`(authorRepository.createAuthor(any(Author::class.java) ?: Author())).thenReturn(author.id!!)
        `when`(authorRepository.findAuthorById(author.id!!)).thenReturn(author)

        val response = authorService.createAuthor(authorDto)

        assertEquals(author.id, response.id)
        assertEquals(authorDto.firstName, response.firstName)
        assertEquals(authorDto.lastName, response.lastName)
        assertEquals(authorDto.birthDate, response.birthDate)
        assertEquals(authorDto.gender, response.gender)
    }

    @Test
    fun `createAuthor should throw ResponseStatusException when an error occurs during author creation`() {
        val authorDto = AuthorDto(
            firstName = "John",
            lastName = "Doe",
            birthDate = LocalDate.of(1970, 1, 1),
            gender = "Male"
        )

        `when`(authorRepository.createAuthor(any(Author::class.java) ?: Author())).thenThrow(RuntimeException("Database error"))

        val exception = assertFailsWith<ResponseStatusException> {
            authorService.createAuthor(authorDto)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
        assertEquals("著者の作成中にエラーが発生しました: Database error", exception.reason)
    }

    @Test
    fun `getAuthorById should return AuthorResponse when author is found`() {
        val author = Author(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            birthDate = LocalDate.of(1970, 1, 1),
            gender = "Male",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        `when`(authorRepository.findAuthorById(author.id!!)).thenReturn(author)

        val response = authorService.getAuthorById(author.id!!)

        assertEquals(author.id, response.id)
        assertEquals(author.firstName, response.firstName)
        assertEquals(author.lastName, response.lastName)
        assertEquals(author.birthDate, response.birthDate)
        assertEquals(author.gender, response.gender)
    }

    @Test
    fun `getAuthorById should throw ResponseStatusException when author is not found`() {
        val authorId = 1

        `when`(authorRepository.findAuthorById(authorId)).thenReturn(null)

        val exception = assertFailsWith<ResponseStatusException> {
            authorService.getAuthorById(authorId)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.statusCode)
        assertEquals("著者が見つかりません", exception.reason)
    }
}
