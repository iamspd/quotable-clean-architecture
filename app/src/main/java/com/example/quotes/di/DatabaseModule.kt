package com.example.quotes.di

import android.content.Context
import androidx.room.Room
import com.example.quotes.data.local.dao.CollectionDao
import com.example.quotes.data.local.database.AppDatabase
import com.example.quotes.util.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providesCollectionDao(appDatabase: AppDatabase): CollectionDao {
        return appDatabase.collectionDao()
    }
}
