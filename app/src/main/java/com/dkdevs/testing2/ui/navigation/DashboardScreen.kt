package com.dkdevs.testing2.ui.navigation

import android.app.Activity
import android.content.res.Configuration
import android.util.Log
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.input.InputModeManager
import androidx.compose.ui.input.key.SoftKeyboardInterceptionModifierNode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.dkdevs.testing2.R
import com.dkdevs.testing2.data.models.Plant
import com.dkdevs.testing2.ui.theme.Testing2Theme
import com.dkdevs.testing2.ui.theme.plantColors
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

    val state = rememberLazyListState()
    val keyboardManager = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    val allPlants = rememberUpdatedState(vm.backupPlants)

    val view = LocalView.current
    val window = (view.context as Activity).window
    window.statusBarColor = MaterialTheme.colorScheme.primary.toArgb()
    window.navigationBarColor = MaterialTheme.colorScheme.secondaryContainer.toArgb()
    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false

    /*LaunchedEffect(lastId) {
        Log.e("Dhaval", "DashboardScreen: LASTID : $lastId", )
        vm.getAllPlants(lastId = lastId)
    }*/

    LaunchedEffect(state) {

            snapshotFlow { state.layoutInfo }
                .collect {info ->
                    if (searchText.isEmpty()){

                        val visibleItemsInfo = info.visibleItemsInfo
                        if (visibleItemsInfo.lastOrNull()?.index == info.totalItemsCount - 1) {

                            // user is at the end of the list
                            visibleItemsInfo.lastOrNull()?.let { it->
                                Log.e("Dhaval", "DashboardScreen: INDEX : ${it.index} : Size : ${allPlants.value.size}", )
                                vm.getAllPlants(allPlants.value, info.totalItemsCount)
                            }
                        }
                    }
                }

    }
    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            MyAppTopBar(title = stringResource(R.string.dashboard))
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.plantColors.cardBackground)
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
                    state = state,
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 30.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .fillMaxSize()

                ) {

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

            AnimatedVisibility(visible = !state.isScrollInProgress,
                exit = slideOut { IntOffset(y = -150, x = 0) },
                enter = slideIn { IntOffset(y = -150, x = 0) }
            ) {

                TextField(
                    value = searchText, onValueChange = {
                        searchText = it
                    },
                    leadingIcon = {

                        Icon(
                            imageVector = Icons.Default.Search,
                            tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                            contentDescription = null
                        )

                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_for_plants),
                            fontSize = 14.sp,
                            color = LocalContentColor.current.copy(alpha = 0.7f)
                        )
                    },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "clear search",
                                tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                                modifier = Modifier.clickable {
                                    searchText = ""
                                    vm.getBackupPlants()
                                }
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer ,
                        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer

                    ),
                    keyboardOptions = if (searchText.length > 2) KeyboardOptions(imeAction = ImeAction.Search) else KeyboardOptions.Default,
                    keyboardActions = KeyboardActions(onSearch = {
                        keyboardManager?.hide()
                        focusManager.clearFocus()
                        vm.searchPlant(searchText)
                    }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(vertical = 10.dp, horizontal = 16.dp)
                        .clip(RoundedCornerShape(CornerSize(50.dp)))
                        .clickable {
                            keyboardManager?.show()
                        }
                )
            }


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

@OptIn(ExperimentalFoundationApi::class)
@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun prevDashboardScreen() {

    val state = rememberLazyListState()

    var count = (0..100).toList()

    Testing2Theme {

        LazyColumn(
            state = state,
            modifier = Modifier.background(color = MaterialTheme.plantColors.cardBackground)
        ) {

            stickyHeader {

                AnimatedVisibility(visible = !state.isScrollInProgress,
                    exit = slideOut { IntOffset(y = -150, x = 0) },
                    enter = slideIn { IntOffset(y = -150, x = 0) }
                ) {

                    TextField(
                        value = "", onValueChange = {

                        },
                        leadingIcon = {
                                      Icon(imageVector = Icons.Default.Search, contentDescription = null,
                                          tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f))
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "clear search",
                                tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color.Transparent,
                            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(CornerSize(50.dp)))
                    )
                }
            }

            items(count) {
                Text(
                    text = it.toString(),
                    color = MaterialTheme.colorScheme.primary, modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(10.dp)
                )
            }
        }
    }
}