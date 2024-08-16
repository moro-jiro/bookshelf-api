package com.example.bookshelf_api.application.dto

import java.time.LocalDate
import jakarta.validation.constraints.*
import java.time.LocalDateTime

data class AuthorDto(
    @field:NotBlank(message = "名は必須です")
    @field:Size(max = 100, message = "名は100文字以内で入力してください")
    val firstName: String,

    @field:NotBlank(message = "姓は必須です")
    @field:Size(max = 100, message = "姓は100文字以内で入力してください")
    val lastName: String,

    @field:NotNull(message = "生年月日は必須です")
    @field:Past(message = "生年月日は過去の日付である必要があります")
    val birthDate: LocalDate,

    @field:NotBlank(message = "性別は必須です")
    @field:Pattern(regexp = "MALE|FEMALE|OTHER", message = "性別はMALE、FEMALE、OTHERのいずれかである必要があります")
    val gender: String
)

data class AuthorResponse(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val gender: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)