package com.example.taggingapplication.imagepreviewtags.tags

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.taggingapplication.R
import com.example.taggingapplication.managers.TagsPhotoDetail
import com.example.taggingapplication.models.TagsModel

class TagsListAdapter(context: Context, var list: List<TagsPhotoDetail>) :
    RecyclerView.Adapter<TagsListAdapter.TagsListViewHolder>() {
    lateinit var onClickListener: View.OnClickListener

    inner class TagsListViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var tagsName: TextView = itemview.findViewById(R.id.tagsName)
        var rootView: ConstraintLayout = itemview.findViewById(R.id.rootView)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsListViewHolder {
        val termView =
            LayoutInflater.from(parent.context).inflate(R.layout.tags_list_itemview, parent, false)
        return TagsListViewHolder(termView)
    }

    override fun onBindViewHolder(holder: TagsListViewHolder, position: Int) {
        var model = list.get(position)
        holder.tagsName.setText(model.tagName)
        holder.rootView.setTag(model.tagName)
        holder.rootView.setOnClickListener(onClickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}