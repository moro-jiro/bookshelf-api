package com.example.bookshelf_api.application.dto

import jakarta.validation.Valid
import java.time.LocalDate
import jakarta.validation.constraints.*
import java.time.LocalDateTime

data class BookDto(
    @field:NotBlank(message = "タイトルは必須です")
    @field:Size(max = 255, message = "タイトルは255文字以内で入力してください")
    val title: String,

    @field:NotNull(message = "著者情報は必須です")
    @field:Valid
    val author: AuthorDto,

    @field:NotNull(message = "出版日は必須です")
    @field:PastOrPresent(message = "出版日は現在以前の日付である必要があります")
    val publicationDate: LocalDate,

    @field:NotBlank(message = "出版社は必須です")
    @field:Size(max = 255, message = "出版社名は255文字以内で入力してください")
    val publisher: String
)

data class UpdateBookDto(
    @field:NotBlank(message = "タイトルは必須です")
    @field:Size(max = 255, message = "タイトルは255文字以内で入力してください")
    val title: String,

    @field:NotNull(message = "出版日は必須です")
    @field:PastOrPresent(message = "出版日は現在以前の日付である必要があります")
    val publicationDate: LocalDate,

    @field:NotBlank(message = "出版社は必須です")
    @field:Size(max = 255, message = "出版社名は255文字以内で入力してください")
    val publisher: String
)

data class BookResponse(
    val id: Int,
    val title: String,
    val author: AuthorResponse,
    val publicationDate: LocalDate,
    val publisher: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)

data class OnlyBookResponse(
    val id: Int,
    val title: String,
    val authorId: Int,
    val publicationDate: LocalDate,
    val publisher: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)