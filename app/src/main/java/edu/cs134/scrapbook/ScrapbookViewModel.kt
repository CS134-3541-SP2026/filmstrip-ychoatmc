package edu.cs134.scrapbook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class ScrapbookViewModel : ViewModel() {

    private val _photoList = MutableStateFlow<List<Bitmap?>>(emptyList())

    val photoList: StateFlow<List<Bitmap?>> = _photoList

    fun addPhoto(bitmap: Bitmap) {
        _photoList.value = _photoList.value + bitmap
    }

    /**
    var photo1 by mutableStateOf<Bitmap?>(null)
    var photo2 by mutableStateOf<Bitmap?>(null)
    var photo3 by mutableStateOf<Bitmap?>(null)

    fun setPhoto(slot: Int?, bitmap: Bitmap) {
        when(slot) {
//            1 -> photoList = photoList + bitmap
            1 -> photo1 = bitmap
            2 -> photo2 = bitmap
            3 -> photo3 = bitmap
        }
    }
    */
}