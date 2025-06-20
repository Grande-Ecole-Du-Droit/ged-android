package com.upsaclay.common.data.remote.api

import com.upsaclay.common.data.UserField.Oracle.USER_ID
import com.upsaclay.common.data.UserField.Oracle.USER_PROFILE_PICTURE_FILE_NAME
import com.upsaclay.common.data.remote.OracleUser
import com.upsaclay.common.data.remote.ServerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

internal interface UserOracleApi {
    @POST("users/create")
    suspend fun createUser(@Body user: OracleUser): Response<ServerResponse>

    @FormUrlEncoded
    @PUT("users/profile-picture-file-name")
    suspend fun updateProfilePictureFileName(
        @Field(USER_ID) userId: String,
        @Field(USER_PROFILE_PICTURE_FILE_NAME) userProfilePictureFileName: String
    ): Response<ServerResponse>

    @DELETE("users/profile-picture-file-name/{userId}")
    suspend fun deleteProfilePictureFileName(@Path("userId") userId: String): Response<ServerResponse>
}