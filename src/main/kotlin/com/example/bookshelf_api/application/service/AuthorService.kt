package com.example.bookshelf_api.application.service

import com.example.bookshelf_api.application.dto.AuthorDto
import com.example.bookshelf_api.application.dto.AuthorResponse
import com.example.bookshelf_api.domain.model.Author
import com.example.bookshelf_api.domain.repository.AuthorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus

@Service
class AuthorService(private val authorRepository: AuthorRepository) {

    @Transactional
    fun createAuthor(authorDto: AuthorDto): AuthorResponse {
        try {
            val author = Author(
                firstName = authorDto.firstName,
                lastName = authorDto.lastName,
                birthDate = authorDto.birthDate,
                gender = authorDto.gender
            )

            val authorId = authorRepository.createAuthor(author)

            return getAuthorById(authorId)
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "著者の作成中にエラーが発生しました: ${ex.message}")
        }
    }

    fun getAuthorById(id: Int): AuthorResponse {
        val author = authorRepository.findAuthorById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "著者が見つかりません")

        return AuthorResponse(
            id = author.id ?: throw IllegalArgumentException("Author ID cannot be null"),
            firstName = author.firstName,
            lastName = author.lastName,
            birthDate = author.birthDate,
            gender = author.gender,
            createdAt = author.createdAt,
            updatedAt = author.updatedAt
        )
    }
}
