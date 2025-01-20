package com.example.mycodechallenge.model.repository

import com.example.mycodechallenge.model.data.FlickrImage
import com.example.mycodechallenge.model.datasource.FetchImagesDataSource

class FetchImagesRepositoryImpl(
    private val fetchImagesDataSource: FetchImagesDataSource
): FetchImagesRepository {
    override suspend fun fetchImages(searchTerm: String): List<FlickrImage> {
        return fetchImagesDataSource.fetchImages(searchTerm)
    }
}