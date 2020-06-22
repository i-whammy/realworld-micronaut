package com.whammy.article.repository

interface ArticleDriver {
    fun getArticles(): List<ArticleModel>
    fun getArticle(slug: String): ArticleModel?
    fun getCommentsOfArticle(slug: String): List<CommentModel>
    fun saveComments(slug: String, commentModels: List<CommentModel>): List<CommentModel>
    fun saveArticle(articleModel: ArticleModel): ArticleModel
    fun updateArticle(slug: String, articleModel: ArticleModel): ArticleModel
    fun deleteArticle(slug: String)
}
