package com.example.mycodechallenge.model.datasource

import com.example.mycodechallenge.model.data.FlickrImage
import com.example.mycodechallenge.model.data.network.FetchImagesApi

class FetchImagesNetworkDataSource(
    private val fetchImagesApi: FetchImagesApi
): FetchImagesDataSource {
    override suspend fun fetchImages(searchTerm: String): List<FlickrImage> {
        return fetchImagesApi.fetchImages(searchTerm)
    }
}