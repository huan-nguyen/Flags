package dev.huannguyen.flags.data

sealed class DataResponse<out T> {
    data class Success<T>(val data: T) : DataResponse<T>()
    data class Failure(val message: String) : DataResponse<Nothing>()
    object Fetching : DataResponse<Nothing>()
}
