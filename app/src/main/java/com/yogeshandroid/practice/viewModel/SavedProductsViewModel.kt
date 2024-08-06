package com.yogeshandroid.practice.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yogeshandroid.practice.dataSource.db.AppDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedProductsViewModel @Inject constructor(private val appDatabase: AppDatabase) :
    ViewModel() {
    private var page: Int = -1

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val deferred: Deferred<Int> = viewModelScope.async { appDatabase.getDao().getMaxPage() }
            page = deferred.await()
            Log.d("~~111~~~", "$page")
        }
    }

}