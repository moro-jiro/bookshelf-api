/*
 * This file is generated by jOOQ.
 */
package com.example.bookshelf_api.infrastructure.jooq.generated.tables.daos;


import com.example.bookshelf_api.infrastructure.jooq.generated.tables.Book;
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.records.BookRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class BookDao extends DAOImpl<BookRecord, com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book, Integer> {

    /**
     * Create a new BookDao without any configuration
     */
    public BookDao() {
        super(Book.BOOK, com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book.class);
    }

    /**
     * Create a new BookDao with an attached configuration
     */
    public BookDao(Configuration configuration) {
        super(Book.BOOK, com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book.class, configuration);
    }

    @Override
    public Integer getId(com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book> fetchRangeOfId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Book.BOOK.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book> fetchById(Integer... values) {
        return fetch(Book.BOOK.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book fetchOneById(Integer value) {
        return fetchOne(Book.BOOK.ID, value);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public Optional<com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book> fetchOptionalById(Integer value) {
        return fetchOptional(Book.BOOK.ID, value);
    }

    /**
     * Fetch records that have <code>title BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book> fetchRangeOfTitle(String lowerInclusive, String upperInclusive) {
        return fetchRange(Book.BOOK.TITLE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>title IN (values)</code>
     */
    public List<com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book> fetchByTitle(String... values) {
        return fetch(Book.BOOK.TITLE, values);
    }

    /**
     * Fetch records that have <code>publication_date BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book> fetchRangeOfPublicationDate(LocalDate lowerInclusive, LocalDate upperInclusive) {
        return fetchRange(Book.BOOK.PUBLICATION_DATE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>publication_date IN (values)</code>
     */
    public List<com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book> fetchByPublicationDate(LocalDate... values) {
        return fetch(Book.BOOK.PUBLICATION_DATE, values);
    }

    /**
     * Fetch records that have <code>author_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book> fetchRangeOfAuthorId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Book.BOOK.AUTHOR_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>author_id IN (values)</code>
     */
    public List<com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book> fetchByAuthorId(Integer... values) {
        return fetch(Book.BOOK.AUTHOR_ID, values);
    }

    /**
     * Fetch records that have <code>publisher BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book> fetchRangeOfPublisher(String lowerInclusive, String upperInclusive) {
        return fetchRange(Book.BOOK.PUBLISHER, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>publisher IN (values)</code>
     */
    public List<com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book> fetchByPublisher(String... values) {
        return fetch(Book.BOOK.PUBLISHER, values);
    }

    /**
     * Fetch records that have <code>created_at BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book> fetchRangeOfCreatedAt(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(Book.BOOK.CREATED_AT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>created_at IN (values)</code>
     */
    public List<com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book> fetchByCreatedAt(LocalDateTime... values) {
        return fetch(Book.BOOK.CREATED_AT, values);
    }

    /**
     * Fetch records that have <code>updated_at BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book> fetchRangeOfUpdatedAt(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(Book.BOOK.UPDATED_AT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>updated_at IN (values)</code>
     */
    public List<com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Book> fetchByUpdatedAt(LocalDateTime... values) {
        return fetch(Book.BOOK.UPDATED_AT, values);
    }
}
