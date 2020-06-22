package com.whammy.article.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ArticleTest {

    @AfterEach
    fun teardown () {
        unmockkAll()
    }

    @Test
    internal fun testToggleLikeFrom() {
        val updatedAt = mockk<LocalDateTime>()
        val article = Article("slug", "title", "body", "user@example.com", updatedAt, Comments(emptyList()), emptyList())
        val expected = Article(
            "slug",
            "title",
            "body",
            "user@example.com",
            updatedAt,
            Comments(emptyList()),
            listOf(Favorite("user@example.com"))
        )
        assertEquals(expected, article.toggleFavoriteFrom("user@example.com"))
    }

    @Test
    internal fun testToggleLikeFromWhenAlreadyLikedByTheUser() {
        val updatedAt = mockk<LocalDateTime>()
        val article = Article(
            "slug",
            "title",
            "body",
            "user@example.com",
            updatedAt,
            Comments(emptyList()),
            listOf(Favorite("user@example.com"))
        )
        val expected = Article("slug", "title", "body", "user@example.com", updatedAt, Comments(emptyList()), emptyList())
        assertEquals(expected, article.toggleFavoriteFrom("user@example.com"))
    }

    @Test
    internal fun testArticleOf() {
        mockkStatic(LocalDateTime::class)
        val createdDateTime = LocalDateTime.of(2020, 1, 1, 1, 0)

        every { LocalDateTime.now() } returns createdDateTime
        val expected = Article("title-1", "title 1", "body", "user@example.com", createdDateTime, Comments(emptyList()), emptyList())

        assertEquals(expected, Article.of("user@example.com", "title 1", "body"))
    }

    @Test
    internal fun testUpdateBothTitleAndBody() {
        val createdDateTime = LocalDateTime.of(2020, 1, 1, 1, 0)
        val updatedTime = mockk<LocalDateTime>()
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now() } returns updatedTime
        val authorEmailAddress = "user@example.com"
        val article = Article("title-1", "title 1", "body",
            authorEmailAddress, createdDateTime, Comments(emptyList()), emptyList())
        val expected = Article("new-title", "new title", "new body",
            authorEmailAddress, createdDateTime, Comments(emptyList()), emptyList(), updatedTime)

        assertEquals(expected, article.update("new title", "new body"))
    }

    @Test
    internal fun testUpdateOnlyTitle() {
        val createdDateTime = LocalDateTime.of(2020, 1, 1, 1, 0)
        val updatedTime = mockk<LocalDateTime>()
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now() } returns updatedTime

        val authorEmailAddress = "user@example.com"
        val article = Article("title-1", "title 1", "body",
            authorEmailAddress, createdDateTime, Comments(emptyList()), emptyList())
        val expected = Article("new-title", "new title", "body",
            authorEmailAddress, createdDateTime, Comments(emptyList()), emptyList(), updatedTime)

        assertEquals(expected, article.update("new title", null))
    }

    @Test
    internal fun testUpdateOnlyBody() {
        val createdDateTime = LocalDateTime.of(2020, 1, 1, 1, 0)
        val updatedTime = mockk<LocalDateTime>()
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now() } returns updatedTime

        val authorEmailAddress = "user@example.com"
        val article = Article("title-1", "title 1", "body",
            authorEmailAddress, createdDateTime, Comments(emptyList()), emptyList())
        val expected = Article("title-1", "title 1", "new body",
            authorEmailAddress, createdDateTime, Comments(emptyList()), emptyList(), updatedTime)

        assertEquals(expected, article.update(null, "new body"))
    }

    @Test
    internal fun testUpdateNothing() {
        val createdDateTime = LocalDateTime.of(2020, 1, 1, 1, 0)
        val authorEmailAddress = "user@example.com"
        val article = Article("title-1", "title 1", "body",
            authorEmailAddress, createdDateTime, Comments(emptyList()), emptyList())
        val expected = Article("title-1", "title 1", "body",
            authorEmailAddress, createdDateTime, Comments(emptyList()), emptyList())

        assertEquals(expected, article.update(null, null))
    }

    @Test
    internal fun testIsCreatedByReturnsTrueWhenTheAuthorAndUserIdAreTheSame() {
        val article = Article(
            "title-1", "title 1", "body",
            "authorEmailAddress", LocalDateTime.now(), Comments(emptyList()), emptyList()
        )
        assertTrue { article.isCreatedBy("authorEmailAddress") }
    }

    @Test
    internal fun testIsCreatedByReturnsFalseWhenTheAuthorAndUserIdAreNotTheSame() {
        val article = Article(
            "title-1", "title 1", "body",
            "authorEmailAddress", LocalDateTime.now(), Comments(emptyList()), emptyList()
        )
        assertFalse { article.isCreatedBy("differentEmailAddress") }
    }
}