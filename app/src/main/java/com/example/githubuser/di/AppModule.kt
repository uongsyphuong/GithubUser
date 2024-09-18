package com.example.githubuser.di

import android.app.Application
import android.content.Context
import com.example.githubuser.storage.AppDataStorage
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideAppDataStorage(context: Context, gson: Gson) = AppDataStorage(context, gson)
}
