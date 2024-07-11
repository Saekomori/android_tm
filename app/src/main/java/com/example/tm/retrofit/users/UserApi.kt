package com.example.tm.retrofit.users

import com.example.tm.retrofit.ApiResponse
import com.example.tm.retrofit.users.AuthRequest
import com.example.tm.retrofit.users.RegRequest
import com.example.tm.retrofit.users.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @POST("v1/auth/authenticate")
    suspend fun auth(@Body authRequest: AuthRequest): Response<User>

    @POST("v1/auth/register")
    suspend fun reg(@Body regRequest: RegRequest): Response<User>

    @POST("v1/auth/validate")
    suspend fun validateToken(@Query("token") token: String): Response<ApiResponse>

    @POST("v1/auth/getLink")
    suspend fun getLink(@Header("Authorization") token: String): Response<LinkBot>
}