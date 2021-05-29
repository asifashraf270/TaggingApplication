package com.example.taggingapplication.imagepreviewtags

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taggingapplication.R
import com.example.taggingapplication.TagsAdapter
import com.example.taggingapplication.managers.PhotosList

class ImagePreviewAdapter(var context: Context, var list: List<PhotosList>) :
    RecyclerView.Adapter<ImagePreviewAdapter.ImagePreviewViewHolder>() {
    lateinit var onClickListener: View.OnClickListener

    inner class ImagePreviewViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var imageview: ImageView = itemview.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePreviewViewHolder {
        val termView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.preview_image_itemview, parent, false)
        return ImagePreviewViewHolder(termView)
    }

    override fun onBindViewHolder(holder: ImagePreviewViewHolder, position: Int) {
        var model = list.get(position)
        Glide.with(context).load(model.imageUri)
            .placeholder(context.resources.getDrawable(R.drawable.ic_imageplaceholder)).centerCrop()
            .into(holder.imageview)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}