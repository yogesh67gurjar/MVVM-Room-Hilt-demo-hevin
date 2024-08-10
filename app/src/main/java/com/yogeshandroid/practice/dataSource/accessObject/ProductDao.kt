package com.yogeshandroid.practice.dataSource.accessObject

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yogeshandroid.practice.model.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(products: List<Product>)

    @Query("SELECT MAX(pageNumber) FROM product")
    fun getMaxPage(): Int

    @Query("SELECT * FROM product WHERE pageNumber=:page")
    fun getProducts(page: Int): List<Product>
}