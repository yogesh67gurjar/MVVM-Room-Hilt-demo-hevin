package com.yogeshandroid.practice.views.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yogeshandroid.practice.databinding.ActivityProductBinding
import com.yogeshandroid.practice.databinding.BsProductSortByBinding
import com.yogeshandroid.practice.model.Product
import com.yogeshandroid.practice.utils.ProductSortBy
import com.yogeshandroid.practice.viewModel.ProductViewModel
import com.yogeshandroid.practice.views.adapters.ProductsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductActivity : AppCompatActivity() {
    private lateinit var activityProductBinding: ActivityProductBinding

    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var adapter: ProductsAdapter
    private lateinit var llm: LinearLayoutManager

    private var myData: MutableList<Product> = mutableListOf()
    private lateinit var sortByBottomSheet: BottomSheetDialog
    private lateinit var bsProductSortByBinding: BsProductSortByBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityProductBinding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(activityProductBinding.root)

        initSetup()
        initBottomSheet()
        initPagination()
        initSearch()

        productViewModel.isLoading.observe(this) {
            if (it) {
                activityProductBinding.loader.visibility = View.VISIBLE
            } else {
                activityProductBinding.loader.visibility = View.GONE
            }
        }

        productViewModel.searchBy.observe(this) {
            searchBy(it)
        }

        productViewModel.sortBy.observe(this) {
            searchBy(productViewModel.searchBy.value.toString())
        }

        productViewModel.products.observe(this) {
            myData.addAll(it.products)
            searchBy(productViewModel.searchBy.value.toString())
        }

        productViewModel.failure.observe(this) {
            Toast.makeText(this@ProductActivity, it, Toast.LENGTH_SHORT).show()
        }
    }


    private fun searchBy(s: String) {
        val filteredlist: MutableList<Product> = mutableListOf()
        if (myData.isNotEmpty()) {
            if (s != "") {
                for (item in myData) {
                    if (item.title.lowercase().contains(s.lowercase()) &&
                        item.brand.lowercase().contains(s.lowercase())
                    ) {
                        filteredlist.add(item)
                    }
                }
            } else {
                filteredlist.addAll(myData)
            }

            when (productViewModel.sortBy.value) {
                ProductSortBy.None -> {
                    filteredlist.sortBy { it.id }
                }

                ProductSortBy.Title -> {
                    filteredlist.sortBy { it.title }
                }

                ProductSortBy.Brand -> {
                    filteredlist.sortBy { it.brand }
                }

                null -> {}
            }
            activityProductBinding.sortBy.text = productViewModel.sortBy.value?.name.toString()


            adapter.filterList(filteredlist)
            setVisibility(filteredlist.size)
        } else {
            showPlaceHolder()
        }
    }

    private fun setVisibility(size: Int) {
        if (size > 0) {
            showMainUi()
        } else {
            showPlaceHolder()
        }
    }

    private fun showMainUi() {
        activityProductBinding.placeHolder.visibility = View.GONE
        activityProductBinding.recyclerView.visibility = View.VISIBLE
    }

    private fun showPlaceHolder() {
        activityProductBinding.placeHolder.visibility = View.VISIBLE
        activityProductBinding.recyclerView.visibility = View.GONE
    }


    private fun initSearch() {
        activityProductBinding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                productViewModel.searchBy.postValue(activityProductBinding.searchEt.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun initBottomSheet() {
        sortByBottomSheet = BottomSheetDialog(this)
        bsProductSortByBinding = BsProductSortByBinding.inflate(layoutInflater)
        sortByBottomSheet.setContentView(bsProductSortByBinding.root)

        bsProductSortByBinding.noneBtn.setOnClickListener {
            productViewModel.sortBy.value = ProductSortBy.None
            sortByBottomSheet.dismiss()
        }

        bsProductSortByBinding.titleBtn.setOnClickListener {
            productViewModel.sortBy.value = ProductSortBy.Title
            sortByBottomSheet.dismiss()
        }

        bsProductSortByBinding.brandBtn.setOnClickListener {
            productViewModel.sortBy.value = ProductSortBy.Brand
            sortByBottomSheet.dismiss()
        }

        activityProductBinding.sortBtn.setOnClickListener {
            sortByBottomSheet.show()
        }

    }

    private fun initPagination() {
        activityProductBinding.recyclerView.setOnScrollChangeListener { p0, p1, p2, p3, p4 ->
            val visibleItemCount: Int = llm.childCount
            val totalItemCount: Int = llm.itemCount
            val firstVisibleItemPosition: Int = llm.findFirstVisibleItemPosition()

            if (!productViewModel.isLastPage && productViewModel.isLoading.value == false && productViewModel.searchBy.value == "") {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
                ) {
                    loadMoreItems()
                }
            }
        }
    }

    private fun loadMoreItems() {
        productViewModel.getProducts()
    }


    private fun initSetup() {
        adapter = ProductsAdapter(myData, this@ProductActivity)
        activityProductBinding.recyclerView.adapter = adapter
        llm = LinearLayoutManager(this@ProductActivity)
        activityProductBinding.recyclerView.layoutManager = llm
    }
}