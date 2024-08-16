package com.example.bookshelf_api.application.service

import com.example.bookshelf_api.application.dto.AuthorDto
import com.example.bookshelf_api.application.dto.AuthorResponse
import com.example.bookshelf_api.application.dto.AuthorSearchDto
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

    @Transactional(readOnly = true)
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

    @Transactional
    fun updateAuthor(id: Int, authorDto: AuthorDto): AuthorResponse {
        val existingAuthor = authorRepository.findAuthorById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "著者が見つかりません")

        val updatedAuthor = existingAuthor.copy(
            firstName = authorDto.firstName,
            lastName = authorDto.lastName,
            birthDate = authorDto.birthDate,
            gender = authorDto.gender
        )

        try {
            authorRepository.updateAuthor(updatedAuthor)
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "著者の更新中にエラーが発生しました: ${ex.message}")
        }

        return AuthorResponse(
            id = updatedAuthor.id ?: throw IllegalArgumentException("Author ID cannot be null"),
            firstName = updatedAuthor.firstName,
            lastName = updatedAuthor.lastName,
            birthDate = updatedAuthor.birthDate,
            gender = updatedAuthor.gender,
            createdAt = updatedAuthor.createdAt,
            updatedAt = updatedAuthor.updatedAt
        )
    }

    @Transactional(readOnly = true)
    fun searchAuthors(searchDto: AuthorSearchDto): List<AuthorResponse> {
        val authors = authorRepository.searchAuthors(searchDto)
        return authors.map { author ->
            AuthorResponse(
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
}
