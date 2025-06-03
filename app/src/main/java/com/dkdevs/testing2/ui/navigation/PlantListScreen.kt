package com.dkdevs.testing2.ui.navigation

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dkdevs.testing2.data.repo.PlantDestinationType
import com.dkdevs.testing2.ui.uiComponents.MyAppTopBar
import com.dkdevs.testing2.ui.vm.PlantListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlantListScreen(
    modifier: Modifier = Modifier,
    getPlantsFrom: PlantDestinationType,
    onPlantClicked: (Int) -> Unit
) {

    val vm : PlantListViewModel = koinViewModel()
    val listUi = vm.plants.collectAsStateWithLifecycle().value

    var showMoreOption by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = getPlantsFrom) {
        vm.getPlant(getPlantsFrom, true)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            if (getPlantsFrom == PlantDestinationType.FromWishlist){
                MyAppTopBar(title = "Wishlist")
            }
            else {
                MyAppTopBar(title = "My Garden",
                    postIcons = {
                    })
            }
        }
    ) {

        if (listUi.loading) {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else if (listUi.error.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                Text(text = listUi.error, fontWeight = FontWeight.Normal,
                    fontSize = 16.sp)
            }
        }
        else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(listUi.plants) {
                    plantItem(plantName = it.name, plantScientificName = it.scientific_name, plantImg = it.alt_img_url) {
                        onPlantClicked.invoke(it.plant_id)
                    }
                }
            }
        }
    }
}