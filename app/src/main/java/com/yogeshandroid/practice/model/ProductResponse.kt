package com.yogeshandroid.practice.model

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
    val availabilityStatus: String,
    val brand: String,
    val category: String,
    val description: String,
    val dimensions: Dimensions,
    val discountPercentage: Double,
    @PrimaryKey
    val id: Int,
    var pageNumber: Int,
    val images: List<String>,
    val meta: Meta,
    val minimumOrderQuantity: Int,
    val price: Double,
    val rating: Double,
    val returnPolicy: String,
    val reviews: List<Review>,
    val shippingInformation: String,
    val sku: String,
    val stock: Int,
    val tags: List<String>,
    val thumbnail: String,
    val title: String,
    val warrantyInformation: String,
    val weight: Int
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