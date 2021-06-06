package com.example.taggingapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taggingapplication.managers.PhotosList
import com.example.taggingapplication.managers.TagsManager
import com.example.taggingapplication.managers.AssetInfo
import com.example.taggingapplication.photos.PhotosActivity
import com.example.taggingapplication.utilities.AppConstants
import com.example.taggingapplication.utilities.AppLogger
import com.example.taggingapplication.utilities.CustomPhotoAlbum
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

private const val TAG = "TagFragment"

class TagFragment : Fragment(), View.OnClickListener {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: TagsAdapter
    var list = mutableListOf<AssetInfo>()
    var gson = Gson()
    var tagsManager: TagsManager? = null
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_tag, container, false)
        sharedPreferences = requireActivity().getSharedPreferences(AppConstants.PREF_NAME, 0)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        adapter = TagsAdapter(requireContext())
        recyclerView.adapter = adapter


        return view
    }

    override fun onResume() {
        super.onResume()
        adapter.onClickListener = this
        Dexter.withActivity(activity).withPermissions(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                token!!.continuePermissionRequest()
            }

            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report!!.areAllPermissionsGranted()) {
                    tagsManager = CustomPhotoAlbum.getAlbum(requireContext())

                    CustomPhotoAlbum.createAlbum()

                    if (tagsManager == null || tagsManager?.list?.size == 0) {
                        list.clear()
                        sharedPreferences.edit().putBoolean(AppConstants.NOTAG, true).commit()
                        AppLogger.errorLog(TAG, "tagsManager is Null create Object")


                        var photoList: MutableList<PhotosList> =
                            CustomPhotoAlbum.getPhotosFromDirectory()

                        var model: AssetInfo =
                            AssetInfo("No Tags in Photo", "", photoList.size, photoList)
                        list.add(model)
                        adapter.updateList(list)

                    } else {
                        sharedPreferences.edit().putBoolean(AppConstants.NOTAG, false).commit()
                        adapter.updateList(tagsManager?.list!!)
                    }

                } else {
                }
            }
        }).check()

    }

    fun initViews(view: View) {


    }

    companion object {
        fun getInstance() = TagFragment()
    }

    override fun onClick(v: View?) {
        var pos = v?.getTag() as Int
        when (v?.id) {

            R.id.viewImages -> {
                var intent = Intent(requireContext(), PhotosActivity::class.java)
                if (sharedPreferences.getBoolean(AppConstants.NOTAG, false)) {
                    intent.putExtra(
                        AppConstants.PHOTOLIST,
                        gson.toJson(list.get(pos).photosList)
                    )
                    intent.putExtra(
                        AppConstants.DEFAULTIMAGE,
                        ""
                    )
                } else {
                    AppLogger.errorLog(
                        TAG,
                        tagsManager?.list?.get(pos)?.photosList?.size!!.toString() + "....size"
                    )
                    intent.putExtra(
                        AppConstants.PHOTOLIST,
                        gson.toJson(tagsManager?.list?.get(pos)?.photosList)
                    )
                    intent.putExtra(AppConstants.TAGGINGPOSITION, pos)
                    intent.putExtra(
                        AppConstants.DEFAULTIMAGE,
                        tagsManager?.list?.get(pos)?.defaultImageUri
                    )
                }

                startActivity(intent)
            }
        }
    }
}