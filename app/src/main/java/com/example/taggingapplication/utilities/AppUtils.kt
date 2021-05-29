package com.example.taggingapplication.utilities

import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import com.example.taggingapplication.managers.PhotosList
import com.example.taggingapplication.managers.TagsManager
import com.example.taggingapplication.managers.TagsPhotoDetail
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

class AppUtils {
    companion object {
        fun storeTagsManagerObject(context: Context, tagsManager: TagsManager?) {
            var sharedPreferences: SharedPreferences =
                context.getSharedPreferences(AppConstants.PREF_NAME, 0)
            var gson = Gson()
            sharedPreferences.edit().putString(AppConstants.TAGS_lIST, gson.toJson(tagsManager))
                .commit()
        }

        fun getTagsManager(context: Context): TagsManager? {
            var sharedPreferences: SharedPreferences =
                context.getSharedPreferences(AppConstants.PREF_NAME, 0)
            var gson = GsonBuilder().create()
            var list = mutableListOf<TagsPhotoDetail>()
            var tagsManager = TagsManager(list)
            if (sharedPreferences.getString(AppConstants.TAGS_lIST, "").isNullOrEmpty()) {
                return tagsManager
            }
            tagsManager = gson.fromJson(
                sharedPreferences.getString(AppConstants.TAGS_lIST, ""),
                TagsManager::class.java
            )
            return tagsManager
        }

        fun getPhotosFromDirectory(): MutableList<PhotosList> {
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

        fun getWidth(context: Context): Int {
            val displayMetrics = DisplayMetrics()
            val windowmanager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowmanager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }
    }
}