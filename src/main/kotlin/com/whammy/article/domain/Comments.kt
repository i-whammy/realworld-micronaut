package com.whammy.article.domain

import com.whammy.article.exception.CommentNotFoundException
import java.time.LocalDateTime

data class Comments(private val comments: List<Comment>): FCC<Comment>(comments) {
    fun add(userEmailEddress: String, body: String): Comments {
        val newComment = comments.toMutableList()
        newComment.add(Comment(getLatestCommentId(), body, userEmailEddress, LocalDateTime.now()))
        return Comments(newComment)
    }

    private fun getLatestCommentId(): Int {
        return this.comments.maxBy { it.id }?.id?.plus(1) ?: 1
    }

    fun getLatestComment(): Comment {
        return this.comments.maxBy { it.id } ?: throw CommentNotFoundException("There is no comment.")
    }
}