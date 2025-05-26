package com.upsaclay.common.data.remote.api

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface WhiteListApi {
    @FormUrlEncoded
    @POST("white-list/user")
    suspend fun isUserWhiteListed(@Field("userEmail") email: String): Response<Boolean>
}