package com.yogeshandroid.practice.dataSource.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yogeshandroid.practice.model.Dimensions
import com.yogeshandroid.practice.model.Meta
import com.yogeshandroid.practice.model.Review

class ProductTypeConverter {
    @TypeConverter
    fun fromDimension(dimensions: Dimensions): String {
        return Gson().toJson(dimensions)
    }

    @TypeConverter
    fun toDimension(string: String): Dimensions {
        return Gson().fromJson(string, Dimensions::class.java)
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toStringList(string: String): List<String> {
        val stringType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(string, stringType)
    }

    @TypeConverter
    fun fromMeta(meta: Meta): String {
        return Gson().toJson(meta)
    }

    @TypeConverter
    fun toMeta(string: String): Meta {
        return Gson().fromJson(string, Meta::class.java)
    }

    @TypeConverter
    fun fromReviewsList(list: List<Review>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toReviewsList(string: String): List<Review> {
        val stringType = object : TypeToken<List<Review>>() {}.type
        return Gson().fromJson(string, stringType)
    }
}