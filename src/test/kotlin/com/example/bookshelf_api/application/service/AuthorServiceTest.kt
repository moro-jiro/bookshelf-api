package com.example.bookshelf_api.application.service

import com.example.bookshelf_api.infrastructure.jooq.generated.tables.daos.AuthorDao
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Author
import com.example.bookshelf_api.infrastructure.repository.CustomAuthorDao
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.server.ResponseStatusException

@ExtendWith(MockitoExtension::class)
class AuthorServiceTest {

    @Mock
    private lateinit var authorDao: AuthorDao

    @Mock
    private lateinit var customAuthorDao: CustomAuthorDao

    @InjectMocks
    private lateinit var authorService: AuthorService

    @BeforeEach
    fun setUp() {
        Mockito.reset(authorDao, customAuthorDao)
    }

    @AfterEach
    fun tearDown() {
        Mockito.reset(authorDao, customAuthorDao)
    }


    @Test
    fun `test createAuthor successfully`() {
        val author = Author().apply {
            id = 1
            firstName = "Kojiro"
            lastName = "Morooka"
        }

        Mockito.`when`(customAuthorDao.fetchByFirstNameAndLastName("Kojiro", "Morooka")).thenReturn(emptyList())

        authorService.createAuthor(author)

        Mockito.verify(customAuthorDao).insert(author)
    }

    @Test
    fun `test createAuthor with duplicate name`() {
        val author = Author().apply {
            id = 1
            firstName = "Kojiro"
            lastName = "Morooka"
        }

        Mockito.`when`(customAuthorDao.fetchByFirstNameAndLastName("Kojiro", "Morooka"))
            .thenReturn(listOf(author))

        val exception = assertThrows<ResponseStatusException> {
            authorService.createAuthor(author)
        }

        assertEquals("同じ名前の著者が既に登録されています", exception.reason)

        Mockito.verify(customAuthorDao, never()).insert(author)
    }

    @Test
    fun `test updateAuthor`() {
        val author = Author().apply {
            id = 1
            firstName = "Kojiro"
            lastName = "Morooka"
        }

        authorService.updateAuthor(author)
        Mockito.verify(authorDao).update(author)
    }

    @Test
    fun `test findAuthorById`() {
        val authorId = 1
        val expectedAuthor = Author().apply {
            id = authorId
            firstName = "Kojiro"
            lastName = "Morooka"
        }

        Mockito.`when`(authorDao.fetchOneById(authorId)).thenReturn(expectedAuthor)

        val actualAuthor = authorService.findAuthorById(authorId)

        assertEquals(expectedAuthor, actualAuthor)
    }

    @Test
    fun `test findAuthorById returns null`() {
        val authorId = 1

        Mockito.`when`(authorDao.fetchOneById(authorId)).thenReturn(null)

        val actualAuthor = authorService.findAuthorById(authorId)

        assertNull(actualAuthor)
    }

    @Test
    fun `test deleteAuthorById`() {
        val authorId = 1

        authorService.deleteAuthorById(authorId)

        Mockito.verify(authorDao).deleteById(authorId)
    }
}
