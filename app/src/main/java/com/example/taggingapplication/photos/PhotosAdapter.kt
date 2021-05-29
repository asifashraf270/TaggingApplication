package com.example.taggingapplication.photos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taggingapplication.R
import com.example.taggingapplication.TagsAdapter
import com.example.taggingapplication.managers.PhotosList

class PhotosAdapter(
    var context: Context,
    var photoList: List<PhotosList>,
    var defaultImage: String?
) :
    RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder>() {
    lateinit var onClickListener: View.OnClickListener

    inner class PhotosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var defaultImage: ImageView = itemView.findViewById(R.id.defaultImage)
        var imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val termView =
            LayoutInflater.from(parent.context).inflate(R.layout.photo_itemview, parent, false)
        return PhotosViewHolder(termView)
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        var photoList = photoList.get(position)
        holder.imageView.setOnClickListener(onClickListener)
        holder.imageView.setTag(position)
        Glide.with(context).load(photoList.imageUri)
            .placeholder(context.resources.getDrawable(R.drawable.ic_imageplaceholder)).centerCrop()
            .into(holder.imageView)
        if (defaultImage.equals(photoList.imageUri)) {
            holder.defaultImage.visibility = View.VISIBLE
        } else {
            holder.defaultImage.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}