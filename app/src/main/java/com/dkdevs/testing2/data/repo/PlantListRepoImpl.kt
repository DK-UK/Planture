package com.dkdevs.testing2.data.repo

import androidx.lifecycle.LiveData
import com.dkdevs.testing2.data.local.PlantDatabase
import com.dkdevs.testing2.data.local.PlantEntity

class PlantListRepoImpl(private var db : PlantDatabase) : PlantListRepo {
    override suspend fun getWishlistOrMyGardenPlants(plantDestination: PlantDestinationType, whereValue: Boolean) : LiveData<List<PlantEntity>> {
        return if (plantDestination == PlantDestinationType.FromMyGarden) {
            db.plantDao().getMyGardenPlants(whereValue)
        }
        else {
            db.plantDao().getWishListPlants(whereValue)
        }
    }
}

sealed class PlantDestinationType() {
    object FromWishlist : PlantDestinationType()

    object FromMyGarden : PlantDestinationType()
}