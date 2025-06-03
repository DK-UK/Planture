package com.dkdevs.testing2.data.repo

import com.dkdevs.testing2.data.models.PlantIdentificationResponse
import okhttp3.MultipartBody

interface PlantIdentificationRepo {
    suspend fun identify(imgFile : List<MultipartBody.Part>) : PlantIdentificationResponse
}