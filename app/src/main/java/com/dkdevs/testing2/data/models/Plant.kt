package com.dkdevs.testing2.data.models

import com.dkdevs.testing2.data.local.PlantEntity

data class Plant(
    var adopter_id: Int = 0,
    var created_at: String = "",
    var `data`: List<Data> = listOf(),
    var description: String = "",
    var id: Int = 0,
    var images: Images = Images(),
    var link: String = "",
    var name: String = "",
    var parent_id: Any = Any(),
    var scientific_name: String = "",
    var slug: String = "",
    var type: String = "",
    var updated_at: String = "",
    var version: Int = 0
)

fun Plant.toPlantEntity() = PlantEntity(
    plant_id = this.id,
    name = this.name,
    scientific_name = this.scientific_name,
    desc = this.description,
    alt_img_url = this.images.thumb
)