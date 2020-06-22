package com.whammy.article.driver

import com.whammy.article.repository.ArticleDriver
import com.whammy.article.repository.ArticleModel
import com.whammy.article.repository.CommentModel
import com.whammy.article.repository.FavoriteModel
import java.time.LocalDateTime
import javax.inject.Singleton

@Singleton
class InMemoryArticleDriver: ArticleDriver {
    private val articles = mutableListOf(
        ArticleModel(
            "Title-1",
            "Title 1",
            "This is a sample article.",
            "taro@example.com",
            LocalDateTime.of(2020, 1, 1, 12, 0),
            listOf(
                CommentModel(1, "This is my first comment!", "taro@example.com", LocalDateTime.of(2020,1,15, 12,0)),
                CommentModel(2, "This is the second comment!", "jiro@example.com", LocalDateTime.of(2020,1,15, 18,0))
            ),
            listOf(FavoriteModel("faviorite@example.com"))
        ),
        ArticleModel(
            "Title-2",
            "Title 2",
            "This article would be latest.",
            "jiro@example.com",
            LocalDateTime.of(2020, 1, 1, 18, 0),
            emptyList(),
            emptyList()
        )
    )

    override fun getArticles(): List<ArticleModel> {
        return articles
    }

    override fun getArticle(slug: String): ArticleModel? {
        return articles.find { it.slug == slug }
    }

    override fun getCommentsOfArticle(slug: String): List<CommentModel> {
        return getArticle(slug)?.comments ?: emptyList()
    }

    override fun saveComments(slug: String, commentModels: List<CommentModel>): List<CommentModel> {
        val article = articles.find { it.slug == slug }!!
        article.comments = commentModels
        return commentModels
    }

    override fun saveArticle(articleModel: ArticleModel): ArticleModel {
        articles.removeIf { it.slug == articleModel.slug }
        articles.add(articleModel)
        return articleModel
    }

    override fun updateArticle(slug: String, articleModel: ArticleModel): ArticleModel {
        articles.removeIf { slug == it.slug }
        articles.add(articleModel)
        return articleModel
    }

    override fun deleteArticle(slug: String) {
        articles.removeIf { slug == it.slug }
    }
}