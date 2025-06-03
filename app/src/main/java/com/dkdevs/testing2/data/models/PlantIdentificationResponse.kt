package com.dkdevs.testing2.data.models

data class PlantIdentificationResponse(
    var bestMatch: String = "",
    var remainingIdentificationRequests: Int = 0,
)