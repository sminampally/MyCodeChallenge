package com.example.mycodechallenge.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycodechallenge.model.data.FlickrImage
import com.example.mycodechallenge.model.repository.FetchImagesRepository
import kotlinx.coroutines.launch

class FetchImagesViewModel(
    private val fetchImagesRepository: FetchImagesRepository
) : ViewModel() {
    private val _imageList = mutableStateListOf<FlickrImage>()
    val imageList: SnapshotStateList<FlickrImage> get() = _imageList

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> get() = _loading

    private val _selectedImage = mutableStateOf<FlickrImage?>(null)
    val selectedImage: State<FlickrImage?> get() = _selectedImage

    fun fetchImages(searchTerm: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _imageList.clear()
                _imageList.addAll(fetchImagesRepository.fetchImages(searchTerm))
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
