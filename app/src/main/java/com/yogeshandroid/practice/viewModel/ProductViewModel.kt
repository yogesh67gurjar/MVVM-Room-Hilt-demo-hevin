package com.yogeshandroid.practice.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yogeshandroid.practice.dataSource.db.AppDatabase
import com.yogeshandroid.practice.model.Product
import com.yogeshandroid.practice.model.ProductResponse
import com.yogeshandroid.practice.repository.ProductRepository
import com.yogeshandroid.practice.utils.ApiListener
import com.yogeshandroid.practice.utils.ProductSortBy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private var productRepository: ProductRepository,
    private var appDatabase: AppDatabase
) : ViewModel() {

    val searchBy: MutableLiveData<String> = MutableLiveData("")
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    var sortBy: MutableLiveData<ProductSortBy> = MutableLiveData(ProductSortBy.None)

    private var pageNumber = 1
    private val limit = 10;
    var isLastPage: Boolean = false

    private var _products: MutableLiveData<ProductResponse> = MutableLiveData()
    var products: LiveData<ProductResponse> = _products

    private var _failure: MutableLiveData<String> = MutableLiveData()
    var failure: LiveData<String> = _failure

    private var apiListener: ApiListener = object : ApiListener {
        override fun onSuccess(any: Any, type: String) {
            _products.postValue(any as ProductResponse)
            saveProductsInRoom(any.products)
            isLastPage = pageNumber - 1 > any.total / 10
            isLoading.postValue(false)
        }

        override fun onFailure(error: String) {
            _failure.postValue(error)
            isLoading.postValue(false)
        }
    }

    init {
        productRepository.getProducts(limit * (pageNumber - 1), limit, apiListener)
    }

    fun getProducts() {
        pageNumber += 1;
        productRepository.getProducts(limit * (pageNumber - 1), limit, apiListener)
        isLoading.postValue(true)
    }

    fun saveProductsInRoom(products: List<Product>) {
        viewModelScope.launch(Dispatchers.IO) {
            products.map {
                it.pageNumber = pageNumber
            }
            appDatabase.getDao().insertProducts(products)
        }
    }
}