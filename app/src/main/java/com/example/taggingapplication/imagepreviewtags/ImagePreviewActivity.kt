package com.example.taggingapplication.imagepreviewtags

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taggingapplication.R
import com.example.taggingapplication.imagepreviewtags.tags.AddNewTagAdapter
import com.example.taggingapplication.imagepreviewtags.tags.TagsListAdapter
import com.example.taggingapplication.locationactivity.LocationActivity
import com.example.taggingapplication.managers.PhotosList
import com.example.taggingapplication.managers.AssetInfo
import com.example.taggingapplication.models.TagsModel
import com.example.taggingapplication.utilities.AppConstants
import com.example.taggingapplication.utilities.AppLogger
import com.example.taggingapplication.utilities.CustomPhotoAlbum
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

private const val TAG = "ImagePreviewActivity"

class ImagePreviewActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var recyclerView: RecyclerView
    lateinit var tagsRecyclerView: RecyclerView
    lateinit var button: Button
    lateinit var adapter: ImagePreviewAdapter
    lateinit var sharedPreferences: SharedPreferences
    var gson = GsonBuilder().create()
    var currentIndex: Int = 0
    lateinit var photosList: List<PhotosList>

    //this TagList is for show Tags on Below Images List
    var tagsList = mutableListOf<TagsModel>()
    lateinit var layoutManager: RecyclerView.LayoutManager

    //this one is for show On Buck RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)
        recyclerView = findViewById(R.id.recyclerView)
        sharedPreferences = getSharedPreferences(AppConstants.PREF_NAME, 0)
        tagsRecyclerView = findViewById(R.id.tagsRecyclerView)
        button = findViewById(R.id.button)
        currentIndex = intent.getIntExtra(AppConstants.CURRENTINDEX, 0)
        val imagesList: Type = object : TypeToken<List<PhotosList?>?>() {}.getType()
        photosList = gson.fromJson(
            intent.getStringExtra(AppConstants.PHOTOLIST),
            imagesList
        )
        AppLogger.errorLog(TAG, photosList.size.toString() + ",,,,,")

        adapter = ImagePreviewAdapter(this, photosList)
        recyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        adapter.onClickListener = this
        recyclerView.adapter = adapter
        recyclerView.scrollToPosition(currentIndex)
        button.setOnClickListener(this)

        bottomRecyclerView()

    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.button -> {
                if (sharedPreferences.getBoolean(AppConstants.ISLOCATIONSAVED, false)) {
                    tagsList.clear()
                    var tagsManager = CustomPhotoAlbum.getTagsManager(this)
                    if (tagsManager?.list?.size!! > 0) {
                        for (i in 0 until tagsManager.list.size) {
                            tagsList.add(
                                TagsModel(
                                    tagsManager.list.get(i).tagName,
                                    tagsManager.list.get(i).defaultImageUri,
                                    false
                                )
                            )
                        }
                    }
                    saveTagDialog()
                } else {
                    var intent = Intent(this, LocationActivity::class.java)
                    startActivityForResult(intent, 100)
                }
            }
            R.id.rootView -> {
                var tagsName = v.tag as String
                AppLogger.errorLog(TAG, "got click listener " + tagsName)
                var tagsManager = CustomPhotoAlbum.getTagsManager(this)
                for (i in 0 until tagsManager?.list?.size!!) {
                    if (tagsManager.list.get(i).tagName.equals(tagsName)) {
                        var list = tagsManager.list.get(i).photosList
                        list.add(PhotosList(getCurrentOpenImage()))
                    }
                }
                CustomPhotoAlbum.storeTagsManagerObject(this, tagsManager)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                //show dialog
                saveTagDialog()
            }
        }
    }

    fun saveTagDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.save_tag_dialog)
        var recyclerView: RecyclerView = dialog.findViewById(R.id.recyclerView)
        var adapter = AddNewTagAdapter(this, tagsList)
        recyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
        var cancel: Button = dialog.findViewById(R.id.cancel)
        var saveTag: Button = dialog.findViewById(R.id.saveTags)
        var buckName: Button = dialog.findViewById(R.id.buckName)
        var message: TextView = dialog.findViewById(R.id.message)
        if (tagsList.size > 0) {
            message.visibility = View.GONE
        } else {
            message.visibility = View.VISIBLE
        }
        buckName.setOnClickListener({
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.buck_name_dialog)
            var buckName: EditText = dialog.findViewById(R.id.buckName)
            var cancelButton: Button = dialog.findViewById(R.id.cancel)
            var saveButton: Button = dialog.findViewById(R.id.save)
            cancelButton.setOnClickListener({
                dialog.dismiss()
            })
            saveButton.setOnClickListener({
                if (buckName.text.toString().length > 0) {
                    var model = TagsModel(buckName.text.toString(), "", false)
                    tagsList.add(model)

                    /* adapter.updateList(tagsList)*/
                    adapter.notifyDataSetChanged()
                    message.visibility = View.GONE
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Please Enter Tags Name", Toast.LENGTH_SHORT).show()
                }

            })
            dialog.setOnDismissListener(object : DialogInterface.OnDismissListener {
                override fun onDismiss(dialog: DialogInterface?) {

                }
            })
            dialog.show()
        })
        saveTag.setOnClickListener({
            var currentPosition =
                (recyclerView?.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            var image = photosList.get(currentIndex).imageUri
            AppLogger.errorLog(TAG, image.toString())

            var tagsManager = CustomPhotoAlbum.getTagsManager(this)

            for (i in 0 until adapter.list.size) {
                if (tagsManager?.list?.size!! > 0) {
                    for (j in 0 until tagsManager?.list?.size!!) {
                        if (adapter.list.get(i).isAdd) {
                            if (adapter.list.get(i).tagName.equals(tagsManager.list.get(j).tagName)) {
                                var list = tagsManager.list.get(j).photosList
                                list.add(PhotosList(photosList.get(currentPosition).imageUri))
                            } else {
                                var photoList = mutableListOf<PhotosList>()
                                photoList.add(PhotosList(this.photosList.get(currentPosition).imageUri))
                                var model = AssetInfo(
                                    adapter.list.get(i).tagName,
                                    photosList.get(currentIndex).imageUri,
                                    0,
                                    photosList as MutableList<PhotosList>
                                )
                                tagsManager.list.add(model)
                            }
                        }
                    }
                } else {
                    if (adapter.list.get(i).isAdd) {
                        var photoList = mutableListOf<PhotosList>()
                        photoList.add(PhotosList(this.photosList.get(currentPosition).imageUri))
                        var model = AssetInfo(
                            adapter.list.get(i).tagName,
                            photosList.get(currentPosition).imageUri,
                            0,
                            photoList
                        )
                        AppLogger.errorLog(TAG, photoList.size.toString() + "....photoList size")
                        tagsManager.list.add(model)
                    }
                }

            }
            AppLogger.errorLog(TAG, tagsManager.toString() + ".....Tags manager")
            CustomPhotoAlbum.storeTagsManagerObject(this, tagsManager)
            bottomRecyclerView()
            dialog.dismiss()
        })
        cancel.setOnClickListener({
            dialog.dismiss()
        })
        dialog.show()
    }

    fun bottomRecyclerView() {
        var tagsManager = CustomPhotoAlbum.getTagsManager(this)
        var adapter = TagsListAdapter(this, tagsManager?.list!!)
        tagsRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        tagsRecyclerView.adapter = adapter
        adapter.onClickListener = this

    }

    fun getCurrentOpenImage(): String {
        var currentPosition =
            (recyclerView?.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        return photosList.get(currentPosition).imageUri
    }
}