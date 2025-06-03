package com.dkdevs.testing2.data.repo

import com.dkdevs.testing2.data.local.PlantEntity
import com.dkdevs.testing2.data.models.Plant
import com.dkdevs.testing2.data.models.PlantDetails

interface PlantDetailsRepo {
    suspend fun getAllPlants() : PlantDetails

    suspend fun searchPlant(plantName : String) : PlantDetails

    suspend fun getPlant(plantId : Int) : PlantEntity

    suspend fun addPlantToWishlist(plant : PlantEntity)

    suspend fun addPlantToMyGarden(plant : PlantEntity)

    suspend fun removePlantToWishlist(plantId: Int)

    suspend fun removePlantFromMyGarden(plantId: Int)
}