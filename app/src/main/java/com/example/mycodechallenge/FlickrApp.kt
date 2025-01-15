package com.example.mycodechallenge

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun FlickrApp(viewModel: FlickrViewModel) {
    var searchText by remember { mutableStateOf("") }
    val images = viewModel.imageList // Directly access the state
    val loading = viewModel.loading.value // Directly read the state
    val localKeyboardController = LocalSoftwareKeyboardController.current

    Column {
        TextField(
            value = searchText,
            onValueChange = {
                searchText = it
            },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                localKeyboardController?.hide()
                viewModel.fetchImages(searchText)
            })
        )
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(images.size) { index ->
                val image = images[index]
                Image(
                    painter = rememberAsyncImagePainter(image.thumbnailUrl),
                    contentDescription = image.title,
                    modifier = Modifier
                        .padding(4.dp)
                        .size(100.dp)
                        .clickable {
                            viewModel.onImageClick(image)
                        }
                )
            }
        }
    }

    val selectedImage = viewModel.selectedImage.value
    selectedImage?.let { image ->
        AlertDialog(
            onDismissRequest = { viewModel.clearSelection() },
            title = { Text(image.title) },
            text = {
                Column {
                    Image(
                        painter = rememberAsyncImagePainter(image.imageUrl),
                        contentDescription = image.title,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Description: ${image.description}")
                    Text("Author: ${image.author}")
                    Text("Published: ${image.publishedDate}")
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.clearSelection() }) {
                    Text("Close")
                }
            }
        )
    }
}

