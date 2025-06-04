package com.dkdevs.testing2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Plant")
data class PlantEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    var plant_id : Int = 0,
    val img_thumbnail : String = "",
    var big_img_url : String = "",
    var name : String = "",
    var scientific_name : String = "",
    var desc : String = "",
    var is_wishlisted : Boolean = false,
    var is_in_my_garden : Boolean = false,
    var watering : String = "",
    var sunlight : String = "",
    var soil : String = ""
)

data class NurturingDetail(
    var watering : String = "",
    var sunlight : String = "",
    var soil : String = ""
)