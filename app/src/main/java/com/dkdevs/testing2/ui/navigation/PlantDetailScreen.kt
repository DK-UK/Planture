package com.dkdevs.testing2.ui.navigation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.dkdevs.testing2.R
import com.dkdevs.testing2.ui.theme.Testing2Theme
import com.dkdevs.testing2.ui.uiComponents.DialogBox
import com.dkdevs.testing2.ui.uiComponents.MyAppTopBar
import com.dkdevs.testing2.ui.vm.DetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlantDetailScreen(
    modifier: Modifier = Modifier,
    plantId: Int,
    onBackPressed: () -> Unit
) {

    val vm: DetailViewModel = koinViewModel()
    val detailUi = vm.plants.collectAsStateWithLifecycle().value

    var isWishlist by remember(key1 = detailUi.plants.is_wishlisted){
        mutableStateOf(detailUi.plants.is_wishlisted)
    }

    var isInMyGarden by remember (key1 = detailUi.plants.is_in_my_garden) {
        mutableStateOf(detailUi.plants.is_in_my_garden)
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    var showMoreOption by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(plantId) {
        Log.e("Dhaval", "PLANT ID : ${plantId}")
        if (plantId > 0) {
            vm.getPlant(plantId)
        }
    }


    Scaffold(
        topBar = {
            MyAppTopBar(title = "Details",
                isBackAvailable = true,
                onBackPressed = {
                    onBackPressed.invoke()
                }, postIcons = {
                    if (isInMyGarden){
                        Column {
                            Icon(Icons.Default.MoreVert, contentDescription = "More options",
                                modifier = Modifier.clickable {
                                    showMoreOption = true
                                })
                            if (showMoreOption){
                                DropdownMenu(expanded = showMoreOption, onDismissRequest = { showMoreOption = false }) {
                                    DropdownMenuItem(text = { Text(text = "Remove from garden") }, onClick = {
                                        showMoreOption = false
                                        vm.addPlantToMyGarden(addToMyGarden = false, detailUi.plants)
                                        isInMyGarden = false
                                    })
                                }
                            }
                        }
                    }
                })
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (!isInMyGarden) {
                ExtendedFloatingActionButton(
                    text = {
                        Text(text = "Add to My Garden")
                    },
                    icon = { /*TODO*/ },
                    onClick = {
                        showDialog = true
                    })
            }
        }
    ) {
        if (showDialog) {
            Box(
                modifier = Modifier.fillMaxSize()
            ){
                DialogBox(message = "This plant will be add to your garden. Are you sure ?", show = showDialog, onDismiss = { showDialog = !showDialog }) {
                    showDialog = !showDialog
                    isInMyGarden = true
                    vm.addPlantToMyGarden(true, detailUi.plants)
                }
            }
        }

        if (detailUi.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (detailUi.error.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.Center
            ) {
                Text(text = detailUi.error)
            }
        } else if (detailUi.plants.plant_id > 0) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState())
                    .padding(it)
                    .padding(horizontal = 16.dp, vertical = 50.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Box {
                    AsyncImage(
                        model = detailUi.plants.img_path.ifEmpty { detailUi.plants.alt_img_url },
                        contentDescription = "${detailUi.plants.name}'s image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (!detailUi.plants.is_in_my_garden) {
                        Icon(imageVector = if (isWishlist) Icons.Default.Favorite
                        else Icons.Default.FavoriteBorder, contentDescription = "wishlist icon",
                            modifier = Modifier
                                .clickable {
                                    isWishlist = !isWishlist
                                    vm.addPlantToWishlist(isWishlist, detailUi.plants)
                                }
                                .align(Alignment.BottomEnd))

                    }
                }

                Text(
                    text = detailUi.plants.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = detailUi.plants.desc,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground
                )


                Text(
                    text = "Nurturing",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(corner = CornerSize(5.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_droplet),
                            contentDescription = null,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                    Text(
                        text = detailUi.plants.watering,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                    )
                }


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(corner = CornerSize(5.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_sunlight),
                            contentDescription = null,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                    Text(
                        text = detailUi.plants.sunlight,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(corner = CornerSize(5.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_plant),
                            contentDescription = null,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                    Text(
                        text = detailUi.plants.soil,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                    )
                }


            }

            /* LazyColumn(
                 modifier = Modifier
                     .fillMaxWidth()
                     .height((40 * detailUi.plants.data.size).dp),
                 userScrollEnabled = false,
                 state = LazyListState(),
             ) {
                 items(detailUi.plants.data) {
                     Row(
                         modifier = Modifier.fillMaxWidth(),
                         verticalAlignment = Alignment.CenterVertically,
                         horizontalArrangement = Arrangement.spacedBy(10.dp)
                     ) {

                         Text(text = it.key)

                         Text(text = it.value)
                     }
                 }
             }*/
        } else {
            Box(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No plant found!")
            }
        }
    }
}

@Preview
@Composable
private fun prevDetailScreen() {

    Testing2Theme {
        Scaffold(
            floatingActionButton = {
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = " image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .clip(RoundedCornerShape(CornerSize(7.dp)))
                            .align(Alignment.TopCenter)
                    )

                    Icon(
                        imageVector = Icons.Default.Favorite, contentDescription = "wishlist icon",
                        modifier = Modifier
                            .align(
                                Alignment.BottomEnd
                            )
                            .padding(10.dp)
                    )
                }
            }

        }
    }
}