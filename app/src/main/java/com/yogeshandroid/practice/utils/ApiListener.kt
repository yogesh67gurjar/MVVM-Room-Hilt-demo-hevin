package com.yogeshandroid.practice.utils

interface ApiListener {
    fun onSuccess(any: Any, type: String)
    fun onFailure(error: String)
}