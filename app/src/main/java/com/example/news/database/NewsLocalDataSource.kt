import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.news.domain.News
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.news.Result
import com.example.news.Result.Success
import com.example.news.database.NewsDao
import com.example.news.database.asDomainModel
import com.example.news.network.asDatabaseModel

class NewsLocalDataSource internal constructor(
    private val newsDao: NewsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun observeNewsList(): LiveData<Result<List<News>>> {
        return newsDao.observeNews().map {
            Success(it.asDomainModel())
        }
    }

    fun observeNews(url: String): LiveData<Result<News>> {
        return newsDao.observeNewsById(url).map {
            Success(it.asDomainModel())
        }
    }

    suspend fun getNewsList(): Result<List<News>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(newsDao.getNewsList())
        } catch (e: Exception) {
            Error(e)
        } as Result<List<News>>
    }

    suspend fun getNews(url: String): Result<News> = withContext(ioDispatcher) {
        try {
            val news = newsDao.getNewsByUrl(url)
            if (news != null) {
                return@withContext Success(news)
            } else {
                return@withContext Error(Exception("News not found!"))
            }
        } catch (e: Exception) {
            return@withContext Error(e)
        }
    } as Result<News>

    suspend fun saveNews(news: News) = withContext(ioDispatcher) {
        newsDao.insertNews(news.asDatabaseModel())
    }

    suspend fun deleteNews(newsUrl: String) = withContext<Unit>(ioDispatcher) {
        newsDao.deleteNewsById(newsUrl)
    }
}
