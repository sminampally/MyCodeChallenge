package com.example.mycodechallenge.view

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import com.example.mycodechallenge.viewmodel.FetchImagesViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun FlickrApp(viewModel: FetchImagesViewModel) {
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
    var webView: WebView? by remember { mutableStateOf(null) }
    selectedImage?.let { image ->
        AlertDialog(
            onDismissRequest = { viewModel.clearSelection() },
            text = {
                Column {
                    Image(
                        painter = rememberAsyncImagePainter(image.imageUrl),
                        contentDescription = image.title,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = image.title,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    val htmlContent = "<html><body>${image.description}</body></html>"
                    AndroidView(
                        factory = { context ->
                            WebView(context).apply {
                                settings.javaScriptEnabled = true
                                settings.domStorageEnabled = true
                                webViewClient = WebViewClient()
                                loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
                                webView = this
                            }
                        },
                        update = {
                            it.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Author: ${image.author}")
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Published: ${formatDate(image.publishedDate)}")
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.clearSelection() }) {
                    Text("Close")
                }
            }
        )
    }

    BackHandler(enabled = webView?.canGoBack() == true) {
        webView?.goBack()
    }
}

fun formatDate(rawDateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = inputFormat.parse(rawDateString)
        outputFormat.format(date)
    } catch (e: Exception) {
        rawDateString // Fallback in case of error
    }
}