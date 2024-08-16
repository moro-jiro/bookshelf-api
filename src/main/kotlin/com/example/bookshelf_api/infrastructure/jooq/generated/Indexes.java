/*
 * This file is generated by jOOQ.
 */
package com.example.bookshelf_api.infrastructure.jooq.generated;


import com.example.bookshelf_api.infrastructure.jooq.generated.tables.Author;
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.Book;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables in public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index IDX_AUTHOR_BIRTH_DATE = Internal.createIndex(DSL.name("idx_author_birth_date"), Author.AUTHOR, new OrderField[] { Author.AUTHOR.BIRTH_DATE }, false);
    public static final Index IDX_AUTHOR_FIRST_NAME = Internal.createIndex(DSL.name("idx_author_first_name"), Author.AUTHOR, new OrderField[] { Author.AUTHOR.FIRST_NAME }, false);
    public static final Index IDX_AUTHOR_FULL_NAME = Internal.createIndex(DSL.name("idx_author_full_name"), Author.AUTHOR, new OrderField[] { Author.AUTHOR.LAST_NAME, Author.AUTHOR.FIRST_NAME }, false);
    public static final Index IDX_AUTHOR_LAST_NAME = Internal.createIndex(DSL.name("idx_author_last_name"), Author.AUTHOR, new OrderField[] { Author.AUTHOR.LAST_NAME }, false);
    public static final Index IDX_BOOKS_TITLE = Internal.createIndex(DSL.name("idx_books_title"), Book.BOOK, new OrderField[] { Book.BOOK.TITLE }, false);
}
