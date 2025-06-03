package com.dkdevs.testing2.ui.navigation

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.dkdevs.testing2.R
import com.dkdevs.testing2.ui.Utils
import com.dkdevs.testing2.ui.uiComponents.MyAppTopBar
import com.dkdevs.testing2.ui.vm.IdentifyViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdentifyScreen(
    modifier: Modifier = Modifier,
    redirectToPlantDetailScreen: (Int) -> Unit
) {

    val context = LocalContext.current
    val height = LocalConfiguration.current.screenHeightDp

    val vm: IdentifyViewModel = koinViewModel()
    val identifyUi = vm.plantDetails.collectAsStateWithLifecycle().value

    var imageStr by remember {
        mutableStateOf("")
    }

    var cameraPermission by remember {
        mutableStateOf(
            context.checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    var galleryPermission by remember {
        mutableStateOf(context.checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED)
    }

    var cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
            cameraPermission = it
        }

    var galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
            galleryPermission = it
        }


    var pickFromGalleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            it?.let {
                Log.e("Dhaval", "IdentifyScreen: URI : ${it}")
                val multipart = Utils.uriToMultipart(context, it)
                Log.e(
                    "Dhaval",
                    "IdentifyScreen: MULTIPART : ${multipart.body} -- ${multipart.headers}",
                )
                vm.identifyPlant(multipart)
            }
        }

    var captureFromCameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) {
            it?.let {

                val multipart = Utils.bitmapToMultipart(it)
                vm.identifyPlant(multipart)
            }
        }

    LaunchedEffect(key1 = identifyUi) {
        if (identifyUi.plantDetails.isNotEmpty()) {
            redirectToPlantDetailScreen.invoke(identifyUi.plantDetails.first().id)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            MyAppTopBar(title = "Identify")
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(it),

            ) {
            if (imageStr.isNotEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    AsyncImage(
                        model = Uri.parse(imageStr), contentDescription = "plant image",
                        modifier = Modifier.height((height / 2).dp)
                    )
                    LinearProgressIndicator()
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {

                    Card(
                        shape = CircleShape,
                        onClick = {
                            if (galleryPermission) {
                                pickFromGalleryLauncher.launch(
                                    PickVisualMediaRequest.Builder()
                                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        .build()
                                )
                            } else {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    galleryLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                                } else {
                                    galleryLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                }
                            }
                        }

                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.ic_gallery),
                            contentDescription = "pick from gallery",
                            modifier = Modifier
                                .padding(10.dp)
                                .size(35.dp)
                        )
                    }

                    Spacer(modifier = Modifier.padding(20.dp))

                    Card(
                        shape = CircleShape,
                        onClick = {
                            if (cameraPermission) {
                                captureFromCameraLauncher.launch(null)
                            } else {
                                cameraLauncher.launch(android.Manifest.permission.CAMERA)
                            }
                        }
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = "capture",
                            modifier = Modifier
                                .padding(10.dp)
                                .size(50.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun prevIdentifyScreen() {
//    IdentifyScreen()
}