package com.whammy.article.domain

abstract class FCC<T> (private val values: List<T>) {
    fun <R> map(function: (T) -> R): List<R> {
        return values.map(function)
    }
}