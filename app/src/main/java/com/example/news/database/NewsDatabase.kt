package com.example.news.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NewsDao {
    @Query("SELECT * FROM News")
    fun observeNews(): LiveData<List<DatabaseNews>>

    @Query("SELECT * FROM News WHERE url = :url")
    fun observeNewsById(url: String): LiveData<DatabaseNews>

    @Query("SELECT * FROM News")
    suspend fun getNewsList(): List<DatabaseNews>

    @Query("SELECT * FROM News WHERE url = :url")
    suspend fun getNewsByUrl(url: String): DatabaseNews?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: DatabaseNews)

    @Update
    suspend fun updateNews(news: DatabaseNews): Int

    @Query("DELETE FROM News WHERE url = :url")
    suspend fun deleteNewsById(url: String): Int

    @Query("DELETE FROM News")
    suspend fun deleteNews()
}

@Database(entities = [DatabaseNews::class], version = 1)
abstract class NewsDatabase: RoomDatabase() {
    abstract val newsDao: NewsDao
}


