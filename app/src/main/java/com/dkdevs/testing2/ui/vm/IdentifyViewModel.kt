package com.dkdevs.testing2.ui.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dkdevs.testing2.data.models.Plant
import com.dkdevs.testing2.data.repo.IdentificationRepoImpl
import com.dkdevs.testing2.data.repo.PlantDetailsRepoImpl
import com.dkdevs.testing2.ui.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.HttpException


data class IdentifyUi(
    var plantDetails: List<Plant> = emptyList(),
    var error: String = "",
    var loading: Boolean = false
)

class IdentifyViewModel(
    private val appContext: Application,
    private val identificationRepoImpl: IdentificationRepoImpl,
    private val plantDetailsRepoImpl: PlantDetailsRepoImpl,
) : AndroidViewModel(appContext) {

    private val _plantDetails: MutableStateFlow<IdentifyUi> = MutableStateFlow(IdentifyUi())
    val plantDetails: StateFlow<IdentifyUi> = _plantDetails
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = IdentifyUi()
        )

    // placed in init block because it was calling every time
    // in LaunchEffect while changing tab

    fun identifyPlant(multipart: MultipartBody.Part) {
        viewModelScope.launch {
            _plantDetails.value = IdentifyUi(loading = true)

            if (Utils.isConnected(appContext.applicationContext)) {
                try {
                    val response = identificationRepoImpl.identify(listOf(multipart))
                    Log.e("Dhaval", "identifyPlant: ${response.bestMatch}")
                    if (response.bestMatch.isNotEmpty()) {
                        searchPlant(response.bestMatch)
                    }
                } catch (e: HttpException) {
                    Log.e(
                        "Dhaval",
                        "identifyPlant: ERROR : ${e.response()} --- ${e.message()} --- ${e.cause}",
                    )
                    if (e.code() == 404){
                        _plantDetails.value = IdentifyUi(error = "Image not found!")
                    }
                    else {
                        _plantDetails.value = IdentifyUi(error = "Something went wrong!")
                    }
                }
            } else {
                _plantDetails.value = IdentifyUi(error = "No Internet connection!")
            }
        }
    }

    fun searchPlant(plantName: String) {
        viewModelScope.launch {

            if (Utils.isConnected(appContext.applicationContext)) {
                var plants = plantDetailsRepoImpl.searchPlant(plantName)
                Log.e("Dhaval", "searchPlant: PLANTS : ${plants.plants.toString()}", )
                if (plants.plants.isNotEmpty()) {
                    _plantDetails.update {
                        it.copy(plantDetails = plants.plants, loading = false, error = "")
                    }
                } else {
                    _plantDetails.update {
                        it.copy(plantDetails = emptyList(), error = "", loading = false)
                    }
                }
            } else {
                _plantDetails.value = IdentifyUi(error = "No Internet connection!")
            }
        }
    }

    fun clearData() {
        _plantDetails.value = IdentifyUi(plantDetails = emptyList())
    }
}