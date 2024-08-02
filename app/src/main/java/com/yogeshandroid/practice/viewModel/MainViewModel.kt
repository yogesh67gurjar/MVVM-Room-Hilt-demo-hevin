package com.yogeshandroid.practice.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yogeshandroid.practice.model.UserResponse
import com.yogeshandroid.practice.repository.MainRepository
import com.yogeshandroid.practice.utils.ApiListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(var mainRepository: MainRepository) : ViewModel() {

    private var pageNumber = 1
    private val limit = 10;
    var isLastPage: Boolean = false

    private var _users: MutableLiveData<UserResponse> = MutableLiveData()
    var users: LiveData<UserResponse> = _users

    private var _failure: MutableLiveData<String> = MutableLiveData()
    var failure: LiveData<String> = _failure

    private var apiListener: ApiListener = object : ApiListener {
        override fun onSuccess(any: Any, type: String) {
            _users.postValue(any as UserResponse)

            isLastPage = pageNumber < any.total / 10

            Log.d("~~~111~~~", " page is ${pageNumber}  ,  total/10 is ${any.total / 10}  ,  isNext ${isLastPage}")
        }

        override fun onFailure(error: String) {
            _failure.postValue(error)
        }
    }

    init {
        mainRepository.getUsers(limit * (pageNumber - 1), limit, apiListener)
    }

    fun getUsers() {
        pageNumber += 1;
        mainRepository.getUsers(limit * (pageNumber - 1), limit, apiListener)
    }
}