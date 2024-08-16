package com.example.bookshelf_api.application.service

import com.example.bookshelf_api.application.dto.AuthorDto
import com.example.bookshelf_api.application.dto.AuthorSearchDto
import com.example.bookshelf_api.domain.model.Author
import com.example.bookshelf_api.domain.repository.AuthorRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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

        `when`(
            authorRepository.createAuthor(
                any(Author::class.java) ?: Author()
            )
        ).thenThrow(RuntimeException("Database error"))

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

    @Test
    fun `updateAuthor should update author information successfully`() {
        val authorId = 1
        val existingAuthor = Author(
            id = authorId,
            firstName = "John",
            lastName = "Doe",
            birthDate = LocalDate.of(1980, 1, 1),
            gender = "MALE",
            createdAt = null,
            updatedAt = null
        )

        val authorDto = AuthorDto(
            firstName = "Jane",
            lastName = "Doe",
            birthDate = LocalDate.of(1985, 5, 5),
            gender = "FEMALE"
        )

        val updatedAuthor = existingAuthor.copy(
            firstName = authorDto.firstName,
            lastName = authorDto.lastName,
            birthDate = authorDto.birthDate,
            gender = authorDto.gender
        )

        `when`(authorRepository.findAuthorById(authorId)).thenReturn(existingAuthor)
        doNothing().`when`(authorRepository).updateAuthor(updatedAuthor)

        val result = authorService.updateAuthor(authorId, authorDto)

        assertEquals(authorId, result.id)
        assertEquals(authorDto.firstName, result.firstName)
        assertEquals(authorDto.lastName, result.lastName)
        assertEquals(authorDto.birthDate, result.birthDate)
        assertEquals(authorDto.gender, result.gender)

        verify(authorRepository).findAuthorById(authorId)
        verify(authorRepository).updateAuthor(updatedAuthor)
    }

    @Test
    fun `updateAuthor should throw ResponseStatusException when author is not found`() {
        val authorId = 1
        val authorDto = AuthorDto(
            firstName = "Jane",
            lastName = "Doe",
            birthDate = LocalDate.of(1985, 5, 5),
            gender = "FEMALE"
        )

        `when`(authorRepository.findAuthorById(authorId)).thenReturn(null)

        val exception = assertThrows<ResponseStatusException> {
            authorService.updateAuthor(authorId, authorDto)
        }

        assertEquals("404 NOT_FOUND \"著者が見つかりません\"", exception.message)

        verify(authorRepository).findAuthorById(authorId)
        verify(authorRepository, never()).updateAuthor(Author())
    }

    @Test
    fun `updateAuthor should throw ResponseStatusException when update fails`() {
        val authorId = 1
        val existingAuthor = Author(
            id = authorId,
            firstName = "John",
            lastName = "Doe",
            birthDate = LocalDate.of(1980, 1, 1),
            gender = "MALE",
            createdAt = null,
            updatedAt = null
        )

        val authorDto = AuthorDto(
            firstName = "Jane",
            lastName = "Doe",
            birthDate = LocalDate.of(1985, 5, 5),
            gender = "FEMALE"
        )

        val updatedAuthor = existingAuthor.copy(
            firstName = authorDto.firstName,
            lastName = authorDto.lastName,
            birthDate = authorDto.birthDate,
            gender = authorDto.gender
        )

        `when`(authorRepository.findAuthorById(authorId)).thenReturn(existingAuthor)
        doThrow(RuntimeException("Update failed")).`when`(authorRepository).updateAuthor(updatedAuthor)

        val exception = assertThrows<ResponseStatusException> {
            authorService.updateAuthor(authorId, authorDto)
        }

        assertEquals("400 BAD_REQUEST \"著者の更新中にエラーが発生しました: Update failed\"", exception.message)

        verify(authorRepository).findAuthorById(authorId)
        verify(authorRepository).updateAuthor(updatedAuthor)
    }

    @Test
    fun `searchAuthors should return list of AuthorResponse`() {
        val searchDto = AuthorSearchDto(
            lastName = "Doe",
            firstName = "Jane",
            birthDate = null,
            gender = null
        )

        val authors = listOf(
            Author(
                id = 1,
                firstName = "Jane",
                lastName = "Doe",
                birthDate = LocalDate.of(1985, 5, 5),
                gender = "FEMALE",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            Author(
                id = 2,
                firstName = "John",
                lastName = "Doe",
                birthDate = LocalDate.of(1990, 1, 1),
                gender = "MALE",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        `when`(authorRepository.searchAuthors(searchDto)).thenReturn(authors)

        val result = authorService.searchAuthors(searchDto)

        assertEquals(2, result.size)
        assertEquals(1, result[0].id)
        assertEquals("Jane", result[0].firstName)
        assertEquals("Doe", result[0].lastName)
        assertEquals(LocalDate.of(1985, 5, 5), result[0].birthDate)
        assertEquals("FEMALE", result[0].gender)

        assertEquals(2, result[1].id)
        assertEquals("John", result[1].firstName)
        assertEquals("Doe", result[1].lastName)
        assertEquals(LocalDate.of(1990, 1, 1), result[1].birthDate)
        assertEquals("MALE", result[1].gender)

        verify(authorRepository).searchAuthors(searchDto)
    }
}
