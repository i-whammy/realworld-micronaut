package com.whammy.article.domain

import com.whammy.article.exception.CommentNotFoundException
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class CommentsTest {

    @Test
    internal fun testAddCommentWithIncrementalIdAndAddedTime() {
        val createdAt = mockk<LocalDateTime>()
        mockkStatic(LocalDateTime::class)
        val email = "taro@example.com"
        val comments = Comments(listOf(
            Comment(1, "body", "address@example.com", LocalDateTime.of(2020,1,1,12,0)),
            Comment(2, "body", "address2@example.com", LocalDateTime.of(2020,1,1,12,0))
        ))
        val newComments = Comments(listOf(
            Comment(1, "body","address@example.com", LocalDateTime.of(2020,1,1,12,0)),
            Comment(2, "body","address2@example.com", LocalDateTime.of(2020,1,1,12,0)),
            Comment(3, "New comment body", email, createdAt)
        ))
        every { LocalDateTime.now() } returns createdAt

        assertEquals(newComments, comments.add(email, "New comment body"))

        verify { LocalDateTime.now() }
    }

    @Test
    internal fun testAddCommentAsFirstComment() {
        val createdAt = mockk<LocalDateTime>()
        mockkStatic(LocalDateTime::class)
        val comments = Comments(emptyList())
        val email = "taro@example.com"
        val newComments = Comments(listOf(Comment(1, "New comment body", email, createdAt)))
        every { LocalDateTime.now() } returns createdAt

        assertEquals(newComments, comments.add(email, "New comment body"))

        verify { LocalDateTime.now() }
    }

    @Test
    internal fun testGetLatestComment() {
        val latestComment = Comment(3, "body","example@example.com", mockk())
        val comments = Comments(
            listOf(
                Comment(1, "body", "example@example.com", mockk()),
                Comment(2, "body","example@example.com", mockk()),
                latestComment
            )
        )

        assertEquals(latestComment, comments.getLatestComment())
    }

    @Test
    internal fun testFailedToGetLatestCommentWhenTheArticleHasNoComment() {
        assertThrows<CommentNotFoundException> { Comments(emptyList()).getLatestComment() }
    }
}