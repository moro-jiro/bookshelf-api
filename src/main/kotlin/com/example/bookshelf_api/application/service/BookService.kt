package com.example.bookshelf_api.application.service

import com.example.bookshelf_api.application.dto.BookCreationRequest
import com.example.bookshelf_api.application.dto.BookResponse
import com.example.bookshelf_api.application.dto.AuthorDto
import com.example.bookshelf_api.application.dto.AuthorResponse
import com.example.bookshelf_api.domain.model.Book
import com.example.bookshelf_api.domain.model.Author
import com.example.bookshelf_api.domain.repository.AuthorRepository
import com.example.bookshelf_api.domain.repository.BookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository
) {

    @Transactional
    fun createBook(bookRequest: BookCreationRequest): BookResponse {
        val authorId = findOrCreateAuthor(bookRequest.author)

        val book = Book(
            title = bookRequest.title,
            authorId = authorId,
            publicationDate = bookRequest.publicationDate,
            publisher = bookRequest.publisher
        )

        val insertedBook = bookRepository.createBook(book)

        return getBookById(insertedBook.id!!)
    }

    private fun findOrCreateAuthor(authorDto: AuthorDto): Int {
        val existingAuthor = authorRepository.findAuthorByDetails(
            authorDto.firstName,
            authorDto.lastName,
            authorDto.birthDate
        )

        return existingAuthor?.id ?: createNewAuthor(authorDto)
    }

    private fun createNewAuthor(authorDto: AuthorDto): Int {
        val author = Author(
            firstName = authorDto.firstName,
            lastName = authorDto.lastName,
            birthDate = authorDto.birthDate,
            gender = authorDto.gender
        )

        return authorRepository.createAuthor(author)
    }

    fun getBookById(id: Int): BookResponse {
        val result = bookRepository.findBookById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "書籍が見つかりません")

        val (bookModel, authorModel) = result
        return BookResponse(
            id = bookModel.id!!,
            title = bookModel.title,
            author = AuthorResponse(
                id = authorModel.id ?: throw IllegalArgumentException("Author ID cannot be null"),
                firstName = authorModel.firstName,
                lastName = authorModel.lastName,
                birthDate = authorModel.birthDate,
                gender = authorModel.gender,
                createdAt = authorModel.createdAt,
                updatedAt = authorModel.updatedAt
            ),
            publicationDate = bookModel.publicationDate,
            publisher = bookModel.publisher,
            createdAt = bookModel.createdAt,
            updatedAt = bookModel.updatedAt
        )
    }
}
