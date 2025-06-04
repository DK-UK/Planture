package com.dkdevs.testing2.ui.vm
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.dkdevs.testing2.data.local.PlantEntity
import com.dkdevs.testing2.data.repo.PlantDestinationType
import com.dkdevs.testing2.data.repo.PlantListRepoImpl
import com.dkdevs.testing2.ui.utility.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PlantListUi(
    var plants: List<PlantEntity> = emptyList(),
    var error: String = "",
    var loading: Boolean = false
)

class PlantListViewModel(
    private val appContext: Application,
    private val plantListRepoImpl: PlantListRepoImpl,
) : AndroidViewModel(appContext) {

    private val _plants: MutableStateFlow<PlantListUi> = MutableStateFlow(PlantListUi())
    val plants: StateFlow<PlantListUi> = _plants
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlantListUi()
        )

    // placed in init block because it was calling every time
    // in LaunchEffect while changing tab


    fun getPlant(getPlantsFrom : PlantDestinationType, value : Boolean) {
        viewModelScope.launch {
            _plants.value = PlantListUi(loading = true)

            if (Utils.isConnected(appContext.applicationContext)) {
                Log.e("Dhaval", "GETPLANTFROM : ${getPlantsFrom}", )
                var plants = plantListRepoImpl.getWishlistOrMyGardenPlants(getPlantsFrom, value).asFlow()
                Log.e("Dhaval", "getPlant: PLANTS : ${plants}")

                plants.collect {
                    Log.e("Dhaval", "PLANTLIST : ${it}", )
                    if (it.isNotEmpty()){
                        _plants.value = PlantListUi(plants = it, loading = false, error = "")

                    }
                    else {
                        _plants.update {
                            it.copy(
                                plants = emptyList(),
                                error = "No plant(s) found",
                                loading = false
                            )
                        }
                    }
                }
            } else {
                _plants.value = PlantListUi(error = "No Internet connection!")
            }
        }
    }
}