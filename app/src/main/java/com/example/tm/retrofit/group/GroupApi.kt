package com.example.tm.retrofit.group

import com.example.tm.entity.Group
import com.example.tm.entity.GroupExtended
import com.example.tm.entity.Task
import com.example.tm.entity.TaskGroup
import com.example.tm.retrofit.task.InviteGroup
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupApi {
    @POST("tm/group/join/{inviteToken}")
    suspend fun joinGroup(
        @Path("inviteToken") inviteToken: String,
        @Header("Authorization") token: String
    ): Response<String>

    @POST("tm/group/add")
    suspend fun addGroup(
        @Body group: Group,
        @Header("Authorization") token: String
    ): Response<Group>

    @GET("tm/group/creator")
    suspend fun  getGroupCreator(@Header("Authorization") inviteToken: String): Response<List<Group>>

    @GET("tm/group/user")
    suspend fun  getGroupUser(@Header("Authorization") inviteToken: String): Response<List<Group>>

    @POST("tm/group/generate-invite/{groupId}")
    suspend fun generateInviteToken(@Path("groupId") groupId: String): Response<InviteGroup>

    @GET("tm/group/getGroupId/{groupId}")
    suspend fun getGroupById(@Path("groupId") groupId: String): Response<GroupExtended>

    @POST("tm/group/addTaskGroup")
    suspend fun addGroupTask(@Body taskGroup: TaskGroup): Response<TaskGroup>

    @GET("tm/group/allTasksGroup")
    suspend fun getAllTasksGroup(@Query("groupId") groupId: String): Response<List<TaskGroup>>

    @GET("tm/group/getTaskGroup/{taskGroupId}")
    suspend fun getTaskGroupById(@Path("taskGroupId") taskGroupId: String): Response<TaskGroup>

    @PUT("tm/group/update/{taskGroupId}")
    suspend fun updateTask(@Path("taskGroupId") taskGroupId: String, @Body taskGroup: TaskGroup): Response<TaskGroup>

    @DELETE("tm/group/deleteGroup/{groupId}")
    suspend fun deleteGroup(@Path("groupId") groupId: String): Response<Void>

    @DELETE("tm/group/deleteTaskGroup/{taskGroupId}")
    suspend fun deleteTaskGroup(@Path("taskGroupId") taskGroupId: String): Response<Void>


}
