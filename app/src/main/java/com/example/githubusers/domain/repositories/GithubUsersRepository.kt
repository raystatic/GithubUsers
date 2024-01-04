package com.example.githubusers.domain.repositories

import com.example.githubusers.domain.models.GithubUser
import com.example.githubusers.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Created by Rahul Ray.
 * Version 1.0.0 on 04,January,2024
 */
interface GithubUsersRepository {

    suspend fun getUserByUserName(userName: String?): Flow<Resource<GithubUser>>

    suspend fun getFollowers(userName: String?): Flow<Resource<List<GithubUser>>>

    suspend fun getFollowings(userName: String?): Flow<Resource<List<GithubUser>>>

}