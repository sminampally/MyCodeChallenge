package com.example.mycodechallenge.model.repository

import com.example.mycodechallenge.model.data.FlickrImage

interface FetchImagesRepository {
    suspend fun fetchImages(searchTerm: String): List<FlickrImage>
}