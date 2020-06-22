package com.whammy.article.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ArticlesTest {

    @Test
    fun testSortByUpdatedAt() {
        val articles = Articles(
            listOf(
                Article(
                    "title-1",
                    "title1",
                    "body1",
                    "taro@example.com",
                    LocalDateTime.of(2019, 1, 1, 0, 0),
                    Comments(emptyList()),
                    emptyList()
                ),
                Article(
                    "title-2",
                    "title2",
                    "body2",
                    "taro@example.com",
                    LocalDateTime.of(2020, 1, 1, 0, 0),
                    Comments(emptyList()),
                    emptyList()
                )
            )
        )
        val expected = Articles(
            listOf(
                Article(
                    "title-2",
                    "title2",
                    "body2",
                    "taro@example.com",
                    LocalDateTime.of(2020, 1, 1, 0, 0),
                    Comments(emptyList()),
                    emptyList()
                ),
                Article(
                    "title-1",
                    "title1",
                    "body1",
                    "taro@example.com",
                    LocalDateTime.of(2019, 1, 1, 0, 0),
                    Comments(emptyList()),
                    emptyList()
                )
            )
        )

        assertEquals(expected, articles.sortByUpdatedAt())
    }
}