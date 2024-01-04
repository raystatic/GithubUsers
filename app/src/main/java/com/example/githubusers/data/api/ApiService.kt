package com.example.githubusers.data.api

import com.example.githubusers.domain.models.GithubUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Rahul Ray.
 * Version 1.0.0 on 04,January,2024
 */
interface ApiService {

    companion object {
        const val BASEURL = "https://api.github.com/"
        const val AUTH_TOKEN = "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"
    }

    @GET("/users/{username}")
    suspend fun getUserByUserName(
        @Path("username") userName: String?
    ): Response<GithubUser>

}