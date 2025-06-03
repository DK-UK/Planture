package com.dkdevs.testing2.data.repo

import androidx.lifecycle.LiveData
import com.dkdevs.testing2.data.local.PlantEntity

interface PlantListRepo {

    suspend fun getWishlistOrMyGardenPlants(plantDestination: PlantDestinationType, whereValue: Boolean) : LiveData<List<PlantEntity>>
}