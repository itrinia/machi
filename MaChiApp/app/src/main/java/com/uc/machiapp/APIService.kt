package com.uc.machiapp

import com.uc.machiapp.model.User
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    // LOGIN ~ POST - Format -->  Raw JSON

    @POST("/login")
    suspend fun MasukLogin(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("/register")
    suspend fun BuatRegister(@Body requestBody: RequestBody): Response<ResponseBody>

    @GET("/users")
    suspend fun getUsers(): Response<ResponseBody>

    //Fungsi Lainnya ada di Inner Activity fragment_profile dan fragment_home
    //user/{id}--> frag_profile & frag_home

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: User): Call<User>




}