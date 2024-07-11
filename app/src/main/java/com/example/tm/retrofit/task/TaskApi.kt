package com.example.tm.retrofit.task

import com.example.tm.entity.Task
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApi {
    @GET("tm/task/allTasksUser")
    suspend fun allTasks(@Header("Authorization") inviteToken: String): Response<List<Task>>

    @GET("tm/task/getTaskId/{taskId}")
    suspend fun getTaskById(@Path("taskId") taskId: String): Response<Task>

    @POST("tm/task/add")
    suspend fun addTask(@Header("Authorization") inviteToken: String, @Body task: Task): Response<Task>

    @PUT("tm/task/update/{taskId}")
    suspend fun updateTask(@Path("taskId") taskId: String, @Body task: Task): Response<Task>

    @DELETE("tm/task/delete/{taskId}")
    suspend fun deleteTask(@Path("taskId") taskId: String): Response<Void>


}