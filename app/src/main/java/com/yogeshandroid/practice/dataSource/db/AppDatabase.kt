package com.yogeshandroid.practice.dataSource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yogeshandroid.practice.dataSource.accessObject.ProductDao
import com.yogeshandroid.practice.dataSource.utils.ProductTypeConverter
import com.yogeshandroid.practice.model.Product

@Database(entities = [Product::class], version = 4)
@TypeConverters(ProductTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): ProductDao
}