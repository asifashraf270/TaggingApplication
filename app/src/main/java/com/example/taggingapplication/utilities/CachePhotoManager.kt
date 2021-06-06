package com.example.taggingapplication.utilities

import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import android.util.Log
import com.example.taggingapplication.managers.AssetInfo
import com.example.taggingapplication.managers.PhotosList
import com.example.taggingapplication.managers.TagsManager
import com.google.gson.GsonBuilder
import java.io.File

class CachePhotoManager {
    companion object{
        fun fetchImages(): MutableList<PhotosList> {
            val path: String =
                Environment.getExternalStorageDirectory().toString()
                    .toString() + "/CustomPhotoAlbum"
            Log.d("Files", "Path: $path")
            val directory = File(path)
            val files: Array<File> = directory.listFiles()
            Log.d("Files", "Size: " + files.size)
            var list = mutableListOf<PhotosList>()
            for (i in files.indices) {
                list.add(PhotosList(files[i].path))
                Log.d("Files", "FileName:" + files[i].getName())
            }
            return list
        }
        fun assetWidth(context: Context):Int{
            var tagsManager=CustomPhotoAlbum.getAlbum(context)
            return tagsManager?.list?.size!!
        }
    }
}