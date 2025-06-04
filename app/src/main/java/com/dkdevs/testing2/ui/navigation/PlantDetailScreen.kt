package com.dkdevs.testing2.ui.navigation

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
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
import com.dkdevs.testing2.ui.theme.plantColors
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

    val screenHeight = LocalConfiguration.current.screenHeightDp

    var isWishlist by remember(key1 = detailUi.plants.is_wishlisted) {
        mutableStateOf(detailUi.plants.is_wishlisted)
    }

    val scale by animateFloatAsState(
        targetValue = if (isWishlist) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        )
    )


    var isInMyGarden by remember(key1 = detailUi.plants.is_in_my_garden) {
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
        modifier = Modifier
            .statusBarsPadding(),
        topBar = {
            MyAppTopBar(
                title = "Details",
                isBackAvailable = true,
                onBackPressed = {
                    onBackPressed.invoke()
                }, postIcons = {
                    if (isInMyGarden) {
                        Column {
                            Icon(
                                Icons.Default.MoreVert, contentDescription = "More options",
                                tint = Color.White,
                                modifier = Modifier.clickable {
                                    showMoreOption = true
                                })

                            DropdownMenu(
                                expanded = showMoreOption,
                                onDismissRequest = { showMoreOption = false }) {
                                DropdownMenuItem(
                                    text = { Text(text = "Remove from garden") },
                                    onClick = {
                                        showMoreOption = false
                                        vm.addPlantToMyGarden(
                                            addToMyGarden = false,
                                            isWishlist = false,
                                            plants = detailUi.plants
                                        )
                                        isInMyGarden = false
                                    })
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
            ) {
                DialogBox(
                    title = if (isWishlist) "Are you sure ?" else "This plant will be add to your garden. Are you sure ?",
                    message = if (isWishlist) "This plant will be removed from wishlist and will add to the My garden." else null,
                    show = showDialog,
                    onDismiss = { showDialog = !showDialog }) {
                    showDialog = !showDialog
                    isInMyGarden = true
                    vm.addPlantToMyGarden(true, isWishlist, detailUi.plants)
                }
            }
        }

        if (detailUi.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.plantColors.cardBackground)
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
                    .verticalScroll(state = rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Box {
                    AsyncImage(
                        model = detailUi.plants.big_img_url,
                        contentDescription = "${detailUi.plants.name}'s image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                            .height(((screenHeight / 2) - 100).dp)
                    )

                    if (!isInMyGarden) {

                        IconButton(
                            onClick = {
                                isWishlist = !isWishlist
                                vm.addPlantToWishlist(isWishlist, detailUi.plants)
                            },
                            modifier = Modifier
                                .scale(scale)
                                .align(Alignment.BottomEnd)
                        ) {
                            Icon(
                                imageVector = if (isWishlist) Icons.Default.Favorite
                                else Icons.Default.FavoriteBorder,
                                contentDescription = "wishlist icon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .padding(horizontal = 16.dp, vertical = 50.dp),
                ) {

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
                        fontSize = 16.sp,
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
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_droplet),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ){
                            Text(
                                text = "Watering",
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Text(
                                text = detailUi.plants.watering,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            shape = RoundedCornerShape(corner = CornerSize(5.dp)),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_sunlight),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = "Sunlight",
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                            )

                            Text(
                                text = detailUi.plants.sunlight,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 12.sp,
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            shape = RoundedCornerShape(corner = CornerSize(5.dp)),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_plant),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Column(

                        ) {
                            Text(
                                text = "Soil",
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp,
                            )

                            Text(
                                text = detailUi.plants.soil,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 12.sp,
                            )
                        }
                    }
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

//@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun prevDetailScreen() {

    val height = LocalConfiguration.current.screenHeightDp

    var fav by remember {
        mutableStateOf(false)
    }
    val scale by animateFloatAsState(
        targetValue = if (fav) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Testing2Theme {
        Scaffold(
            floatingActionButton = {
            }
        ) {it->
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.onSecondary)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = " image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(((height / 2) - 50).dp)
                            .align(Alignment.TopCenter)


                    )

                    IconButton(
                        onClick = { fav = !fav },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(top = 10.dp)
                            .scale(scale)
                    ) {

                        Icon(
                            imageVector = if (fav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "wishlist icon",
                            tint = if (fav) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,

                            )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(corner = CornerSize(5.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_droplet),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Text(
                        text = "detailUi.plants.watering",
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
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sunlight),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Text(
                        text = "detailUi.plants.sunlight",
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
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_plant),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Text(
                        text = "detailUi.plants.soil",
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                    )
                }
            }

        }
    }
}