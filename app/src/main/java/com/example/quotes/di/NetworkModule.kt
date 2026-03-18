package com.example.quotes.di

import android.content.Context
import com.example.quotes.data.remote.api.ApiService
import com.example.quotes.util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true    // Ignores the objects the app doesn't care about.
            coerceInputValues = true    // Use the local values from the DTO when API sends
                                        //  blank or null values.
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {

        val cacheSize = (10 * 1024 * 1024).toLong()
        val cache = Cache(directory = context.cacheDir, maxSize = cacheSize)

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val cacheInterceptor = Interceptor { chain ->
            val response = chain.proceed(request = chain.request())
            val maxAge = 5 * 60
            response.newBuilder()
                .header(name = "Cache-Control", value = "public, max-age=$maxAge")
                .removeHeader(name = "Pragma")
                .build()
        }

        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(cacheInterceptor)
            .connectTimeout(30, unit = TimeUnit.SECONDS)
            .readTimeout(30, unit = TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}