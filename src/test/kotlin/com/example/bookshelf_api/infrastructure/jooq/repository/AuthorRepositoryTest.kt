package com.example.bookshelf_api.infrastructure.jooq.repository

import com.example.bookshelf_api.infrastructure.jooq.generated.tables.Author.AUTHOR
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.records.AuthorRecord
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
class AuthorRepositoryTest {

    @Mock
    private lateinit var dsl: DSLContext

    @InjectMocks
    private lateinit var authorRepository: AuthorRepository

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `findById should return AuthorRecord when found`() {
        val authorId = 1
        val authorRecord = AuthorRecord().apply {
            this.id = authorId
        }

        val selectFrom = Mockito.mock(SelectFromStep::class.java) as SelectFromStep<AuthorRecord>
        val selectConditionStep = Mockito.mock(SelectConditionStep::class.java) as SelectConditionStep<AuthorRecord>

        Mockito.`when`(dsl.selectFrom(AUTHOR)).thenReturn(selectFrom)
        Mockito.`when`(selectFrom.where(AUTHOR.ID.eq(authorId))).thenReturn(selectConditionStep)
        Mockito.`when`(selectConditionStep.fetchOne()).thenReturn(authorRecord)

        val result = authorRepository.findById(authorId)
        assertEquals(authorRecord, result)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `findById should return null when not found`() {
        val authorId = 1

        val selectFrom = Mockito.mock(SelectFromStep::class.java) as SelectFromStep<AuthorRecord>
        val selectConditionStep = Mockito.mock(SelectConditionStep::class.java) as SelectConditionStep<AuthorRecord>

        Mockito.`when`(dsl.selectFrom(AUTHOR)).thenReturn(selectFrom)
        Mockito.`when`(selectFrom.where(AUTHOR.ID.eq(authorId))).thenReturn(selectConditionStep)
        Mockito.`when`(selectConditionStep.fetchOne()).thenReturn(null)

        val result = authorRepository.findById(authorId)
        assertNull(result)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `save should return number of affected rows`() {
        val authorRecord = AuthorRecord()

        val insert = Mockito.mock(InsertSetStep::class.java) as InsertSetStep<AuthorRecord>
        val insertMore = Mockito.mock(InsertSetMoreStep::class.java) as InsertSetMoreStep<AuthorRecord>

        Mockito.`when`(dsl.insertInto(AUTHOR)).thenReturn(insert)
        Mockito.`when`(insert.set(authorRecord)).thenReturn(insertMore)
        Mockito.`when`(insertMore.execute()).thenReturn(1)

        val result = authorRepository.save(authorRecord)
        assertEquals(1, result)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `update should return number of affected rows`() {
        val authorRecord = AuthorRecord().apply {
            this.id = 1
        }

        val update = Mockito.mock(UpdateSetFirstStep::class.java) as UpdateSetFirstStep<AuthorRecord>
        val updateSet = Mockito.mock(UpdateSetMoreStep::class.java) as UpdateSetMoreStep<AuthorRecord>
        val updateWhere = Mockito.mock(UpdateConditionStep::class.java) as UpdateConditionStep<AuthorRecord>

        Mockito.`when`(dsl.update(AUTHOR)).thenReturn(update)
        Mockito.`when`(update.set(authorRecord)).thenReturn(updateSet)
        Mockito.`when`(updateSet.where(AUTHOR.ID.eq(authorRecord.id))).thenReturn(updateWhere)
        Mockito.`when`(updateWhere.execute()).thenReturn(1)

        val result = authorRepository.update(authorRecord)
        assertEquals(1, result)
    }
}
