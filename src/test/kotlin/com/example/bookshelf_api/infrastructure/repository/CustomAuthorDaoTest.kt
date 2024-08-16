package com.example.bookshelf_api.infrastructure.repository

import com.example.bookshelf_api.infrastructure.jooq.generated.tables.pojos.Author
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.Author.AUTHOR
import com.example.bookshelf_api.infrastructure.jooq.generated.tables.records.AuthorRecord
import org.jooq.DSLContext
import org.jooq.SelectConditionStep
import org.jooq.SelectWhereStep
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class CustomAuthorDaoTest {

    @Mock
    private lateinit var dslContext: DSLContext

    @InjectMocks
    private lateinit var customAuthorDao: CustomAuthorDao

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `test fetchByFirstNameAndLastName returns expected result`() {
        val firstName = "Kojiro"
        val lastName = "Morooka"

        val mockAuthor = Author()
        mockAuthor.id = 1
        mockAuthor.firstName = firstName
        mockAuthor.lastName = lastName

        val mockResult = listOf(mockAuthor)

        val selectWhereStep = mock(SelectWhereStep::class.java) as SelectWhereStep<AuthorRecord>
        val selectConditionStep = mock(SelectConditionStep::class.java) as SelectConditionStep<AuthorRecord>

        `when`(dslContext.selectFrom(AUTHOR)).thenReturn(selectWhereStep)
        `when`(selectWhereStep.where(any<org.jooq.Condition>())).thenReturn(selectConditionStep)
        `when`(selectConditionStep.and(any<org.jooq.Condition>())).thenReturn(selectConditionStep)
        `when`(selectConditionStep.fetchInto(Author::class.java)).thenReturn(mockResult)

        val result = customAuthorDao.fetchByFirstNameAndLastName(firstName, lastName)

        assertEquals(1, result.size)
        assertEquals(firstName, result[0].firstName)
        assertEquals(lastName, result[0].lastName)

        verify(dslContext).selectFrom(AUTHOR)
        verify(selectWhereStep).where(AUTHOR.FIRST_NAME.eq(firstName))
        verify(selectConditionStep).and(AUTHOR.LAST_NAME.eq(lastName))
        verify(selectConditionStep).fetchInto(Author::class.java)
    }
}

