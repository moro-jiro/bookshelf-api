package com.example.bookshelf_api.application.service

import com.example.bookshelf_api.infrastructure.jooq.generated.tables.daos.AuthorDao
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Author
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class AuthorServiceTest {

    @Mock
    private lateinit var authorDao: AuthorDao

    @InjectMocks
    private lateinit var authorService: AuthorService

    @Test
    fun `test createAuthor`() {
        val author = Author().apply {
            id = 1
            name = "Kojiro Morooka"
        }

        authorService.createAuthor(author)

        Mockito.verify(authorDao).insert(author)
    }

    @Test
    fun `test updateAuthor`() {
        val author = Author().apply {
            id = 1
            name = "Kojiro Morooka"
        }

        authorService.updateAuthor(author)

        Mockito.verify(authorDao).update(author)
    }

    @Test
    fun `test findAuthorById`() {
        val authorId = 1
        val expectedAuthor = Author().apply {
            id = authorId
            name = "Kojiro Morooka"
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
