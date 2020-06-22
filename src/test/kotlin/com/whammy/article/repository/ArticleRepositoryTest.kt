package com.whammy.article.repository

import com.whammy.article.domain.*
import com.whammy.article.exception.ArticleNotFoundException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class ArticleRepositoryTest {

    private lateinit var driver: ArticleDriver

    private lateinit var repository: ArticleRepository

    @BeforeEach
    fun setup() {
        driver = mockk()
        repository = ArticleRepository(driver)
    }

    @Test
    fun testGetArticles() {
        val articles = Articles(
            listOf(
                Article(
                    "title-1",
                    "title1",
                    "body",
                    "taro@example.com",
                    LocalDateTime.of(2020, 1, 1, 0, 0),
                    Comments(emptyList()),
                    emptyList()
                )
            )
        )

        val articleModels = listOf(
            ArticleModel(
                "title-1",
                "title1",
                "body",
                "taro@example.com",
                LocalDateTime.of(2020, 1, 1, 0, 0),
                emptyList(),
                emptyList()
            )
        )

        every { driver.getArticles() } returns articleModels

        assertEquals(articles, repository.getArticles())
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

        val articleModel = ArticleModel(
            "title-1",
            "title1",
            "body",
            "taro@example.com",
            LocalDateTime.of(2020, 1, 1, 0, 0),
            emptyList(),
            emptyList()
        )

        every { driver.getArticle("title-1") } returns articleModel

        assertEquals(article, repository.getArticle("title-1"))

        verify { driver.getArticle("title-1") }
    }

    @Test
    fun testFailedToGetArticle() {
        every { driver.getArticle("no-article") } returns null

        assertThrows<ArticleNotFoundException> { repository.getArticle("no-article") }

        verify { driver.getArticle("no-article") }
    }

    @Test
    fun testGetCommentsOfArticle() {
        val models = listOf(CommentModel(1, "body1", "taro@example.com",LocalDateTime.of(2020,1,1,10,0)))
        val comments = Comments(listOf(Comment(1, "body1", "taro@example.com", LocalDateTime.of(2020,1,1,10,0))))

        every { driver.getArticle("slug1") } returns mockk()
        every { driver.getCommentsOfArticle("slug1") } returns models

        assertEquals(comments, repository.getCommentsOfArticle("slug1"))

        verify {
            driver.getArticle("slug1")
            driver.getCommentsOfArticle("slug1") }
    }

    @Test
    fun testGetEmptyCommentsOfArticle() {
        val models = emptyList<CommentModel>()

        every { driver.getArticle("slug1") } returns mockk()
        every { driver.getCommentsOfArticle("slug1") } returns models

        assertEquals(Comments(emptyList()), repository.getCommentsOfArticle("slug1"))

        verify {
            driver.getArticle("slug1")
            driver.getCommentsOfArticle("slug1") }
    }

    @Test
    fun testGetCommentsFailedWhenNoArticleExists() {
        every { driver.getArticle("no-article") } returns null

        assertThrows<ArticleNotFoundException> { repository.getCommentsOfArticle("no-article") }

        verify { driver.getArticle("no-article") }
    }

    @Test
    internal fun testSaveComments() {
        val slug = "slug"
        val models = listOf(CommentModel(1, "body1", "taro@example.com", LocalDateTime.of(2020,1,1,10,0)))
        val comments = Comments(listOf(Comment(1, "body1","taro@example.com", LocalDateTime.of(2020,1,1,10,0))))

        every { driver.saveComments(slug, models) } returns models

        assertEquals(comments, repository.saveComments(slug, comments))

        verify { driver.saveComments(slug, models) }
    }

    @Test
    internal fun testSaveArticle() {
        val article = Article(
            "slug",
            "title",
            "body",
            "taro@example.com",
            LocalDateTime.of(2020,1,1,12,0),
            Comments(listOf(Comment(1,"comment body", "user@example.com", LocalDateTime.of(2020,2,1,12,0)))),
            listOf(Favorite("favorite@example.com"))
        )
        val model = ArticleModel(
            "slug",
            "title",
            "body",
            "taro@example.com",
            LocalDateTime.of(2020,1,1,12,0),
            listOf(CommentModel(1,"comment body", "user@example.com", LocalDateTime.of(2020,2,1,12,0))),
            listOf(FavoriteModel("favorite@example.com"))
        )
        every { driver.saveArticle(model) } returns model
        assertEquals(article, repository.saveArticle(article))
        verify { driver.saveArticle(model) }
    }

    @Test
    internal fun testUpdateArticle() {
        val article = Article(
            "slug",
            "title",
            "body",
            "taro@example.com",
            LocalDateTime.of(2020,1,1,12,0),
            Comments(listOf(Comment(1,"comment body", "user@example.com", LocalDateTime.of(2020,2,1,12,0)))),
            listOf(Favorite("favorite@example.com"))
        )
        val model = ArticleModel(
            "slug",
            "title",
            "body",
            "taro@example.com",
            LocalDateTime.of(2020,1,1,12,0),
            listOf(CommentModel(1,"comment body", "user@example.com", LocalDateTime.of(2020,2,1,12,0))),
            listOf(FavoriteModel("favorite@example.com"))
        )
        every { driver.updateArticle("old-slug", model) } returns model
        assertEquals(article, repository.updateArticle("old-slug", article))
        verify { driver.updateArticle("old-slug", model) }
    }
}