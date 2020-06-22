package com.whammy.article.usecase

import com.whammy.article.domain.*
import com.whammy.article.exception.ArticleNotFoundException
import com.whammy.article.exception.InvalidRequestException
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class ArticleUsecaseTest {

    private lateinit var usecase: ArticleUsecase

    private lateinit var repository: IArticleRepository

    @BeforeEach
    fun setup() {
        repository = mockk()
        usecase = ArticleUsecase(repository)
    }

    @AfterEach
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun testGetArticles() {
        val articles = Articles(
            listOf(
                Article(
                    "title-1",
                    "title1",
                    "body1",
                    "taro@example.com",
                    LocalDateTime.of(2019, 1, 1, 0, 0),
                    Comments(emptyList()),
                    emptyList()
                ),
                Article(
                    "title-2",
                    "title2",
                    "body2",
                    "taro@example.com",
                    LocalDateTime.of(2020, 1, 1, 0, 0),
                    Comments(emptyList()),
                    emptyList()
                )
            )
        )
        val expected = Articles(
            listOf(
                Article(
                    "title-2",
                    "title2",
                    "body2",
                    "taro@example.com",
                    LocalDateTime.of(2020, 1, 1, 0, 0),
                    Comments(emptyList()),
                    emptyList()
                ),
                Article(
                    "title-1",
                    "title1",
                    "body1",
                    "taro@example.com",
                    LocalDateTime.of(2019, 1, 1, 0, 0),
                    Comments(emptyList()),
                    emptyList()
                )
            )
        )

        every { repository.getArticles() } returns articles

        assertEquals(expected, usecase.getArticlesOrderedByUpdatedDateTime())

        verify { repository.getArticles() }
    }

    @Test
    fun testGetArticle() {
        val article = Article(
            "title-1",
            "title1",
            "body",
            "taro@example.com",
            LocalDateTime.of(2020, 1, 1, 0, 0),
            Comments(emptyList()),
            emptyList()
        )

        every { repository.getArticle("title-1") } returns article

        assertEquals(article, usecase.getArticle("title-1"))

        verify { repository.getArticle("title-1") }
    }

    @Test
    fun testFailedToGetArticle() {
        every { repository.getArticle("no-article") } throws ArticleNotFoundException("")

        assertThrows<ArticleNotFoundException> { usecase.getArticle("no-article") }

        verify { repository.getArticle("no-article") }
    }

    @Test
    fun testGetCommentsOfArticle() {
        val comments = mockk<Comments>()

        every { repository.getCommentsOfArticle("slug") } returns comments
        assertEquals(comments, usecase.getCommentsOfArticle("slug"))
        verify { repository.getCommentsOfArticle("slug") }
    }

    @Test
    fun testAddCommentToArticle() {
        val email = "taro@example.com"
        val slug = "slug-1"
        val body = "body"
        val newComment = mockk<Comment>()
        val comments = mockk<Comments>()
        val newComments = mockk<Comments>()

        every { repository.getCommentsOfArticle(slug) } returns comments
        every { comments.add(email, body) } returns newComments
        every { repository.saveComments(slug, newComments) } returns newComments
        every { newComments.getLatestComment() } returns newComment

        assertEquals(newComment, usecase.addComment(email, slug, body))

        verify {
            repository.getCommentsOfArticle(slug)
            comments.add(email, body)
            newComments.getLatestComment()
        }
    }

    @Test
    fun testFailedToAddCommentToArticle() {
        val email = "taro@example.com"
        val slug = "no-article-slug-1"
        val body = "body"

        every { repository.getCommentsOfArticle(slug) } throws ArticleNotFoundException("")

        assertThrows<ArticleNotFoundException> { usecase.addComment(email, slug, body) }

        verify { repository.getCommentsOfArticle(slug) }
    }

    @Test
    internal fun testToggleFavorite() {
        val slug = "slug"
        val user = "user@example.com"
        val article = mockk<Article>()
        val likedArticle = Article(slug, "title", "body", "taro@example.com", mockk(), Comments(emptyList()), listOf(Favorite(user)))
        val storedArticle = mockk<Article>()

        every { repository.getArticle(slug) } returns article
        every { article.toggleFavoriteFrom(user) } returns likedArticle
        every { repository.saveArticle(likedArticle) } returns storedArticle

        assertEquals(storedArticle, usecase.toggleFavorite(slug, user))

        verify {
            repository.getArticle(slug)
            article.toggleFavoriteFrom(user)
        }
    }

    @Test
    internal fun testFailedToGetArticleWhenTogglingFavorite() {
        every { repository.getArticle("no-article") } throws ArticleNotFoundException("")

        assertThrows<ArticleNotFoundException> { usecase.toggleFavorite("no-article", "no-one@example.com") }

        verify { repository.getArticle("no-article") }
    }

    @Test
    internal fun testCreateNewArticle() {
        mockkObject(Article)

        val user = "user@example.com"
        val slug = "title-1"
        val title = "title 1"
        val body = "body"
        val savedArticle = mockk<Article>()
        val article = mockk<Article>()

        every { Article.of(user, title, body) } returns article
        every { article.slug } returns slug
        every { repository.articleExists(slug) } returns false
        every { repository.saveArticle(article) } returns savedArticle

        assertEquals(savedArticle, usecase.createNewArticle(user, title, body))

        verify {
            Article.of(user, title, body)
            article.slug
            repository.saveArticle(article)
            repository.articleExists(slug)
        }
    }

    @Test
    internal fun testCreateNewArticleWhenSlugAlreadyExists() {
        mockkObject(Article)

        val user = "user@example.com"
        val slug = "title-1"
        val title = "title 1"
        val body = "body"
        val savedArticle = mockk<Article>()
        val article = mockk<Article>()
        val newArticle = mockk<Article>()

        every { Article.of(user, title, body) } returns article
        every { article.slug } returns slug
        every { repository.articleExists(slug) } returns true
        every { article.assignNewSlug() } returns newArticle
        every { repository.saveArticle(newArticle) } returns savedArticle

        assertEquals(savedArticle, usecase.createNewArticle(user, title, body))

        verify {
            Article.of(user, title, body)
            article.slug
            repository.articleExists(slug)
            article.assignNewSlug()
            repository.saveArticle(newArticle)
        }
    }

    @Test
    internal fun testUpdateArticle() {
        val slug = "slug"
        val user = "user@example.com"
        val title = "new title 1"
        val body = "body"

        val article = mockk<Article>()
        val updatedArticle = mockk<Article>()
        val savedArticle = mockk<Article>()

        every { repository.getArticle(slug) } returns article
        every { article.update(title, body) } returns updatedArticle
        every { repository.updateArticle(slug, updatedArticle) } returns savedArticle

        assertEquals(savedArticle, usecase.updateArticle(slug, user, title, body))

        verify {
            repository.getArticle(slug)
            article.update(title,body)
            repository.updateArticle(slug,updatedArticle)
        }
    }

    @Test
    internal fun testDeleteWhenTheRequestingUserCoincidesWithArticleAuthor() {
        val slug = "slug"
        val userId = "userId"
        val article = mockk<Article>()
        every { repository.getArticle(slug) } returns article
        every { article.isCreatedBy(userId) } returns true
        every { repository.deleteArticle(slug) } just runs

        usecase.delete(slug, userId)

        verify {
            repository.getArticle(slug)
            article.isCreatedBy(userId)
            repository.deleteArticle(slug)
        }
    }

    @Test
    internal fun testNotDeletWhenTheRequestingUserCoincidesWithArticleAuthor() {
        val slug = "slug"
        val userId = "userId"
        val article = mockk<Article>()
        every { repository.getArticle(slug) } returns article
        every { article.isCreatedBy(userId) } returns false

        assertThrows<InvalidRequestException> { usecase.delete(slug, userId) }

        verify {
            repository.getArticle(slug)
            article.isCreatedBy(userId)
        }
    }
}