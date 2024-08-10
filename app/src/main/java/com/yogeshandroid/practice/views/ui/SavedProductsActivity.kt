package com.yogeshandroid.practice.views.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yogeshandroid.practice.databinding.ActivitySavedProductsBinding
import com.yogeshandroid.practice.databinding.BsProductSortByBinding
import com.yogeshandroid.practice.model.Product
import com.yogeshandroid.practice.utils.ProductSortBy
import com.yogeshandroid.practice.viewModel.SavedProductsViewModel
import com.yogeshandroid.practice.views.adapters.ProductsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SavedProductsActivity : AppCompatActivity() {
    private lateinit var activitySavedProductsBinding: ActivitySavedProductsBinding

    private val savedProductsViewModel: SavedProductsViewModel by viewModels()
    private lateinit var adapter: ProductsAdapter
    private lateinit var llm: LinearLayoutManager

    private var myData: MutableList<Product> = mutableListOf()
    private lateinit var sortByBottomSheet: BottomSheetDialog
    private lateinit var bsProductSortByBinding: BsProductSortByBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySavedProductsBinding = ActivitySavedProductsBinding.inflate(layoutInflater)
        setContentView(activitySavedProductsBinding.root)

        initSetup()
        initBottomSheet()
        initPagination()
        initSearch()
        attachObservers()
    }

    private fun attachObservers() {
        savedProductsViewModel.isLoading.observe(this) {
            if (it) {
                activitySavedProductsBinding.loader.visibility = View.VISIBLE
            } else {
                activitySavedProductsBinding.loader.visibility = View.GONE
            }
        }

        savedProductsViewModel.searchBy.observe(this) {
            searchBy(it)
        }

        savedProductsViewModel.sortBy.observe(this) {
            searchBy(savedProductsViewModel.searchBy.value.toString())
        }

        savedProductsViewModel.products.observe(this) {
            myData.addAll(it)
            searchBy(savedProductsViewModel.searchBy.value.toString())
        }
    }

    private fun searchBy(s: String) {
        val filteredlist: MutableList<Product> = mutableListOf()
        if (myData.isNotEmpty()) {
            if (s != "") {
                for (item in myData) {
                    if (item.title.lowercase().contains(s.lowercase())) {
                        filteredlist.add(item)
                    }
                }
            } else {
                filteredlist.addAll(myData)
            }

            when (savedProductsViewModel.sortBy.value) {
                ProductSortBy.None -> {
                    filteredlist.sortBy { it.id }
                }

                ProductSortBy.Title -> {
                    filteredlist.sortBy { it.title }
                }

                ProductSortBy.Price -> {
                    filteredlist.sortBy { it.price }
                }

                null -> {}
            }
            activitySavedProductsBinding.sortBy.text = savedProductsViewModel.sortBy.value?.name.toString()

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
        activitySavedProductsBinding.placeHolder.visibility = View.GONE
        activitySavedProductsBinding.recyclerView.visibility = View.VISIBLE
    }

    private fun showPlaceHolder() {
        activitySavedProductsBinding.placeHolder.visibility = View.VISIBLE
        activitySavedProductsBinding.recyclerView.visibility = View.GONE
    }


    private fun initSearch() {
        activitySavedProductsBinding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                savedProductsViewModel.searchBy.postValue(activitySavedProductsBinding.searchEt.text.toString())
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
            savedProductsViewModel.sortBy.value = ProductSortBy.None
            sortByBottomSheet.dismiss()
        }

        bsProductSortByBinding.titleBtn.setOnClickListener {
            savedProductsViewModel.sortBy.value = ProductSortBy.Title
            sortByBottomSheet.dismiss()
        }

        bsProductSortByBinding.priceBtn.setOnClickListener {
            savedProductsViewModel.sortBy.value = ProductSortBy.Price
            sortByBottomSheet.dismiss()
        }

        activitySavedProductsBinding.sortBtn.setOnClickListener {
            sortByBottomSheet.show()
        }

    }

    private fun initPagination() {
        activitySavedProductsBinding.recyclerView.setOnScrollChangeListener { p0, p1, p2, p3, p4 ->
            val visibleItemCount: Int = llm.childCount
            val totalItemCount: Int = llm.itemCount
            val firstVisibleItemPosition: Int = llm.findFirstVisibleItemPosition()

            if (!savedProductsViewModel.isLastPage && savedProductsViewModel.isLoading.value == false && savedProductsViewModel.searchBy.value == "") {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
                ) {
                    loadMoreItems()
                }
            }
        }
    }

    private fun loadMoreItems() {
        lifecycleScope.launch {
            savedProductsViewModel.getProductsFromDb()
        }
    }


    private fun initSetup() {
        adapter = ProductsAdapter(myData, this@SavedProductsActivity)
        activitySavedProductsBinding.recyclerView.adapter = adapter
        llm = LinearLayoutManager(this@SavedProductsActivity)
        activitySavedProductsBinding.recyclerView.layoutManager = llm
    }
}