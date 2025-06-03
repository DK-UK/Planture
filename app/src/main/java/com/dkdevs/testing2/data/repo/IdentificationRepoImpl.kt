package com.dkdevs.testing2.data.repo

import com.dkdevs.testing2.data.models.PlantIdentificationResponse
import com.dkdevs.testing2.data.remote.IdentifyApiRoutes
import okhttp3.MultipartBody

class IdentificationRepoImpl(private val apiService : IdentifyApiRoutes) : PlantIdentificationRepo {

    override suspend fun identify(
        imgFile: List<MultipartBody.Part>
    ): PlantIdentificationResponse = apiService.identify(images = imgFile)



}