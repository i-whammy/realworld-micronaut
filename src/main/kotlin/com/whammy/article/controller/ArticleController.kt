package com.whammy.article.controller

import com.whammy.article.domain.Article
import com.whammy.article.domain.Comment
import com.whammy.article.usecase.ArticleUsecase
import com.whammy.user.service.UserService
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.reactivex.Single
import java.time.LocalDateTime
import javax.inject.Inject

@Controller("/api/articles")
class ArticleController(@Inject private val articleUsecase: ArticleUsecase, @Inject private val userService: UserService) {

    @Get("/")
    fun getArticles(): HttpResponse<ArticlesResponse> {
        return HttpResponse.ok(articleUsecase.getArticlesOrderedByUpdatedDateTime().map { it.convertToArticleResponse() }.let { ArticlesResponse(it, it.size) })
    }

    @Post("/", consumes = [MediaType.APPLICATION_JSON])
    fun postArticle(@Header authorization: String?, @Body request: Single<ArticleRequest>): Single<HttpResponse<ArticleResponse>> {
        return request.map {
            val userId = userService.getUserId(authorization!!)
            HttpResponse.created(articleUsecase.createNewArticle(userId, it.title, it.body).convertToArticleResponse())
        }
    }

    @Put("/{slug}", consumes = [MediaType.APPLICATION_JSON])
    fun updateArticle(@Header authorization: String?, @Body request: Single<UpdateArticleRequest>, @PathVariable slug: String): Single<HttpResponse<ArticleResponse>> {
        return request.map {
            val userId = userService.getUserId(authorization!!)
            HttpResponse.ok(articleUsecase.updateArticle(slug, userId, it.title, it.body).convertToArticleResponse())
        }
    }

    @Get("/{slug}")
    fun getArticle(@PathVariable slug: String): HttpResponse<ArticleResponse>? {
        return HttpResponse.ok(articleUsecase.getArticle(slug).convertToArticleResponse())
    }

    @Delete("/{slug}")
    fun deleteArticle(@Header authorization: String?, @PathVariable slug: String) {
        val userId = userService.getUserId(authorization!!)
        articleUsecase.delete(slug, userId)
    }

    @Get("/{slug}/comments")
    fun getComments(@PathVariable slug: String): HttpResponse<CommentsResponse>? {
        return HttpResponse.ok(articleUsecase.getCommentsOfArticle(slug).map { it.convertToCommentResponse() }.let(::CommentsResponse))
    }

    @Post("/{slug}/comments")
    fun addComment(@Header authorization: String?, @Body request: Single<CommentRequest>, @PathVariable slug: String): Single<HttpResponse<CommentResponse>> {
        return request.map {
            val userId = userService.getUserId(authorization!!)
            HttpResponse.created(articleUsecase.addComment(userId, slug, it.body).convertToCommentResponse())
        }
    }

    @Post("/{slug}/favorite")
    fun addFavorite(@Header authorization: String?, @PathVariable slug: String): HttpResponse<ArticleResponse> {
        val userId = userService.getUserId(authorization!!)
        return HttpResponse.ok(articleUsecase.toggleFavorite(slug, userId).convertToArticleResponse())
    }

    @Delete("/{slug}/favorite")
    fun removeFavorite(@Header authorization: String?, @PathVariable slug: String): HttpResponse<ArticleResponse> {
        val userId = userService.getUserId(authorization!!)
        return HttpResponse.ok(articleUsecase.toggleFavorite(slug, userId).convertToArticleResponse())
    }

    private fun Article.convertToArticleResponse(): ArticleResponse {
        return ArticleResponse(slug, title, body, createdAt, updatedAt, favorites.isNotEmpty(), favorites.count())
    }

    private fun Comment.convertToCommentResponse(): CommentResponse {
        return CommentResponse(id, body, createdAt, updatedAt)
    }

    data class ArticleRequest(
            val title: String,
            val body: String
    )

    data class UpdateArticleRequest(
            val title: String,
            val body: String
    )

    data class ArticlesResponse(
            val articles: List<ArticleResponse>,
            val articlesCount: Int
    )

    data class ArticleResponse(
            val slug: String,
            val title: String,
            val body: String,
            val createdAt: LocalDateTime,
            val updatedAt: LocalDateTime?,
            val favorited: Boolean,
            val favoritesCount: Int
    )

    data class CommentRequest(
            val body: String
    )

    data class CommentsResponse(
            val comments: List<CommentResponse>
    )

    data class CommentResponse(
            val id: Int,
            val body: String,
            val createdAt: LocalDateTime,
            val updatedAt: LocalDateTime?
    )
}
