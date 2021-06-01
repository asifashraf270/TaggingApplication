package com.example.taggingapplication.managers

data class AssetInfo(
    var tagName: String,
    var defaultImageUri: String, var totalPhotos: Int, var photosList: MutableList<PhotosList>
)