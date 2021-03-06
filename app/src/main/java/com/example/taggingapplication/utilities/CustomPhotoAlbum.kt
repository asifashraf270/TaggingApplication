package com.example.taggingapplication.utilities

import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import com.example.taggingapplication.managers.PhotosList
import com.example.taggingapplication.managers.TagsManager
import com.example.taggingapplication.managers.AssetInfo
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

class CustomPhotoAlbum {

    companion object {
        var fileName = "CustomPhotoAlbum"
        fun createAlbum() {
            //this will create a folder into Local Storage if No tag Present This will call from TagFragment
            var file = File(Environment.getExternalStorageDirectory().path, "/CustomPhotoAlbum/")
            if (!file.exists()) {
                file.mkdirs()
            }
        }

        fun saveAlbum(context: Context, tagsManager: TagsManager?) {
            //this will save tags into SharedPreference
            var sharedPreferences: SharedPreferences =
                context.getSharedPreferences(AppConstants.PREF_NAME, 0)
            var gson = Gson()
            sharedPreferences.edit().putString(AppConstants.TAGS_lIST, gson.toJson(tagsManager))
                .commit()
        }

        fun getAlbum(context: Context): TagsManager? {
            //this will return All tags saved in SharedPreference

            var sharedPreferences: SharedPreferences =
                context.getSharedPreferences(AppConstants.PREF_NAME, 0)
            var gson = GsonBuilder().create()
            var list = mutableListOf<AssetInfo>()
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

        fun getWidth(context: Context): Int {
            //this will return Width of Device I used this for Showing Dialog
            val displayMetrics = DisplayMetrics()
            val windowmanager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowmanager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }
    }
}