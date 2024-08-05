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
import com.yogeshandroid.practice.databinding.ActivityMainBinding
import com.yogeshandroid.practice.databinding.BsSortByBinding
import com.yogeshandroid.practice.model.UserResponse
import com.yogeshandroid.practice.utils.UserSortBy
import com.yogeshandroid.practice.viewModel.MainViewModel
import com.yogeshandroid.practice.views.adapters.UsersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var adapter: UsersAdapter
    private lateinit var llm: LinearLayoutManager

    private var myData: MutableList<UserResponse.User> = mutableListOf()
    private lateinit var sortByBottomSheet: BottomSheetDialog
    private lateinit var bsSortByBinding: BsSortByBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        initSetup()
        initBottomSheet()
        initPagination()
        initSearch()

        mainViewModel.isLoading.observe(this) {
            if (it) {
                activityMainBinding.loader.visibility = View.VISIBLE
            } else {
                activityMainBinding.loader.visibility = View.GONE
            }
        }

        mainViewModel.searchBy.observe(this) {
            searchBy(it)
        }

        mainViewModel.sortBy.observe(this) {
            searchBy(mainViewModel.searchBy.value.toString())
        }

        mainViewModel.users.observe(this) {
            myData.addAll(it.users)
            searchBy(mainViewModel.searchBy.value.toString())
        }

        mainViewModel.failure.observe(this) {
            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchBy(s: String) {
        val filteredlist: MutableList<UserResponse.User> = mutableListOf()
        if (myData.isNotEmpty()) {
            if (s != "") {
                for (item in myData) {
                    if (item.firstName.lowercase().contains(s.lowercase()) &&
                        item.email.lowercase().contains(s.lowercase())
                    ) {
                        filteredlist.add(item)
                    }
                }
            } else {
                filteredlist.addAll(myData)
            }

            when (mainViewModel.sortBy.value) {
                UserSortBy.None -> {
                    filteredlist.sortBy { it.id }
                }

                UserSortBy.Name -> {
                    filteredlist.sortBy { it.firstName }
                }

                UserSortBy.Email -> {
                    filteredlist.sortBy { it.email }
                }

                null -> {}
            }
            activityMainBinding.sortBy.text = mainViewModel.sortBy.value?.name.toString()


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
        activityMainBinding.placeHolder.visibility = View.GONE
        activityMainBinding.recyclerView.visibility = View.VISIBLE
    }

    private fun showPlaceHolder() {
        activityMainBinding.placeHolder.visibility = View.VISIBLE
        activityMainBinding.recyclerView.visibility = View.GONE
    }


    private fun initSearch() {
        activityMainBinding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mainViewModel.searchBy.postValue(activityMainBinding.searchEt.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun initBottomSheet() {
        sortByBottomSheet = BottomSheetDialog(this)
        bsSortByBinding = BsSortByBinding.inflate(layoutInflater)
        sortByBottomSheet.setContentView(bsSortByBinding.root)

        bsSortByBinding.noneBtn.setOnClickListener {
            mainViewModel.sortBy.value = UserSortBy.None
            sortByBottomSheet.dismiss()
        }

        bsSortByBinding.nameBtn.setOnClickListener {
            mainViewModel.sortBy.value = UserSortBy.Name
            sortByBottomSheet.dismiss()
        }

        bsSortByBinding.emailBtn.setOnClickListener {
            mainViewModel.sortBy.value = UserSortBy.Email
            sortByBottomSheet.dismiss()
        }

        activityMainBinding.sortBtn.setOnClickListener {
            sortByBottomSheet.show()
        }

    }

    private fun initPagination() {
        activityMainBinding.recyclerView.setOnScrollChangeListener { p0, p1, p2, p3, p4 ->
            val visibleItemCount: Int = llm.childCount
            val totalItemCount: Int = llm.itemCount
            val firstVisibleItemPosition: Int = llm.findFirstVisibleItemPosition()

            if (!mainViewModel.isLastPage && mainViewModel.isLoading.value == false && mainViewModel.searchBy.value == "") {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
                ) {
                    loadMoreItems()
                }
            }
        }
    }

    private fun loadMoreItems() {
        mainViewModel.getUsers()
    }


    private fun initSetup() {
        adapter = UsersAdapter(myData, this@MainActivity)
        activityMainBinding.recyclerView.adapter = adapter
        llm = LinearLayoutManager(this@MainActivity)
        activityMainBinding.recyclerView.layoutManager = llm
    }
}