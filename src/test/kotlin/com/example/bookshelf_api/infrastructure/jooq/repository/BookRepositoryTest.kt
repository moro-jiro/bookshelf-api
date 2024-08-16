package com.example.bookshelf_api.infrastructure.jooq.repository

import com.example.bookshelf_api.infrastructure.jooq.generated.tables.Book.BOOK
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.records.BookRecord
import org.jooq.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class BookRepositoryTest {

    @Mock
    private lateinit var dsl: DSLContext

    @InjectMocks
    private lateinit var bookRepository: BookRepository

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `findById should return BookRecord when found`() {
        val bookId = 1
        val bookRecord = BookRecord().apply {
            this.id = bookId
        }

        val selectFrom = Mockito.mock(SelectFromStep::class.java) as SelectFromStep<BookRecord>
        val selectConditionStep = Mockito.mock(SelectConditionStep::class.java) as SelectConditionStep<BookRecord>

        Mockito.`when`(dsl.selectFrom(BOOK)).thenReturn(selectFrom)
        Mockito.`when`(selectFrom.where(BOOK.ID.eq(bookId))).thenReturn(selectConditionStep)
        Mockito.`when`(selectConditionStep.fetchOne()).thenReturn(bookRecord)

        val result = bookRepository.findById(bookId)
        assertEquals(bookRecord, result)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `findById should return null when not found`() {
        val bookId = 1

        val selectFrom = Mockito.mock(SelectFromStep::class.java) as SelectFromStep<BookRecord>
        val selectConditionStep = Mockito.mock(SelectConditionStep::class.java) as SelectConditionStep<BookRecord>

        Mockito.`when`(dsl.selectFrom(BOOK)).thenReturn(selectFrom)
        Mockito.`when`(selectFrom.where(BOOK.ID.eq(bookId))).thenReturn(selectConditionStep)
        Mockito.`when`(selectConditionStep.fetchOne()).thenReturn(null)

        val result = bookRepository.findById(bookId)
        assertNull(result)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `findByAuthorId should return list of BookRecords`() {
        val authorId = 1
        val bookRecord1 = BookRecord().apply { this.id = 1 }
        val bookRecord2 = BookRecord().apply { this.id = 2 }

        val selectFrom = Mockito.mock(SelectFromStep::class.java) as SelectFromStep<BookRecord>
        val selectConditionStep = Mockito.mock(SelectConditionStep::class.java) as SelectConditionStep<BookRecord>
        val resultMock = Mockito.mock(Result::class.java) as Result<BookRecord>

        val bookRecords = mutableListOf(bookRecord1, bookRecord2)
        Mockito.`when`(resultMock.iterator()).thenReturn(bookRecords.iterator())

        Mockito.`when`(dsl.selectFrom(BOOK)).thenReturn(selectFrom)
        Mockito.`when`(selectFrom.where(BOOK.AUTHOR_ID.eq(authorId))).thenReturn(selectConditionStep)
        Mockito.`when`(selectConditionStep.fetch()).thenReturn(resultMock)

        val result = bookRepository.findByAuthorId(authorId)
        assertEquals(bookRecords, result)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `save should return number of affected rows`() {
        val bookRecord = BookRecord()

        val insert = Mockito.mock(InsertSetStep::class.java) as InsertSetStep<BookRecord>
        val insertMore = Mockito.mock(InsertSetMoreStep::class.java) as InsertSetMoreStep<BookRecord>

        Mockito.`when`(dsl.insertInto(BOOK)).thenReturn(insert)
        Mockito.`when`(insert.set(bookRecord)).thenReturn(insertMore)
        Mockito.`when`(insertMore.execute()).thenReturn(1)

        val result = bookRepository.save(bookRecord)
        assertEquals(1, result)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `update should return number of affected rows`() {
        val bookRecord = BookRecord().apply {
            this.id = 1
        }

        val update = Mockito.mock(UpdateSetFirstStep::class.java) as UpdateSetFirstStep<BookRecord>
        val updateSet = Mockito.mock(UpdateSetMoreStep::class.java) as UpdateSetMoreStep<BookRecord>
        val updateWhere = Mockito.mock(UpdateConditionStep::class.java) as UpdateConditionStep<BookRecord>

        Mockito.`when`(dsl.update(BOOK)).thenReturn(update)
        Mockito.`when`(update.set(bookRecord)).thenReturn(updateSet)
        Mockito.`when`(updateSet.where(BOOK.ID.eq(bookRecord.id))).thenReturn(updateWhere)
        Mockito.`when`(updateWhere.execute()).thenReturn(1)

        val result = bookRepository.update(bookRecord)
        assertEquals(1, result)
    }
}
