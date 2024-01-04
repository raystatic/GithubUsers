package com.example.githubusers.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubusers.domain.models.GithubUser
import com.example.githubusers.domain.repositories.GithubUsersRepository
import com.example.githubusers.utils.Resource
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Created by Rahul Ray.
 * Version 1.0.0 on 04,January,2024
 */
@HiltViewModel
class GithubUserViewModel @Inject constructor(
    private val repository: GithubUsersRepository
): ViewModel() {

    var githubUserResponse = MutableLiveData<Resource<GithubUser?>>(Resource.none())
        private set

    var followers = MutableLiveData<Resource<List<GithubUser>?>>(Resource.none())
        private set

    var followings = MutableLiveData<Resource<List<GithubUser>?>>(Resource.none())
        private set

    fun getUserByUserName(userName: String?) = viewModelScope.launch{
        repository.getUserByUserName(userName)
            .onEach {
                githubUserResponse.postValue(it)
            }.launchIn(viewModelScope)
    }

    fun getFollowers(userName: String?) = viewModelScope.launch{
        repository.getFollowers(userName)
            .onEach {
                followers.postValue(it)
            }.launchIn(viewModelScope)
    }

    fun getFollowings(userName: String?) = viewModelScope.launch{
        repository.getFollowings(userName)
            .onEach {
                followings.postValue(it)
            }.launchIn(viewModelScope)
    }

}