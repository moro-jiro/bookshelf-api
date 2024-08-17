package com.example.bookshelf_api.infrastructure.jooq.repository

import com.example.bookshelf_api.application.dto.AuthorSearchDto
import com.example.bookshelf_api.domain.model.Author
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.records.AuthorRecord
import org.jooq.*
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.Author as AuthorTable
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class JooqAuthorRepositoryTest {

    @Mock
    private lateinit var dsl: DSLContext

    @InjectMocks
    private lateinit var repository: JooqAuthorRepository

    private lateinit var sampleAuthor: Author

    @BeforeEach
    fun setUp() {
        sampleAuthor = Author(
            id = 1,
            firstName = "太郎",
            lastName = "山田",
            birthDate = LocalDate.of(1980, 1, 1),
            gender = "MALE"
        )
    }

    @Test
    fun `should create author successfully`() {
        val insertStep = mock(InsertSetStep::class.java) as InsertSetStep<AuthorRecord>
        val insertMoreStep = mock(InsertSetMoreStep::class.java) as InsertSetMoreStep<AuthorRecord>
        val resultStep = mock(InsertResultStep::class.java) as InsertResultStep<AuthorRecord>
        val record = mock(AuthorRecord::class.java) as AuthorRecord

        `when`(dsl.insertInto(AuthorTable.AUTHOR))
            .thenReturn(insertStep)
        `when`(insertStep.set(AuthorTable.AUTHOR.FIRST_NAME, "太郎"))
            .thenReturn(insertMoreStep)
        `when`(insertMoreStep.set(AuthorTable.AUTHOR.LAST_NAME, "山田"))
            .thenReturn(insertMoreStep)
        `when`(insertMoreStep.set(AuthorTable.AUTHOR.BIRTH_DATE, LocalDate.of(1980, 1, 1)))
            .thenReturn(insertMoreStep)
        `when`(insertMoreStep.set(AuthorTable.AUTHOR.GENDER, "MALE"))
            .thenReturn(insertMoreStep)
        `when`(insertMoreStep.returning(AuthorTable.AUTHOR.ID))
            .thenReturn(resultStep)
        `when`(resultStep.fetchOne())
            .thenReturn(record)
        `when`(record.getValue(AuthorTable.AUTHOR.ID))
            .thenReturn(1)

        val author = Author(
            firstName = "太郎",
            lastName = "山田",
            birthDate = LocalDate.of(1980, 1, 1),
            gender = "MALE"
        )
        val result = repository.createAuthor(author)
        assertEquals(1, result)
    }


    @Test
    fun `should find author by details`() {
        val selectFromStep = mock(SelectFromStep::class.java) as SelectFromStep<AuthorRecord>
        val conditionStep = mock(SelectConditionStep::class.java) as SelectConditionStep<AuthorRecord>

        `when`(dsl.selectFrom(AuthorTable.AUTHOR))
            .thenReturn(selectFromStep)
        `when`(selectFromStep.where(any<Condition>()))
            .thenReturn(conditionStep)

        `when`(conditionStep.and(any<Condition>()))
            .thenReturn(conditionStep)

        `when`(conditionStep.fetchOneInto(Author::class.java))
            .thenReturn(sampleAuthor)

        val result = repository.findAuthorByDetails("太郎", "山田", LocalDate.of(1980, 1, 1))
        assertNotNull(result)
        assertEquals(sampleAuthor, result)

        verify(selectFromStep).where(any<Condition>())
        verify(conditionStep, times(2)).and(any<Condition>())
    }

    @Test
    fun `should update author successfully`() {
        val updateSetFirstStep = mock(UpdateSetFirstStep::class.java) as UpdateSetFirstStep<AuthorRecord>
        val updateSetMoreStep = mock(UpdateSetMoreStep::class.java) as UpdateSetMoreStep<AuthorRecord>
        val updateConditionStep = mock(UpdateConditionStep::class.java) as UpdateConditionStep<AuthorRecord>

        `when`(dsl.update(AuthorTable.AUTHOR)).thenReturn(updateSetFirstStep)
        `when`(updateSetFirstStep.set(AuthorTable.AUTHOR.FIRST_NAME, "太郎"))
            .thenReturn(updateSetMoreStep)
        `when`(updateSetMoreStep.set(AuthorTable.AUTHOR.LAST_NAME, "山田"))
            .thenReturn(updateSetMoreStep)
        `when`(updateSetMoreStep.set(AuthorTable.AUTHOR.BIRTH_DATE, LocalDate.of(1980, 1, 1)))
            .thenReturn(updateSetMoreStep)
        `when`(updateSetMoreStep.set(AuthorTable.AUTHOR.GENDER, "MALE"))
            .thenReturn(updateSetMoreStep)
        `when`(updateSetMoreStep.set(eq(AuthorTable.AUTHOR.UPDATED_AT), any(LocalDateTime::class.java)))
            .thenReturn(updateSetMoreStep)

        `when`(updateSetMoreStep.where(any(Condition::class.java))).thenReturn(updateConditionStep)

        `when`(updateConditionStep.execute()).thenReturn(1)

        val author = Author(
            id = 1,
            firstName = "太郎",
            lastName = "山田",
            birthDate = LocalDate.of(1980, 1, 1),
            gender = "MALE"
        )

        assertDoesNotThrow {
            repository.updateAuthor(author)
        }

        verify(updateSetMoreStep).where(any(Condition::class.java))
        verify(updateConditionStep).execute()
    }

    @Test
    fun `should search authors`() {
        val selectFromStep = mock(SelectFromStep::class.java) as SelectFromStep<AuthorRecord>
        val selectConditionStep = mock(SelectConditionStep::class.java) as SelectConditionStep<AuthorRecord>
        val selectSeekStep2 = mock(SelectSeekStep2::class.java) as SelectSeekStep2<AuthorRecord, Any, Any>

        lenient().`when`(dsl.selectFrom(AuthorTable.AUTHOR)).thenReturn(selectFromStep)
        lenient().`when`(selectFromStep.where(any<Collection<Condition>>())).thenReturn(selectConditionStep)

        lenient().`when`(selectConditionStep.orderBy(any<OrderField<*>>(), any<OrderField<*>>())).thenReturn(selectSeekStep2)

        lenient().`when`(selectSeekStep2.fetchInto(Author::class.java)).thenReturn(listOf(sampleAuthor))

        val searchDto = AuthorSearchDto(
            lastName = "山田",
            firstName = "太郎",
            birthDate = LocalDate.of(1980, 1, 1),
            gender = "MALE"
        )
        val authors = repository.searchAuthors(searchDto)
        assertEquals(1, authors.size)
        assertEquals(sampleAuthor, authors[0])

        verify(selectFromStep).where(any<Collection<Condition>>())
        verify(selectConditionStep).orderBy(any<OrderField<*>>(), any<OrderField<*>>())
        verify(selectSeekStep2).fetchInto(Author::class.java)
    }
}
