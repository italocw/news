package com.example.news.models

import org.junit.Assert.assertFalse
import org.junit.Test

import java.time.LocalDateTime


class NewsTest {


    @Test
    fun shouldNotHaveCompleteInformation() {
        val news = News(
            "Motorista morre em acidente",
            "null",
            "null",
            "null",
            "null",
            "null",
            LocalDateTime.now(),
            "null"
        )

        assertFalse(news.hasCompleteInformation())
    }
}