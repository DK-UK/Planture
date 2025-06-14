package com.dkdevs.testing2.ui.navigation

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.dkdevs.testing2.R
import com.dkdevs.testing2.ui.utility.Utils
import com.dkdevs.testing2.ui.theme.Testing2Theme
import com.dkdevs.testing2.ui.theme.plantColors
import com.dkdevs.testing2.ui.uiComponents.MyAppTopBar
import com.dkdevs.testing2.ui.vm.IdentifyViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.core.net.toUri

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                context.checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        (context.checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) /*&&
                        (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)*/)
        } else {
            mutableStateOf(
                context.checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && (context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                       /* (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)*/)
        }
    }

    var galleryPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(context.checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED)
        } else {
            mutableStateOf(context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        }
    }

    var cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) {
            cameraPermission = it.all {perm-> perm.value }
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
                imageStr = it.toString()
                vm.identifyPlant(multipart)
            }
        }

    var captureFromCameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) {
            it?.let {

                imageStr = Utils.bitmapToUri(context, it).toString()
                val multipart = Utils.bitmapToMultipart(it)
                vm.identifyPlant(multipart)
            }
        }

    LaunchedEffect(key1 = identifyUi) {
        if (identifyUi.plantDetails.isNotEmpty()) {
            Log.e("Dhaval", "IdentifyScreen: LAUNCH EFFECT")
            redirectToPlantDetailScreen.invoke(identifyUi.plantDetails.first().id)
            vm.clearData()
        }
    }

    Scaffold(
        modifier = modifier
            .statusBarsPadding(),
        topBar = {
            MyAppTopBar(title = stringResource(R.string.identify))
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.plantColors.cardBackground)
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
                        model = imageStr.toUri(), contentDescription = "plant image",
                        modifier = Modifier.height((height / 2).dp)
                    )

                    if (identifyUi.error.isNotEmpty()) {
                        Text(text = identifyUi.error, fontWeight = FontWeight.Normal, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                        Button(onClick = {
                            imageStr = ""
                        }) {
                            Text(text = stringResource(R.string.retry))
                        }
                    }
                    else {
                        Text(
                            text = stringResource(R.string.identifying),
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        LinearProgressIndicator()
                    }
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
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(10.dp)
                                .size(25.dp)
                        )
                    }

                    Spacer(modifier = Modifier.padding(20.dp))

                    Card(
                        shape = CircleShape,
                        onClick = {
                            if (cameraPermission) {
                                captureFromCameraLauncher.launch(null)
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    cameraLauncher.launch(arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_MEDIA_IMAGES, /*android.Manifest.permission.WRITE_EXTERNAL_STORAGE*/))
                                }
                                else {
                                    cameraLauncher.launch(arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, /*android.Manifest.permission.WRITE_EXTERNAL_STORAGE*/))
                                }
                            }
                        }
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = "capture",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(10.dp)
                                .size(40.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
private fun prevIdentifyScreen() {
//    IdentifyScreen()

    Testing2Theme {
        Card(
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            onClick = {

            }

        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_gallery),
                contentDescription = "pick from gallery",
                modifier = Modifier
                    .padding(10.dp)
                    .size(25.dp)
            )
        }
    }

}