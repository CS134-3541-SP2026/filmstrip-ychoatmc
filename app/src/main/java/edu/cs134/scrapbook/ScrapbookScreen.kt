package edu.cs134.scrapbook

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


@Composable
fun ScrapbookScreen(viewModel: ScrapbookViewModel,
                    modifier: Modifier = Modifier) {
//    var selectedSlot by rememberSaveable { mutableStateOf<Int?>(null) }
    val context = LocalContext.current
    var photoURI by remember {mutableStateOf<Uri?>(null)}

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
        viewModel.photoList.collectAsState().value.forEach {
            ScrapbookSlot(
                //photo = viewModel.photoList,
                photo = it,
                onClick = {
//                    selectedSlot = 1
                    val uri = getContextualUri(context)
                    photoURI = uri
                    // todo: activate the launcher
                    cameraLauncher.launch(photoURI!!)
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