package com.example.mycodechallenge.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mycodechallenge.model.data.network.FetchImagesApi
import com.example.mycodechallenge.model.datasource.FetchImagesNetworkDataSource
import com.example.mycodechallenge.model.repository.FetchImagesRepositoryImpl
import com.example.mycodechallenge.viewmodel.FetchImagesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataSource = FetchImagesNetworkDataSource(FetchImagesApi())
        val repository = FetchImagesRepositoryImpl(dataSource)
        val fetchImagesViewModel = ViewModelProvider.create(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FetchImagesViewModel(repository) as T
            }
        })[FetchImagesViewModel::class]
        setContent {
            val viewModel = remember { fetchImagesViewModel }
            FlickrApp(viewModel)
        }
    }
}
