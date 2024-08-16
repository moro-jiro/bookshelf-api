package com.example.bookshelf_api.presentation.controller

import com.example.bookshelf_api.application.dto.BookCreationRequest
import com.example.bookshelf_api.application.dto.BookResponse
import com.example.bookshelf_api.application.dto.OnlyBookResponse
import com.example.bookshelf_api.application.service.BookService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/books")
class BookController(private val bookService: BookService) {

    @PostMapping("/register")
    fun registerBook(@Valid @RequestBody bookRequest: BookCreationRequest): ResponseEntity<BookResponse> {
        return try {
            val bookResponse = bookService.createBook(bookRequest)
            ResponseEntity.ok(bookResponse)
        } catch (ex: Exception) {
            throw ex
        }
    }

    @PutMapping("/{id}")
    fun updateBookTitle(@PathVariable id: Int, @RequestParam("title") title: String): ResponseEntity<OnlyBookResponse> {
        return try {
            val updatedBook = bookService.updateBookTitle(id, title)
            ResponseEntity.ok(updatedBook)
        } catch (ex: Exception) {
            throw ex
        }
    }

    @GetMapping("/search")
    fun searchBooks(@RequestParam title: String): ResponseEntity<List<OnlyBookResponse>> {
        try {
            val books = bookService.findBooksByTitle(title)
            return ResponseEntity.ok(books)
        } catch (ex: ResponseStatusException) {
            throw ex
        }
    }

}
