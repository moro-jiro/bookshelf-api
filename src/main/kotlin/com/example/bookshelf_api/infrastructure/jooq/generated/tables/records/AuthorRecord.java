/*
 * This file is generated by jOOQ.
 */
package com.example.bookshelf_api.infrastructure.jooq.generated.tables.records;


import com.example.bookshelf_api.infrastructure.jooq.generated.tables.Author;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class AuthorRecord extends UpdatableRecordImpl<AuthorRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.author.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.author.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.author.last_name</code>.
     */
    public void setLastName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.author.last_name</code>.
     */
    public String getLastName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.author.first_name</code>.
     */
    public void setFirstName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.author.first_name</code>.
     */
    public String getFirstName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.author.birth_date</code>.
     */
    public void setBirthDate(LocalDate value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.author.birth_date</code>.
     */
    public LocalDate getBirthDate() {
        return (LocalDate) get(3);
    }

    /**
     * Setter for <code>public.author.gender</code>.
     */
    public void setGender(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.author.gender</code>.
     */
    public String getGender() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.author.created_at</code>.
     */
    public void setCreatedAt(LocalDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.author.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(5);
    }

    /**
     * Setter for <code>public.author.updated_at</code>.
     */
    public void setUpdatedAt(LocalDateTime value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.author.updated_at</code>.
     */
    public LocalDateTime getUpdatedAt() {
        return (LocalDateTime) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AuthorRecord
     */
    public AuthorRecord() {
        super(Author.AUTHOR);
    }

    /**
     * Create a detached, initialised AuthorRecord
     */
    public AuthorRecord(Integer id, String lastName, String firstName, LocalDate birthDate, String gender, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(Author.AUTHOR);

        setId(id);
        setLastName(lastName);
        setFirstName(firstName);
        setBirthDate(birthDate);
        setGender(gender);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised AuthorRecord
     */
    public AuthorRecord(com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Author value) {
        super(Author.AUTHOR);

        if (value != null) {
            setId(value.getId());
            setLastName(value.getLastName());
            setFirstName(value.getFirstName());
            setBirthDate(value.getBirthDate());
            setGender(value.getGender());
            setCreatedAt(value.getCreatedAt());
            setUpdatedAt(value.getUpdatedAt());
            resetChangedOnNotNull();
        }
    }
}
