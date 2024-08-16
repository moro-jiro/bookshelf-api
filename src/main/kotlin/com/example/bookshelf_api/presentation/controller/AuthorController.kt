package com.example.bookshelf_api.presentation.controller

import com.example.bookshelf_api.application.service.AuthorService
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Author
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/authors")
class AuthorController(private val authorService: AuthorService) {

    @PostMapping
    fun createAuthor(
        @RequestParam firstName: String,
        @RequestParam lastName: String
    ): ResponseEntity<String> {
        return try {
            val author = Author().apply {
                this.firstName = firstName
                this.lastName = lastName
            }
            authorService.createAuthor(author)
            ResponseEntity.status(HttpStatus.CREATED).build()
        } catch (ex: ResponseStatusException) {
            ResponseEntity.status(ex.statusCode).body(ex.reason)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("サーバーエラーが発生しました")
        }
    }
}
