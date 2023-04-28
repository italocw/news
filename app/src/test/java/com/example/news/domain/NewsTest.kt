package com.example.news.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

import java.time.LocalDateTime

class NewsTest {

    @Test
    fun shouldHaveCompleteInformation() {
        val news = News(
            "Motorista morre em acidente",
            "www.google.com",
            "www.google.com",
            "descrição",
            "uol",
            "José",
            LocalDateTime.now(),
            "Conteúdo de grande qualidade"
        )

        assertTrue(news.hasCompleteInformation())
    }
    @Test
    fun shouldNotHaveCompleteInformation() {
        val news = News(
            null,
            "www.google.com",
            null,
            null,
            "uol",
            null,
            LocalDateTime.now(),
            null
        )

        assertFalse(news.hasCompleteInformation())
    }
}