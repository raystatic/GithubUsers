package com.example.githubusers.data.repositories

import com.example.githubusers.data.api.ApiService
import com.example.githubusers.domain.models.GithubUser
import com.example.githubusers.domain.repositories.GithubUsersRepository
import com.example.githubusers.utils.Constants
import com.example.githubusers.utils.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by Rahul Ray.
 * Version 1.0.0 on 04,January,2024
 */
class GithubUsersRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): GithubUsersRepository {

    override suspend fun getUserByUserName(userName: String?): Flow<Resource<GithubUser>> = flow {
        emit(Resource.loading())
        val response = apiService.getUserByUserName(userName)
        if (response.isSuccessful) {
            emit(Resource.success(response.body()))
        } else {
            emit(Resource.error(Constants.SOMETHING_WENT_WRONG, null))
        }
    }
}