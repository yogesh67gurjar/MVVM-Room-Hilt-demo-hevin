package com.yogeshandroid.practice.views.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yogeshandroid.practice.R
import com.yogeshandroid.practice.databinding.ActivitySavedProductsBinding

class SavedProductsActivity : AppCompatActivity() {
    lateinit var activitySavedProductsBinding: ActivitySavedProductsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_products)
    }
}