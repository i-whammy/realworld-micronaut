package com.whammy.article.repository

import java.time.LocalDateTime

data class ArticleModel(
    val slug: String,
    val title: String,
    val body: String,
    val authorEmailAddress: String,
    val createdAt: LocalDateTime,
    var comments: List<CommentModel>,
    val favorites: List<FavoriteModel>,
    val updatedAt: LocalDateTime? = null
)

data class CommentModel(
    val id: Int,
    val body: String,
    val authorEmailAddress: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null
)

data class FavoriteModel(
    val userEmailAddress: String
)
