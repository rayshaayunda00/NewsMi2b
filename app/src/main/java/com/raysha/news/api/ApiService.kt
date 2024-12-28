package com.raysha.news.api

import com.raysha.news.models.BeritaResponse
import com.raysha.news.models.LoginRequest
import com.raysha.news.models.LoginResponse
import com.raysha.news.models.RegisterRequest
import com.raysha.news.models.RegisterResponse
import com.raysha.news.models.TambahBeritaResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiService {
    @FormUrlEncoded
    @POST("API_BASIC/register.php")
    fun register(
        @Field("username") username:String,
        @Field("password") password:String,
        @Field("fullname") fullname:String,
        @Field("email") email:String,
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("API_BASIC/login.php")
    fun login (
        @Field("username") username: String,
        @Field("password") password: String,
    ): Call<LoginResponse>
    @GET("API_BASIC/get_berita.php")
    fun getListBerita(@Query("judul")judul: String): Call<BeritaResponse>

    @Multipart
    @POST("api_basic/add_berita.php")
    fun addBerita(
        @Part("judul") judul: RequestBody,
        @Part("isiBerita") isiBerita: RequestBody,
        @Part fileGambar: MultipartBody.Part
    ): Call<TambahBeritaResponse>

}