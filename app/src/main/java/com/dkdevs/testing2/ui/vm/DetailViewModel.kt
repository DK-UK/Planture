package com.dkdevs.testing2.ui.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dkdevs.testing2.data.local.PlantEntity
import com.dkdevs.testing2.data.repo.PlantDetailsRepoImpl
import com.dkdevs.testing2.ui.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailUi(
    var plants: PlantEntity = PlantEntity(),
    var error: String = "",
    var loading: Boolean = false
)

class DetailViewModel(
    private val appContext: Application,
    private val plantDetailsRepoImpl: PlantDetailsRepoImpl
) : AndroidViewModel(appContext) {

    private val _plants: MutableStateFlow<DetailUi> = MutableStateFlow(DetailUi())
    val plants: StateFlow<DetailUi> = _plants
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DetailUi()
        )

    // placed in init block because it was calling every time
    // in LaunchEffect while changing tab


    fun getPlant(plantId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _plants.value = DetailUi(loading = true)

            if (Utils.isConnected(appContext.applicationContext)) {
                var plants = plantDetailsRepoImpl.getPlant(plantId)
                Log.e("Dhaval", "getPlant: PLANTS : ${plants}")

                if (plants.plant_id > 0) {
                    _plants.update {
                        it.copy(plants = plants, loading = false, error = "")
                    }
                } else {
                    _plants.update {
                        it.copy(
                            plants = PlantEntity(),
                            error = "No plant(s) found",
                            loading = false
                        )
                    }
                }
            } else {
                _plants.value = DetailUi(error = "No Internet connection!")
            }
        }
    }

    fun addPlantToWishlist(isWishlist : Boolean, plants: PlantEntity) {
        viewModelScope.launch {

            if (isWishlist){
                plantDetailsRepoImpl.addPlantToWishlist(plants.copy(is_wishlisted = true))
            }
            else {
                plantDetailsRepoImpl.removePlantToWishlist(plants.plant_id)
            }
        }
    }

    fun addPlantToMyGarden(addToMyGarden : Boolean, isWishlist: Boolean, plants: PlantEntity) {
        viewModelScope.launch {

            if (addToMyGarden){
                if (isWishlist) {
                    plantDetailsRepoImpl.updatePlantToAddToMyGarden(true, plants.plant_id)
                }
                else {
                    plantDetailsRepoImpl.addPlantToMyGarden(plants.copy(is_in_my_garden = true))
                }
            }
            else {
                plantDetailsRepoImpl.removePlantFromMyGarden(plants.plant_id)
            }
        }
    }


}