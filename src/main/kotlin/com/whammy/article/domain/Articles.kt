package com.whammy.article.domain

data class Articles(private val articles: List<Article>): FCC<Article>(articles) {
    fun sortByUpdatedAt(): Articles {
        return Articles(articles.sortedByDescending { it.createdAt })
    }
}