package com.example.bookshelf_api.presentation.controller

import com.example.bookshelf_api.application.dto.AuthorDto
import com.example.bookshelf_api.application.dto.AuthorResponse
import com.example.bookshelf_api.application.service.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/authors")
class AuthorController(private val authorService: AuthorService) {

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
}
