package com.example.bookshelf_api.domain.repository

import com.example.bookshelf_api.application.dto.AuthorSearchDto
import com.example.bookshelf_api.domain.model.Author
import java.time.LocalDate

interface AuthorRepository {
    fun createAuthor(author: Author): Int
    fun findAuthorByDetails(firstName: String, lastName: String, birthDate: LocalDate): Author?
    fun findAuthorById(id: Int): Author?
    fun updateAuthor(author: Author)
    fun searchAuthors(searchDto: AuthorSearchDto): List<Author>
}
