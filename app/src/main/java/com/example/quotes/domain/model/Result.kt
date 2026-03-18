package com.example.quotes.domain.model

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val errorMessage: String, val exception: Throwable? = null) : Result<Nothing>
}