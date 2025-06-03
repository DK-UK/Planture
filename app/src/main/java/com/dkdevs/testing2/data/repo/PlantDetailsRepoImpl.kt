package com.dkdevs.testing2.data.repo

import android.util.Log
import com.dkdevs.testing2.data.local.PlantDatabase
import com.dkdevs.testing2.data.local.PlantEntity
import com.dkdevs.testing2.data.models.Plant
import com.dkdevs.testing2.data.models.PlantDetails
import com.dkdevs.testing2.data.models.toPlantEntity
import com.dkdevs.testing2.data.remote.PlantDetailsApiRoutes
import com.dkdevs.testing2.ui.Utils

class PlantDetailsRepoImpl(private var apiService : PlantDetailsApiRoutes,
    private var db : PlantDatabase) : PlantDetailsRepo {
    override suspend fun getAllPlants(): PlantDetails {
        return apiService.getAllPlants()
    }

    override suspend fun searchPlant(
        plantName: String
    ): PlantDetails {
        return apiService.searchPlant(plantName = plantName)
    }

    override suspend fun getPlant(plantId: Int): PlantEntity {
        // check is plant exists in DB, if not then call the API
        val plantFromDb = db.plantDao().getPlants(plantId)
        Log.e("Dhaval", "getPlant: FROM DB : ${plantFromDb}", )

        return if (plantFromDb != null && plantFromDb.name.isNotEmpty()) {
            plantFromDb
        } else {
            val plant = apiService.getPlant(id = plantId)
            Utils.apiToLocalDb(plant)
        }
    }

    override suspend fun addPlantToWishlist(plant: PlantEntity) {
        db.plantDao().insertPlant(plant)
    }

    override suspend fun addPlantToMyGarden(plant: PlantEntity) {
        db.plantDao().insertPlant(plant)
    }

    override suspend fun removePlantToWishlist(plantId: Int) {
        db.plantDao().deletePlant(plantId)
    }

    override suspend fun removePlantFromMyGarden(plantId: Int) {
        db.plantDao().deletePlant(plantId)
    }
}