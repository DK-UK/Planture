package com.dkdevs.testing2.ui

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import androidx.core.content.FileProvider
import com.dkdevs.testing2.data.local.PlantEntity
import com.dkdevs.testing2.data.models.Data
import com.dkdevs.testing2.data.models.Plant
import com.dkdevs.testing2.data.models.toPlantEntity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object Utils {

    fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        }
        return false
    }

    fun bitmapToUri(context: Context, bitmap: Bitmap) : Uri? {
        val file = File(context.cacheDir, "images.png")
        try {
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.close()
            return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        }
        catch (e : Exception) {
            e.printStackTrace()
        }
        return null
    }
    fun bitmapToMultipart(bitmap : Bitmap) : MultipartBody.Part {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData("images", "image.jpg", requestBody)
    }

    fun uriToMultipart(context: Context, uri: Uri) : MultipartBody.Part {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "${System.currentTimeMillis()}_image.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()

        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("images", file.name, requestBody)
    }

    fun getNurturingDetails(data : List<Data>) : NurturingDetails {
        var details = NurturingDetails()

        data.forEach {
            if (it.key.contains("water requirement", ignoreCase = true)) {
                details.water = it.value
            }
            else if (it.key.contains("light requirement", ignoreCase = true)) {
                details.sunlight = it.value
            }
            else if (it.key.contains("soil type", ignoreCase = true)) {
                details.soil = it.value
            }
        }
        return details
    }

    fun apiToLocalDb(plant : Plant) : PlantEntity {
        var plantEntity = plant.toPlantEntity()
        var details = getNurturingDetails(plant.data)
        plantEntity.watering = details.water
        plantEntity.sunlight = details.sunlight
        plantEntity.soil = details.soil
        return plantEntity
    }
}

data class NurturingDetails (
    var water : String = "",
    var sunlight : String = "",
    var soil : String = ""
)