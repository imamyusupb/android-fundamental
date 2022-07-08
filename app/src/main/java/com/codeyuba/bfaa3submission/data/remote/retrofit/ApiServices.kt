package com.codeyuba.bfaa3submission.data.remote.retrofit

import com.codeyuba.bfaa3submission.BuildConfig
import com.codeyuba.bfaa3submission.data.remote.response.UserItem
import com.codeyuba.bfaa3submission.data.remote.response.UserListResponse
import com.codeyuba.bfaa3submission.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun searchUsers(
        @Query("q") username: String
    ): Call<UserListResponse>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun getUser(
        @Path("username") username: String
    ): Call<UserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<UserItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<UserItem>>
}