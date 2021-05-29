package com.example.taggingapplication.managers

data class TagsPhotoDetail(
    var tagName: String,
    var defaultImageUri: String, var totalPhotos: Int, var photosList: MutableList<PhotosList>
)