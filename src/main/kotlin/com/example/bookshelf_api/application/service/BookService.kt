package com.example.bookshelf_api.application.service

import com.example.bookshelf_api.application.dto.*
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
    fun createBook(bookRequest: BookDto): BookResponse {
        val authorId = findOrCreateAuthor(bookRequest.author)

        val book = Book(
            title = bookRequest.title,
            authorId = authorId,
            publicationDate = bookRequest.publicationDate,
            publisher = bookRequest.publisher
        )

        val insertedBook = bookRepository.createBook(book)

        return getBookById(insertedBook.id ?: throw IllegalArgumentException("Book ID cannot be null"))
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

    @Transactional(readOnly = true)
    fun getBookById(id: Int): BookResponse {
        val result = bookRepository.findBookById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "書籍が見つかりません")

        val (bookModel, authorModel) = result
        return bookModel.toBookResponse(authorModel)
    }

    @Transactional
    fun updateBook(id: Int, updateBookDto: UpdateBookDto): OnlyBookResponse {
        val existingBook = bookRepository.findBookById(id)?.first
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "書籍が見つかりません")

        val updatedBook = existingBook.copy(
            title = updateBookDto.title,
            publicationDate = updateBookDto.publicationDate,
            publisher = updateBookDto.publisher
        )

        bookRepository.updateBook(updatedBook)

        return updatedBook.toOnlyBookResponse()
    }

    @Transactional(readOnly = true)
    fun findBooksByTitle(title: String): List<OnlyBookResponse> {
        if (title.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "書籍名は必須です")
        }
        val books = bookRepository.findBooksByTitle(title)
        return books.map { book ->
            book.toOnlyBookResponse()
        }
    }

    @Transactional(readOnly = true)
    fun getBooksByAuthorId(authorId: Int): List<OnlyBookResponse> {
        val books = bookRepository.findBooksByAuthorId(authorId)
        return books.map { book ->
            book.toOnlyBookResponse()
        }
    }

    private fun Book.toOnlyBookResponse(): OnlyBookResponse {
        return OnlyBookResponse(
            id = this.id ?: throw IllegalArgumentException("Book ID cannot be null"),
            title = this.title,
            authorId = this.authorId,
            publicationDate = this.publicationDate,
            publisher = this.publisher,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }

    private fun Book.toBookResponse(author: Author): BookResponse {
        return BookResponse(
            id = this.id ?: throw IllegalArgumentException("Book ID cannot be null"),
            title = this.title,
            author = author.toAuthorResponse(),
            publicationDate = this.publicationDate,
            publisher = this.publisher,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }

    private fun Author.toAuthorResponse(): AuthorResponse {
        return AuthorResponse(
            id = this.id ?: throw IllegalArgumentException("Author ID cannot be null"),
            firstName = this.firstName,
            lastName = this.lastName,
            birthDate = this.birthDate,
            gender = this.gender,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}
