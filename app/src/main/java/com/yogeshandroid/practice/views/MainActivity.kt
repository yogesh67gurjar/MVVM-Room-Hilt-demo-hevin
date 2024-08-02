package com.yogeshandroid.practice.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yogeshandroid.practice.R
import com.yogeshandroid.practice.databinding.ActivityMainBinding
import com.yogeshandroid.practice.model.UserResponse
import com.yogeshandroid.practice.viewModel.MainViewModel
import com.yogeshandroid.practice.views.adapters.UsersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()
    lateinit var adapter: UsersAdapter
    lateinit var llm: LinearLayoutManager

    var myData: MutableList<UserResponse.User> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        initSetup()
        initPagination()

        mainViewModel.failure.observe(this) {
            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
        }

        mainViewModel.users.observe(this) {
            handleApiSuccess(it.users)
        }
    }

    private fun initPagination() {
        activityMainBinding.recyclerView.setOnScrollChangeListener { p0, p1, p2, p3, p4 ->
            val visibleItemCount: Int = llm.childCount
            val totalItemCount: Int = llm.itemCount
            val firstVisibleItemPosition: Int = llm.findFirstVisibleItemPosition()
//            if (!mainViewModel.isLastPage) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
                ) {
                    loadMoreItems()
                }
//            } else {
//                Toast.makeText(this@MainActivity, "We are at last page", Toast.LENGTH_SHORT)
//                    .show()
//            }
        }
    }

    private fun loadMoreItems() {
        mainViewModel.getUsers()
    }

    private fun handleApiSuccess(users: List<UserResponse.User>) {
        myData.addAll(users)
        adapter.notifyDataSetChanged()
    }

    private fun initSetup() {
        adapter = UsersAdapter(myData, this@MainActivity)
        activityMainBinding.recyclerView.adapter = adapter
        llm = LinearLayoutManager(this@MainActivity)
        activityMainBinding.recyclerView.layoutManager = llm
    }
}