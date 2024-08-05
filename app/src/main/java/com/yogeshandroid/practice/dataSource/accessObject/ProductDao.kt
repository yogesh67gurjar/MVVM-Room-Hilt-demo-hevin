package com.yogeshandroid.practice.dataSource.accessObject

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.yogeshandroid.practice.model.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(products: List<Product>)
}