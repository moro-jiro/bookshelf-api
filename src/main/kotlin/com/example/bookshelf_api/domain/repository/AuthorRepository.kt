package com.example.bookshelf_api.domain.repository

import com.example.bookshelf_api.domain.model.Author
import java.time.LocalDate

interface AuthorRepository {
    fun createAuthor(author: Author): Int
    fun findAuthorByDetails(firstName: String, lastName: String, birthDate: LocalDate): Author?
}
