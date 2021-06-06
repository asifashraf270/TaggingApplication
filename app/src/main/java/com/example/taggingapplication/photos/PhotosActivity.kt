package com.example.taggingapplication.photos

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taggingapplication.R
import com.example.taggingapplication.imagepreviewtags.ImagePreviewActivity
import com.example.taggingapplication.managers.PhotosList
import com.example.taggingapplication.managers.TagsManager
import com.example.taggingapplication.utilities.AppConstants
import com.example.taggingapplication.utilities.AppLogger
import com.example.taggingapplication.utilities.CustomPhotoAlbum
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

private const val TAG = "PhotosActivity"

class PhotosActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var recyclerview: RecyclerView
    var gson = GsonBuilder().create()
    lateinit var photosList: List<PhotosList>
    var sendDataGson = Gson()
    lateinit var sharedPreferences: SharedPreferences
    var defaultImage: String? = null
    var isNoTag = false
    var tagPosition: Int = 0
    var tags: TagsManager? = null


    lateinit var adapter: PhotosAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos)
        recyclerview = findViewById(R.id.recyclerView)
        sharedPreferences = getSharedPreferences(AppConstants.PREF_NAME, 0)
        val imagesList: Type = object : TypeToken<List<PhotosList?>?>() {}.getType()
        photosList = gson.fromJson(
            intent.getStringExtra(AppConstants.PHOTOLIST),
            imagesList
        )
        defaultImage = intent.getStringExtra(AppConstants.DEFAULTIMAGE)
        AppLogger.errorLog(TAG, defaultImage.toString() + "....default image uri")
        isNoTag = sharedPreferences.getBoolean(AppConstants.NOTAG, false)
        if (!isNoTag) {
            tags = CustomPhotoAlbum.getAlbum(this)
            tagPosition = intent.getIntExtra(AppConstants.TAGGINGPOSITION, -1)
        }

        adapter = PhotosAdapter(this, photosList, defaultImage)
        adapter.onClickListener = this
        recyclerview.layoutManager = GridLayoutManager(this, 4)
        recyclerview.adapter = adapter
    }

    override fun onClick(v: View?) {
        var position = v?.getTag() as Int
        when (v?.id) {
            R.id.imageView -> {
                if (sharedPreferences.getBoolean(AppConstants.NOTAG, false)) {
                    var intent = Intent(this, ImagePreviewActivity::class.java)
                    intent.putExtra(
                        AppConstants.PHOTOLIST, gson.toJson(photosList)
                    )
                    intent.putExtra(AppConstants.CURRENTINDEX, position)
                    startActivity(intent)
                } else {
                    adapter.defaultImage = photosList.get(position).imageUri
                    tags?.list?.get(tagPosition)?.defaultImageUri = photosList.get(position).imageUri
                    CustomPhotoAlbum.saveAlbum(this,tags)
                    adapter.notifyDataSetChanged()
                }

            }
        }
    }
}