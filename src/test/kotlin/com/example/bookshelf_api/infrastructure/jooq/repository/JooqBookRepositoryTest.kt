package com.example.bookshelf_api.infrastructure.jooq.repository

import com.example.bookshelf_api.domain.model.Author
import com.example.bookshelf_api.domain.model.Book
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.records.BookRecord
import org.jooq.*
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.Book as BookTable
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
class JooqBookRepositoryTest {

    @Mock
    private lateinit var dsl: DSLContext

    @InjectMocks
    private lateinit var repository: JooqBookRepository

    private lateinit var sampleBook: Book
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
        sampleBook = Book(
            id = 1,
            title = "サンプルブック",
            authorId = sampleAuthor.id!!,
            publicationDate = LocalDate.of(2023, 8, 1),
            publisher = "サンプル出版社",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `should create book successfully`() {
        val insertStep = mock(InsertSetStep::class.java) as InsertSetStep<BookRecord>
        val insertMoreStep = mock(InsertSetMoreStep::class.java) as InsertSetMoreStep<BookRecord>
        val resultStep = mock(InsertResultStep::class.java) as InsertResultStep<BookRecord>
        val record = mock(BookRecord::class.java) as BookRecord

        val bookRecordMap: Map<Field<*>, Any?> = mapOf(
            BookTable.BOOK.TITLE to sampleBook.title,
            BookTable.BOOK.AUTHOR_ID to sampleBook.authorId,
            BookTable.BOOK.PUBLICATION_DATE to sampleBook.publicationDate,
            BookTable.BOOK.PUBLISHER to sampleBook.publisher
        )

        `when`(dsl.insertInto(BookTable.BOOK)).thenReturn(insertStep)
        `when`(insertStep.set(bookRecordMap)).thenReturn(insertMoreStep)
        `when`(insertMoreStep.set(eq(BookTable.BOOK.CREATED_AT), any(LocalDateTime::class.java))).thenReturn(insertMoreStep)
        `when`(insertMoreStep.set(eq(BookTable.BOOK.UPDATED_AT), any(LocalDateTime::class.java))).thenReturn(insertMoreStep)
        `when`(insertMoreStep.returning()).thenReturn(resultStep)
        `when`(resultStep.fetchOne()).thenReturn(record)

        `when`(record.get(BookTable.BOOK.ID)).thenReturn(sampleBook.id)
        `when`(record.get(BookTable.BOOK.TITLE)).thenReturn(sampleBook.title)
        `when`(record.get(BookTable.BOOK.AUTHOR_ID)).thenReturn(sampleBook.authorId)
        `when`(record.get(BookTable.BOOK.PUBLICATION_DATE)).thenReturn(sampleBook.publicationDate)
        `when`(record.get(BookTable.BOOK.PUBLISHER)).thenReturn(sampleBook.publisher)
        `when`(record.get(BookTable.BOOK.CREATED_AT)).thenReturn(sampleBook.createdAt)
        `when`(record.get(BookTable.BOOK.UPDATED_AT)).thenReturn(sampleBook.updatedAt)

        val result = repository.createBook(sampleBook)
        assertNotNull(result)
        assertEquals(sampleBook, result)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `should find book by ID`() {
        val selectStep = mock(SelectSelectStep::class.java) as SelectSelectStep<Record>
        val joinStep = mock(SelectJoinStep::class.java) as SelectJoinStep<Record>
        val onStep = mock(SelectOnStep::class.java) as SelectOnStep<Record>
        val onConditionStep = mock(SelectOnConditionStep::class.java) as SelectOnConditionStep<Record>
        val conditionStep = mock(SelectConditionStep::class.java) as SelectConditionStep<Record>

        lenient().`when`(dsl.select()).thenReturn(selectStep)
        lenient().`when`(selectStep.from(BookTable.BOOK)).thenReturn(joinStep)
        lenient().`when`(joinStep.join(AuthorTable.AUTHOR)).thenReturn(onStep)
        lenient().`when`(onStep.on(BookTable.BOOK.AUTHOR_ID.eq(AuthorTable.AUTHOR.ID))).thenReturn(onConditionStep)
        lenient().`when`(onConditionStep.where(BookTable.BOOK.ID.eq(1))).thenReturn(conditionStep)

        lenient().`when`(conditionStep.fetchOne(any<RecordMapper<Record, Pair<Book, Author>>>())).thenReturn(Pair(sampleBook, sampleAuthor))

        val result = repository.findBookById(1)
        assertNotNull(result)
        assertEquals(sampleBook, result?.first)
        assertEquals(sampleAuthor, result?.second)

        verify(conditionStep).fetchOne(any<RecordMapper<Record, Pair<Book, Author>>>())
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `should find books by title`() {
        val records = mock(Result::class.java) as Result<BookRecord>

        val selectConditionStep = mock(SelectConditionStep::class.java) as SelectConditionStep<BookRecord>
        val selectWhereStep = mock(SelectWhereStep::class.java) as SelectWhereStep<BookRecord>

        `when`(dsl.selectFrom(BookTable.BOOK)).thenReturn(selectWhereStep)
        `when`(selectWhereStep.where(BookTable.BOOK.TITLE.likeIgnoreCase("%サンプル%")))
            .thenReturn(selectConditionStep)
        `when`(selectConditionStep.fetch()).thenReturn(records)

        doReturn(listOf(sampleBook)).`when`(records).map(any<(BookRecord) -> Book>())

        val result = repository.findBooksByTitle("サンプル")

        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(sampleBook, result[0])
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `should update book successfully`() {
        val updateStep = mock(UpdateSetFirstStep::class.java) as UpdateSetFirstStep<BookRecord>
        val updateMoreStep = mock(UpdateSetMoreStep::class.java) as UpdateSetMoreStep<BookRecord>
        val updateConditionStep = mock(UpdateConditionStep::class.java) as UpdateConditionStep<BookRecord>
        val updateResultStep = mock(UpdateResultStep::class.java) as UpdateResultStep<BookRecord>
        val record = mock(BookRecord::class.java)

        `when`(dsl.update(BookTable.BOOK)).thenReturn(updateStep)
        `when`(updateStep.set(any<Map<Field<*>, Any?>>())).thenReturn(updateMoreStep)
        `when`(updateMoreStep.set(eq(BookTable.BOOK.UPDATED_AT), any(LocalDateTime::class.java))).thenReturn(updateMoreStep)
        `when`(updateMoreStep.where(eq(BookTable.BOOK.ID.eq(sampleBook.id!!)))).thenReturn(updateConditionStep)
        `when`(updateConditionStep.returning()).thenReturn(updateResultStep)
        `when`(updateResultStep.fetchOne()).thenReturn(record)

        `when`(record.get(BookTable.BOOK.ID)).thenReturn(sampleBook.id)
        `when`(record.get(BookTable.BOOK.TITLE)).thenReturn(sampleBook.title)
        `when`(record.get(BookTable.BOOK.AUTHOR_ID)).thenReturn(sampleBook.authorId)
        `when`(record.get(BookTable.BOOK.PUBLICATION_DATE)).thenReturn(sampleBook.publicationDate)
        `when`(record.get(BookTable.BOOK.PUBLISHER)).thenReturn(sampleBook.publisher)
        `when`(record.get(BookTable.BOOK.CREATED_AT)).thenReturn(sampleBook.createdAt)
        `when`(record.get(BookTable.BOOK.UPDATED_AT)).thenReturn(sampleBook.updatedAt)

        val result = repository.updateBook(sampleBook)
        assertNotNull(result)
        assertEquals(sampleBook, result)

        verify(dsl).update(BookTable.BOOK)
        verify(updateStep).set(any<Map<Field<*>, Any?>>())
        verify(updateMoreStep).set(eq(BookTable.BOOK.UPDATED_AT), any(LocalDateTime::class.java))
        verify(updateMoreStep).where(eq(BookTable.BOOK.ID.eq(sampleBook.id!!)))
        verify(updateConditionStep).returning()
        verify(updateResultStep).fetchOne()
    }

    @Test
    fun `should find books by author ID`() {
        val selectWhereStep: SelectWhereStep<BookRecord> = mock()
        val selectConditionStep: SelectConditionStep<BookRecord> = mock()
        val record: BookRecord = mock()

        `when`(dsl.selectFrom(BookTable.BOOK)).thenReturn(selectWhereStep)
        `when`(selectWhereStep.where(BookTable.BOOK.AUTHOR_ID.eq(sampleAuthor.id!!))).thenReturn(selectConditionStep)

        val mockResult: Result<BookRecord> = mock()
        `when`(selectConditionStep.fetch()).thenReturn(mockResult)

        `when`(record.get(BookTable.BOOK.ID)).thenReturn(sampleBook.id)
        `when`(record.get(BookTable.BOOK.TITLE)).thenReturn(sampleBook.title)
        `when`(record.get(BookTable.BOOK.AUTHOR_ID)).thenReturn(sampleBook.authorId)
        `when`(record.get(BookTable.BOOK.PUBLICATION_DATE)).thenReturn(sampleBook.publicationDate)
        `when`(record.get(BookTable.BOOK.PUBLISHER)).thenReturn(sampleBook.publisher)
        `when`(record.get(BookTable.BOOK.CREATED_AT)).thenReturn(sampleBook.createdAt)
        `when`(record.get(BookTable.BOOK.UPDATED_AT)).thenReturn(sampleBook.updatedAt)

        `when`(mockResult.map(any<RecordMapper<BookRecord, Book>>())).thenAnswer { invocation ->
            val mapper = invocation.getArgument<RecordMapper<BookRecord, Book>>(0)
            listOf(mapper.map(record))
        }

        val result = repository.findBooksByAuthorId(sampleAuthor.id!!)

        println("Test Result: $result")

        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(sampleBook, result[0])

        verify(dsl).selectFrom(BookTable.BOOK)
        verify(selectWhereStep).where(BookTable.BOOK.AUTHOR_ID.eq(sampleAuthor.id!!))
        verify(selectConditionStep).fetch()
        verify(mockResult).map(any<RecordMapper<BookRecord, Book>>())
    }
}
