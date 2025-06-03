package com.dkdevs.testing2.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PlantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plantEntity: PlantEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlants(plants : List<PlantEntity>)

    @Upsert
    suspend fun updatePlant(plantEntity: PlantEntity)

    @Query("UPDATE plant set is_wishlisted=:isWishlisted where plant_id=:plantId")
    suspend fun updatePlantWishlistStatus(isWishlisted : Boolean, plantId : Int)

    @Query("UPDATE plant set is_in_my_garden=:addToMyGarden where plant_id=:plantId")
    suspend fun updatePlantToAddToMyGarden(addToMyGarden : Boolean, plantId : Int)

    @Query("DELETE from plant where plant_id=:plantId")
    suspend fun deletePlant(plantId : Int)

    @Query("SELECT * FROM plant where plant_id=:whereValue")
    fun getPlants(whereValue : Int) : PlantEntity

    @Query("SELECT * FROM plant where is_wishlisted=:whereValue")
    fun getWishListPlants(whereValue: Boolean) : LiveData<List<PlantEntity>>

    @Query("SELECT * FROM plant where is_in_my_garden=:whereValue")
    fun getMyGardenPlants(whereValue: Boolean) : LiveData<List<PlantEntity>>
}