package com.yogeshandroid.practice.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yogeshandroid.practice.model.UserResponse
import com.yogeshandroid.practice.repository.MainRepository
import com.yogeshandroid.practice.utils.ApiListener
import com.yogeshandroid.practice.utils.SortBy
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private var mainRepository: MainRepository) : ViewModel() {

    val searchBy: MutableLiveData<String> = MutableLiveData("")
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    var sortBy: MutableLiveData<SortBy> = MutableLiveData(SortBy.None)

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
            isLastPage = pageNumber - 1 > any.total / 10
            isLoading.postValue(false)
        }

        override fun onFailure(error: String) {
            _failure.postValue(error)
            isLoading.postValue(false)
        }
    }

    init {
        mainRepository.getUsers(limit * (pageNumber - 1), limit, apiListener)
    }

    fun getUsers() {
        pageNumber += 1;
        mainRepository.getUsers(limit * (pageNumber - 1), limit, apiListener)
        isLoading.postValue(true)
    }
}