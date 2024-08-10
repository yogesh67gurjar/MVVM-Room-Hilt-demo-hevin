package com.yogeshandroid.practice.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yogeshandroid.practice.dataSource.db.AppDatabase
import com.yogeshandroid.practice.model.Product
import com.yogeshandroid.practice.utils.ProductSortBy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedProductsViewModel @Inject constructor(var appDatabase: AppDatabase) : ViewModel() {
    private var limit: Int = -1

    val searchBy: MutableLiveData<String> = MutableLiveData("")
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    var sortBy: MutableLiveData<ProductSortBy> = MutableLiveData(ProductSortBy.None)

    private var pageNumber = 0
    var isLastPage: Boolean = false

    private var _products: MutableLiveData<List<Product>> = MutableLiveData()
    var products: LiveData<List<Product>> = _products

    init {
        viewModelScope.launch {
            val res = viewModelScope.async(Dispatchers.IO) {
                appDatabase.getDao().getMaxPage()
            }
            limit = res.await()
            Log.d("~~~777~~~", "$limit")
            getProductsFromDb()
        }
    }

    suspend fun getProductsFromDb() {
        pageNumber+=1
        if (limit >= pageNumber) {
            isLastPage = false
            val deferred = viewModelScope.async(Dispatchers.IO) {
                appDatabase.getDao().getProducts(pageNumber)
            }
            isLoading.postValue(true)
            _products.postValue(deferred.await())
            isLoading.postValue(false)
        } else {
            isLastPage = true
        }
    }

}