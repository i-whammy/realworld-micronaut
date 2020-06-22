package com.whammy.article.usecase

import com.whammy.article.domain.Article
import com.whammy.article.domain.Articles
import com.whammy.article.domain.Comment
import com.whammy.article.domain.Comments

interface IArticleRepository {
    fun getArticles(): Articles
    fun getArticle(slug: String): Article
    fun getCommentsOfArticle(slug: String): Comments
    fun saveComments(slug: String, comments: Comments): Comments
    fun saveArticle(article: Article): Article
    fun updateArticle(slug: String, article: Article): Article
    fun articleExists(slug: String): Boolean
    fun deleteArticle(slug: String)
}
