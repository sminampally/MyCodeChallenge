package com.example.mycodechallenge.model.data.network

import com.example.mycodechallenge.model.data.FlickrImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class FetchImagesApi {
    suspend fun fetchImages(searchTerm: String) = withContext(Dispatchers.IO) {
        val url =
            "https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1&tags=$searchTerm"
        val response = URL(url).readText()
        val jsonResponse = JSONObject(response)
        val items = jsonResponse.getJSONArray("items")
        (0 until items.length()).map { index ->
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
    }
}