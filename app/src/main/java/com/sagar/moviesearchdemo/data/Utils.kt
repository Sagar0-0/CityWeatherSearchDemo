package com.sagar.moviesearchdemo.data

import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.IOException

inline fun <T> getApiResponse(apiCall: () -> Response<T>): Response<T> {
    return try {
        apiCall()
    } catch (e: IOException) {
        Response.error(500, "Network error: ${e.localizedMessage}".toResponseBody(null))
    }
}