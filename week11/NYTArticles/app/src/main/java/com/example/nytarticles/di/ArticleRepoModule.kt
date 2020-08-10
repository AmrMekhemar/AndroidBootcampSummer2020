package com.example.nytarticles.di
import androidx.room.Room
import com.example.nytarticles.App.Companion.BASE_URL
import com.example.nytarticles.App.Companion.DATABASE_NAME
import com.example.nytarticles.database.ArticleDB
import com.example.nytarticles.database.ArticlesDAO
import com.example.nytarticles.networking.RemoteApiService
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import splitties.init.appCtx

val articleRepoModule = module {
    fun provideUseApi(retrofit: Retrofit): RemoteApiService {
        return retrofit.create(RemoteApiService::class.java)
    }

    fun provideDB(): ArticleDB {
        return Room.databaseBuilder(appCtx, ArticleDB::class.java, DATABASE_NAME)
            .build()
    }

    fun provideDao(): ArticlesDAO {
        return provideDB().articleDao()
    }

    fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }

    fun provideHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(client)
            .build()
    }

    single { provideGson() }
    single { provideHttpClient() }
    single { provideRetrofit(get(), get()) }
    single { provideDB() }
    single { provideDao() }
    single { provideUseApi(get()) }
}


