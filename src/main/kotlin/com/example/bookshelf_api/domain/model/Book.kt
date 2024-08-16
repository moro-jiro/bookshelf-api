package com.example.bookshelf_api.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class Book(
    val id: Int? = null,
    var title: String = "",
    val authorId: Int = 0,
    val publicationDate: LocalDate = LocalDate.now(),
    val publisher: String = "",
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)
