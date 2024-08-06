package com.yogeshandroid.practice.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class ProductResponse(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)

@Entity(tableName = "Product")
data class Product(
    val availabilityStatus: String = "",
    val brand: String? = null,
    val category: String = "",
    val description: String = "",
    val dimensions: Dimensions = Dimensions(0.0, 0.0, 0.0),
    val discountPercentage: Double = 0.0,
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "pageNumber")
    var pageNumber: Int,
    val images: List<String> = listOf(),
    val meta: Meta = Meta("", "", "", ""),
    val minimumOrderQuantity: Int = 0,
    val price: Double = 0.0,
    val rating: Double = 0.0,
    val returnPolicy: String = "",
    val reviews: List<Review> = listOf(),
    val shippingInformation: String = "",
    val sku: String = "",
    val stock: Int = 0,
    val tags: List<String> = listOf(),
    val thumbnail: String = "",
    val title: String = "",
    val warrantyInformation: String = "",
    val weight: Int = 0
)

data class Dimensions(
    val depth: Double,
    val height: Double,
    val width: Double
)

data class Review(
    val comment: String,
    val date: String,
    val rating: Int,
    val reviewerEmail: String,
    val reviewerName: String
)

data class Meta(
    val barcode: String,
    val createdAt: String,
    val qrCode: String,
    val updatedAt: String
)