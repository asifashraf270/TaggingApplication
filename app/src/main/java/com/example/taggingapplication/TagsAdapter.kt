package com.example.taggingapplication

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taggingapplication.managers.TagsPhotoDetail
import com.example.taggingapplication.utilities.AppLogger

private const val TAG = "TagsAdapter"

class TagsAdapter(var context: Context) : RecyclerView.Adapter<TagsAdapter.TagsViewHolder>() {
    var list: MutableList<TagsPhotoDetail>
    lateinit var onClickListener: View.OnClickListener

    init {
        list = mutableListOf()
    }

    fun updateList(updatedList: List<TagsPhotoDetail>) {
        this.list.clear()
        this.list.addAll(updatedList)
        AppLogger.errorLog(TAG, list.size.toString() + " list size")
        notifyDataSetChanged()
    }

    class TagsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tagName: TextView = itemView.findViewById(R.id.tagName)
        var defaultImage: ImageView = itemView.findViewById(R.id.defaultImage)
        var viewImage: Button = itemView.findViewById(R.id.viewImages)
        var viewGraph: Button = itemView.findViewById(R.id.viewGraph)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsViewHolder {
        val termView =
            LayoutInflater.from(parent.context).inflate(R.layout.tags_itemview, parent, false)
        return TagsViewHolder(termView)
    }

    override fun onBindViewHolder(holder: TagsViewHolder, position: Int) {
        var tags = list.get(position)
        holder.tagName.setText(tags.tagName)
        holder.defaultImage.setImageURI((Uri.parse(tags.defaultImageUri)))
        holder.viewImage.setTag(position)
        Glide.with(context).load(tags.defaultImageUri)
            .placeholder(context.resources.getDrawable(R.drawable.ic_imageplaceholder)).centerCrop()
            .into(holder.defaultImage)
        holder.viewImage.setOnClickListener(onClickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}