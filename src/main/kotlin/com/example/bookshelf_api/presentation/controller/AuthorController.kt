package com.example.bookshelf_api.presentation.controller

import com.example.bookshelf_api.application.dto.AuthorDto
import com.example.bookshelf_api.application.dto.AuthorResponse
import com.example.bookshelf_api.application.dto.AuthorSearchDto
import com.example.bookshelf_api.application.dto.OnlyBookResponse
import com.example.bookshelf_api.application.service.AuthorService
import com.example.bookshelf_api.application.service.BookService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

@RestController
@RequestMapping("/api/authors")
class AuthorController(
    private val authorService: AuthorService,
    private val bookService: BookService
) {

    @PostMapping("/register")
    fun createAuthor(@RequestBody authorDto: AuthorDto): ResponseEntity<AuthorResponse> {
        try {
            val authorResponse = authorService.createAuthor(authorDto)
            return ResponseEntity.status(HttpStatus.CREATED).body(authorResponse)
        } catch (ex: ResponseStatusException) {
            throw ex
        }
    }

    @PutMapping("/{id}")
    fun updateAuthor(
        @PathVariable id: Int,
        @RequestBody authorDto: AuthorDto
    ): ResponseEntity<AuthorResponse> {
        val updatedAuthor = authorService.updateAuthor(id, authorDto)
        return ResponseEntity.ok(updatedAuthor)
    }

    @GetMapping("/search")
    fun searchAuthors(
        @RequestParam lastName: String,
        @RequestParam firstName: String,
        @RequestParam(required = false) birthDate: String?,
        @RequestParam(required = false) gender: String?
    ): ResponseEntity<List<AuthorResponse>> {
        try {
            val searchDto = AuthorSearchDto(
                lastName = lastName,
                firstName = firstName,
                birthDate = birthDate?.let { LocalDate.parse(it) },
                gender = gender
            )
            val authors = authorService.searchAuthors(searchDto)
            return ResponseEntity.ok(authors)
        } catch (ex: ResponseStatusException) {
            throw ex
        } catch (ex: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.message)
        }
    }

    @GetMapping("/{authorId}/books")
    fun getBooksByAuthorId(@PathVariable authorId: Int): ResponseEntity<List<OnlyBookResponse>> {
        return try {
            val books = bookService.getBooksByAuthorId(authorId)
            ResponseEntity.ok(books)
        } catch (ex: ResponseStatusException) {
            throw ex
        }
    }
}
