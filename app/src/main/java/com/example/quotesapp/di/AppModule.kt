package com.example.quotesapp.di

import android.content.Context
import androidx.room.Room
import com.example.quotesapp.data.api.QuoteApiService
import com.example.quotesapp.data.db.dao.QuoteDao
import com.example.quotesapp.data.db.QuoteDatabase
import com.example.quotesapp.data.db.dao.AuthorQuotesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideDao(database: QuoteDatabase): QuoteDao {
        return database.getDao()
    }

    @Provides
    fun provideAuthorDao(database: QuoteDatabase): AuthorQuotesDao {
        return database.getAuthorQuotesDao()
    }
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): QuoteDatabase {
        return Room.databaseBuilder(
            app,
            QuoteDatabase::class.java,
            "quote_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideBaseUrl(): String = "https://api.quotable.io"

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): QuoteApiService = retrofit.create(QuoteApiService::class.java)
}