package com.yogeshandroid.practice.di

import android.content.Context
import androidx.room.Room
import com.yogeshandroid.practice.dataSource.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "Room"
        ).fallbackToDestructiveMigration().build()
    }

}