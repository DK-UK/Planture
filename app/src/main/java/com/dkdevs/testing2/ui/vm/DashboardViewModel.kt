package com.dkdevs.testing2.ui.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dkdevs.testing2.R
import com.dkdevs.testing2.data.models.Plant
import com.dkdevs.testing2.data.repo.PlantDetailsRepoImpl
import com.dkdevs.testing2.ui.utility.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardUi(
    var plants: List<Plant> = emptyList(),
    var error: String = "",
    var loading: Boolean = false
)

class DashboardViewModel(
    private val appContext: Application,
    private val plantDetailsRepoImpl: PlantDetailsRepoImpl
) : AndroidViewModel(appContext) {

    private val _plants: MutableStateFlow<DashboardUi> = MutableStateFlow(DashboardUi())
    val plants: StateFlow<DashboardUi> = _plants
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardUi()
        )

    var backupPlants : List<Plant> = emptyList()

    // placed in init block because it was calling every time
    // in LaunchEffect while changing tab
    init {
        getAllPlants()
    }

    fun getAllPlants(plantList : List<Plant> = emptyList(), lastId : Int = 0) {
        viewModelScope.launch {
            _plants.value = DashboardUi(loading = true)

            if (Utils.isConnected(appContext.applicationContext)) {
                try {

                    var plants = plantDetailsRepoImpl.getAllPlants(lastId)
                    if (plants.plants.isNotEmpty()) {
                        val completeList = plantList + plants.plants
                        backupPlants = completeList

                        _plants.update {
                            it.copy(plants = if (plantList.isEmpty()) plants.plants else completeList, loading = false, error = "")
                        }
                    } else {
                        _plants.update {
                            it.copy(plants = emptyList(), error = appContext.getString(R.string.no_plants_found), loading = false)
                        }
                    }
                }
                catch (e : Exception) {
                    Log.e("Dhaval", "getAllPlants: EXCEPTION : ${e.toString()}", )
                    _plants.update {
                        it.copy(plants = emptyList(), error = appContext.getString(R.string.something_went_wrong), loading = false)
                    }
                }

            } else {
                _plants.value = DashboardUi(error = appContext.getString(R.string.no_internet_connection))
            }
        }
    }

   /* fun getPlant(plantId: Int) {
        viewModelScope.launch {
            _plants.value = DashboardUi(loading = true)

            if (Utils.isConnected(appContext.applicationContext)) {
                var plants = plantDetailsRepoImpl.getPlant(plantId)
                Log.e("Dhaval", "getPlant: PLANTS : ${plants.plants}", )

                Log.e("Dhaval", "getPlant: PLANTS : ${plants.plants}", )
                if (plants.plants.isNotEmpty()) {
                    _plants.update {
                        it.copy(plants = plants.plants, loading = false, error = "")
                    }
                } else {
                    _plants.update {
                        it.copy(plants = emptyList(), error = "No plant(s) found", loading = false)
                    }
                }
            } else {
                _plants.value = DashboardUi(error = "No Internet connection!")
            }
        }
    }
*/

    fun searchPlant(plantName: String) {
        viewModelScope.launch {
            _plants.value = DashboardUi(loading = true)

            if (Utils.isConnected(appContext.applicationContext)) {
                var plants = plantDetailsRepoImpl.searchPlant(plantName)
                if (plants.plants.isNotEmpty()) {

                    _plants.update {
                        it.copy(plants = plants.plants, loading = false, error = "")
                    }
                } else {
                    _plants.update {
                        it.copy(plants = emptyList(), error = appContext.getString(R.string.no_plants_found), loading = false)
                    }
                }
            } else {
                _plants.value = DashboardUi(error = appContext.getString(R.string.no_internet_connection))
            }
        }
    }

    fun getBackupPlants() {
        _plants.value = DashboardUi(plants = backupPlants)
    }
}