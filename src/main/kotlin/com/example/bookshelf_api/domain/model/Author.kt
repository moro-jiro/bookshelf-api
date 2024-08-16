package com.example.bookshelf_api.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class Author(
    val id: Int? = null,
    val lastName: String = "",
    val firstName: String = "",
    val birthDate: LocalDate = LocalDate.now(),
    val gender: String = "",
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)

