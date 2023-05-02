package com.example.news

sealed class Result<out R>  {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Searching : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Searching -> "Searching"
        }
    }
}

val Result<*>.succeeded
    get() = this is Result.Success && data != null