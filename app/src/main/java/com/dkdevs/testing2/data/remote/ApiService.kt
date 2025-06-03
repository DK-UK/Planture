package com.dkdevs.testing2.data.remote

import com.dkdevs.testing2.BuildConfig
import com.dkdevs.testing2.data.models.Plant
import com.dkdevs.testing2.data.models.PlantDetails
import com.dkdevs.testing2.data.models.PlantIdentificationResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

object ApiService {

    const val IDENTIFICATION_BASE_URL = "https://my-api.plantnet.org/v2/"
    const val DETAILS_BASE_URL = "https://permapeople.org/api/"

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    fun getIdentifyApiService(): IdentifyApiRoutes {
        return Retrofit.Builder()
            .baseUrl(IDENTIFICATION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(IdentifyApiRoutes::class.java)

    }

    fun getDetailsApiService(): PlantDetailsApiRoutes {
        return Retrofit.Builder()
            .baseUrl(DETAILS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(PlantDetailsApiRoutes::class.java)

    }
}


interface IdentifyApiRoutes {

    @Multipart
    @POST("identify/{project}")
    suspend fun identify(@Path("project") project : String = "all", @Query("api-key") key : String = BuildConfig.IDENTIFY_KEY,
                         @Part images : List<MultipartBody.Part>) : PlantIdentificationResponse

}

interface PlantDetailsApiRoutes {
    @GET("plants")
    suspend fun getAllPlants(@Header("x-permapeople-key-id") keyId : String = BuildConfig.KEY_ID,
                             @Header("x-permapeople-key-secret") keySecret : String = BuildConfig.KEY_SECRET) : PlantDetails

    @POST("search")
    suspend fun searchPlant(@Header("x-permapeople-key-id") keyId : String = BuildConfig.KEY_ID,
                             @Header("x-permapeople-key-secret") keySecret : String = BuildConfig.KEY_SECRET,
                             @Query("q") plantName : String) : PlantDetails

    @GET("plants/{id}")
    suspend fun getPlant(@Header("x-permapeople-key-id") keyId : String = BuildConfig.KEY_ID,
                            @Header("x-permapeople-key-secret") keySecret : String = BuildConfig.KEY_SECRET,
                            @Path("id") id : Int) : Plant

}
sealed class ApiResponse {
    class Success(var data : Any?, var message : String? = null) : ApiResponse()
    class Failure(var data: Any? = null, var error : String) : ApiResponse()
    object Loading
}