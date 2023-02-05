package com.test.filmlist.api

import com.test.filmlist.util.Constants
import retrofit2.Response

abstract class BaseRemoteDataSource {

    suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Result.Success(body)
            }
            return Result.Error(Constants.GENERIC_ERROR)
        } catch (e: Exception) {
            return Result.Error(e.message ?: e.toString())
        }
    }
}