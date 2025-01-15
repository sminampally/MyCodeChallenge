package com.example.mycodechallenge

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL


class FlickrViewModel : ViewModel() {
    private val _imageList = mutableStateListOf<FlickrImage>()
    val imageList: SnapshotStateList<FlickrImage> get() = _imageList

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> get() = _loading

    private val _selectedImage = mutableStateOf<FlickrImage?>(null)
    val selectedImage: State<FlickrImage?> get() = _selectedImage

    fun fetchImages(searchText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            val url = "https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1&tags=$searchText"
            try {
                val response = URL(url).readText()
                val jsonResponse = JSONObject(response)
                val items = jsonResponse.getJSONArray("items")
                val newImages = (0 until items.length()).map { index ->
                    val item = items.getJSONObject(index)
                    FlickrImage(
                        title = item.getString("title"),
                        thumbnailUrl = item.getJSONObject("media").getString("m"),
                        imageUrl = item.getJSONObject("media").getString("m"),
                        description = item.getString("description"),
                        author = item.getString("author"),
                        publishedDate = item.getString("published")
                    )
                }
                _imageList.clear()
                _imageList.addAll(newImages)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _loading.value = false
            }
        }
    }

    fun onImageClick(image: FlickrImage) {
        _selectedImage.value = image
    }

    fun clearSelection() {
        _selectedImage.value = null
    }
}
