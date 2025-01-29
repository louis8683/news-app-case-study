package com.louislu.news.data.network

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber


// Logging interceptors
class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Log the full URL
        Timber.i("Request URL: ${request.url}")

        return chain.proceed(request) // Continue the request
    }
}