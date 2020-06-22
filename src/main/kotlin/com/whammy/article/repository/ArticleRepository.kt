package com.whammy.article.repository

import com.whammy.article.domain.*
import com.whammy.article.exception.ArticleNotFoundException
import com.whammy.article.usecase.IArticleRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository(@Inject private val driver: ArticleDriver):
    IArticleRepository {
    override fun getArticles(): Articles {
        return Articles(driver.getArticles().map { it.convertToArticle() })
    }

    override fun getArticle(slug: String): Article {
        return driver.getArticle(slug)?.let { it.convertToArticle() }
            ?: throw ArticleNotFoundException("Article not found. slug = $slug")
    }

    override fun getCommentsOfArticle(slug: String): Comments {
        val article = driver.getArticle(slug) ?: throw ArticleNotFoundException("Article not found. slug = $slug")
        return driver.getCommentsOfArticle(slug).map {
            Comment(it.id, it.body, it.authorEmailAddress, it.createdAt, it.updatedAt)
        }.let { Comments(it) }
    }

    override fun saveComments(slug: String, comments: Comments): Comments {
        driver.saveComments(slug, comments.map {
            CommentModel(it.id, it.body, it.authorEmailAddress, it.createdAt, it.updatedAt)
        })
        return comments
    }

    override fun saveArticle(article: Article): Article {
        driver.saveArticle(ArticleModel(article.slug, article.title, article.body, article.authorEmailAddress, article.createdAt,
            article.comments.map { CommentModel(it.id, it.body, it.authorEmailAddress, it.createdAt, it.updatedAt) },
            article.favorites.map { FavoriteModel(it.userEmailAddress) }))
        return article
    }

    override fun updateArticle(slug: String, article: Article): Article {
        return driver.updateArticle(slug, article.convertToArticleModel()).convertToArticle()
    }

    override fun articleExists(slug: String): Boolean {
        return driver.getArticle(slug) != null
    }

    override fun deleteArticle(slug: String) {
        driver.deleteArticle(slug)
    }

    private fun ArticleModel.convertToArticle() = Article(
        slug,
        title,
        body,
        authorEmailAddress,
        createdAt,
        comments.convertToComments(),
        favorites.convertToFavorites(),
        updatedAt
    )

    private fun Article.convertToArticleModel() = ArticleModel(slug, title, body, authorEmailAddress, createdAt,
        comments.map { CommentModel(it.id, it.body, it.authorEmailAddress, it.createdAt, it.updatedAt) },
        favorites.map { FavoriteModel(it.userEmailAddress) }, updatedAt)

    private fun List<CommentModel>.convertToComments() =
        this.map { it.convertToComment() }.let(::Comments)

    private fun CommentModel.convertToComment() = Comment(id, body, authorEmailAddress, createdAt, updatedAt)

    private fun List<FavoriteModel>.convertToFavorites() = this.map { favorite -> Favorite(favorite.userEmailAddress) }
}