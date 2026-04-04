package edu.cs134.scrapbook

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import androidx.core.graphics.scale
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.withIndex
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore("scrapbook_prefs")

fun getContextualUri(context: Context): Uri {
    val tempFile = File.createTempFile(
        "shot_",
        ".jpg",
        context.externalCacheDir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        tempFile
    )
}


@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun ScrapbookScreen(viewModel: ScrapbookViewModel,
                    modifier: Modifier = Modifier) {
//    var selectedSlot by rememberSaveable { mutableStateOf<Int?>(null) }
    val context = LocalContext.current
    val TOTAL_PHOTOS = intPreferencesKey("total_photos")
    val totalPhotos by context.dataStore.data
        .map { it[TOTAL_PHOTOS]?:0 }
        .collectAsState(0)
    var photoURI by remember {mutableStateOf<Uri?>(null)}
    val scope = rememberCoroutineScope()

    for(i in 0..< totalPhotos) {
        viewModel.refreshPhoto(i, LocalContext.current)
    }
    // todo: create a launcher
    val cameraLauncher =
        rememberLauncherForActivityResult(
        ActivityResultContracts
            .TakePicture()
    ) {
            success ->
            if(success && photoURI != null){
                // ending with !! is like de-referencing a pointer
                // it basically lets us look at the core value
                // if it's null, it'll break
                val inputStream = context.contentResolver.openInputStream(photoURI!!)
                val fullBitmap = BitmapFactory.decodeStream(inputStream)
                val scaleBitmap = fullBitmap.scale(1024, 1024)
                viewModel.addPhoto(scaleBitmap)
            //    viewModel.setPhoto(1, scaleBitmap)
            }
            /**
        image ->
        image?.let {
            viewModel.addPhoto(it)*/
//            viewModel.setPhoto(selectedSlot, it)
//        }
    }

    Column (
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        //Text("totalPhotos: $totalPhotos & ${viewModel.photoList.collectAsState().value.size}")
        //for(i in 0..< viewModel.photoList.collectAsState().value.size){
        viewModel.photoList.collectAsState().value.forEachIndexed { index, bitmap ->
            ScrapbookSlot(
                //photo = viewModel.photoList,
                photo = bitmap,
                onClick = {
//                    selectedSlot = 1
                    val storageDir = context.externalCacheDir
                    val imageFile = File(storageDir, "photo_$index.jpg")
                    val uri = FileProvider.getUriForFile(context,
                        "edu.cs134.scrapbook.fileprovider",
                        imageFile)
//                    val uri = getContextualUri(context)
//                    photoURI = uri
                    // todo: activate the launcher
                    cameraLauncher.launch(uri)
                    if(index + 1 == viewModel.photoList.value.size) {
                        scope.launch {
                            context.dataStore.edit { it[TOTAL_PHOTOS] = totalPhotos + 1 }
                        }
                    }
                }
            )
        }
        /**
        ScrapbookSlot(
            //photo = viewModel.photoList,
            photo = viewModel.photo2,
            onClick = {
                selectedSlot = 2
                // todo: activate the launcher
                cameraLauncher.launch(null)
            }
        )
        */
    }
}