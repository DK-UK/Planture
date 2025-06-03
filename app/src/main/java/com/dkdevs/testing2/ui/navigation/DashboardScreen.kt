package com.dkdevs.testing2.ui.navigation

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.dkdevs.testing2.data.models.Plant
import com.dkdevs.testing2.ui.theme.Testing2Theme
import com.dkdevs.testing2.ui.uiComponents.MyAppTopBar
import com.dkdevs.testing2.ui.vm.DashboardViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    redirectToDetailScreen: (Int) -> Unit
) {

    val vm: DashboardViewModel = koinViewModel()
    val dashboardUi = vm.plants.collectAsStateWithLifecycle().value

    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            MyAppTopBar(title = "Dashboard")
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {


            if (dashboardUi.loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (dashboardUi.error.isNotEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        Text(text = dashboardUi.error)
                    }
                }
            } else {

                LazyColumn(
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 30.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .fillMaxSize()

                ) {
                    stickyHeader {

                    }
                    items(dashboardUi.plants) {
                        plantItem(
                            plantName = it.name,
                            plantScientificName = it.scientific_name,
                            plantImg = it.images.thumb
                        ) {
                            redirectToDetailScreen.invoke(it.id)
                        }
                    }
                }
            }

            TextField(
                value = searchText, onValueChange = {
                    searchText = it
                },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Close, contentDescription = "clear search",
                            tint = LocalContentColor.current.copy(alpha = 0.7f),
                            modifier = Modifier.clickable {
                                searchText = ""
                            }
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = if (searchText.length > 2) KeyboardOptions(imeAction = ImeAction.Search) else KeyboardOptions.Default,
                keyboardActions = KeyboardActions(onSearch = {
                    vm.searchPlant(searchText)
                }),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 16.dp)
                    .align(Alignment.TopCenter)
                    .clip(RoundedCornerShape(CornerSize(50.dp)))
            )
        }


    }
}

@Composable
fun plantItem(
    modifier: Modifier = Modifier,
    plantImg: String,
    plantName: String,
    plantScientificName: String,
    onClicked: () -> Unit
) {

    Log.e("Dhaval", "plantItem: NAME : ${plantName} -- SCIENCE : ${plantScientificName}")
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClicked.invoke()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AsyncImage(
            model = plantImg, contentDescription = "${plantName} plant image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .clip(
                    RoundedCornerShape(CornerSize(7.dp))
                )
                .background(color = MaterialTheme.colorScheme.primary)
                .size(80.dp)
        )

        Column {
            Text(
                text = plantName,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )

            Text(
                text = plantScientificName,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp
            )
        }
    }
}

//@Preview(showSystemUi = true, uiMode = UiModeManager.MODE_NIGHT_YES)
//@Preview(showSystemUi = true, uiMode = UiModeManager.MODE_NIGHT_NO)
@Composable
private fun prevPlantItem() {
    Testing2Theme {
        LazyColumn(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.onPrimary),
            contentPadding = PaddingValues(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(
                listOf(
                    Plant(
                        name = "abc",
                        scientific_name = "unknown"
                    ), Plant(
                        name = "efg",
                        scientific_name = "123456"
                    )
                )
            ) {
//                plantItem(plant = it) {
//
//                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun prevDashboardScreen() {

    TextField(
        value = "dhaval", onValueChange = {

        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Close, contentDescription = "clear search",
                tint = LocalContentColor.current.copy(alpha = 0.7f)
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CornerSize(50.dp)))
    )
}