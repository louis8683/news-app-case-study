package com.louislu.news.data.network

import com.louislu.news.data.BuildConfig
import com.louislu.news.data.network.dto.NewsResponseDto
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber

interface NewsApiService {
    @GET("everything")
    suspend fun getEverything(
        @Query("q") keyword: String,
        @Query("apiKey") apikey: String = BuildConfig.API_KEY
    ): NewsResponseDto

}

// API endpoint: {{base_url}}/everything?q=bitcoin&apiKey={{api_key}}

// Logging interceptors
class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Log the full URL
        Timber.i("Request URL: ${request.url}")

        return chain.proceed(request) // Continue the request
    }
}

//val client = OkHttpClient.Builder()
//    .addInterceptor(LoggingInterceptor()) // 🔹 Add the custom logging interceptor
//    .addInterceptor { chain ->
//        val original = chain.request()
//        val originalUrl = original.url
//
//        val newUrl = originalUrl.newBuilder()
////            .addQueryParameter("apiKey", BuildConfig.API_KEY) // Auto-add API key
//            .build()
//
//        val request = original.newBuilder().url(newUrl).build()
//
//        Timber.i("Updated Request URL: ${request.url}") // Log updated request with API key
//
//        chain.proceed(request)
//    }
//    .build()

