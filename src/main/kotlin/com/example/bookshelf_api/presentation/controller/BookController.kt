package com.example.bookshelf_api.presentation.controller

import com.example.bookshelf_api.application.dto.BookCreationRequest
import com.example.bookshelf_api.application.dto.BookResponse
import com.example.bookshelf_api.application.service.BookService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}
