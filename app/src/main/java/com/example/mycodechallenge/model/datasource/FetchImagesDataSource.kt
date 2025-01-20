package com.example.mycodechallenge.model.datasource

import com.example.mycodechallenge.model.data.FlickrImage

interface FetchImagesDataSource {
    suspend fun fetchImages(searchTerm: String): List<FlickrImage>
}