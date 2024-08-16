package com.example.bookshelf.bookshelf_api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BookshelfApiApplication

fun main(args: Array<String>) {
	runApplication<BookshelfApiApplication>(*args)
}
