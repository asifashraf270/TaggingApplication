package com.example.taggingapplication.imagepreviewtags.tags

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taggingapplication.R
import com.example.taggingapplication.models.TagsModel

class AddNewTagAdapter(var context: Context,var list:MutableList<TagsModel>) :
    RecyclerView.Adapter<AddNewTagAdapter.AddNewTagViewHolder>() {
    lateinit var onClickListener: View.OnClickListener


    inner class AddNewTagViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var imageView: ImageView = itemview.findViewById(R.id.imageView)
        var tagName: TextView = itemview.findViewById(R.id.tagName)
        var addTagImage: ImageView = itemview.findViewById(R.id.addTag)
        var rootView=itemView.findViewById<ConstraintLayout>(R.id.rootView)
    }
/*    fun updateList(list:List<TagsModel>){
        tagsList.clear()
        tagsList.addAll(list)
        notifyDataSetChanged()
    }*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddNewTagViewHolder {
        val termView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.add_adds_list_itemview, parent, false)
        return AddNewTagViewHolder(termView)
    }

    override fun onBindViewHolder(holder: AddNewTagViewHolder, position: Int) {
        var model = list.get(position)
        if (model.isAdd) {
            holder.addTagImage.visibility = View.VISIBLE
        } else {
            holder.addTagImage.visibility = View.GONE
        }
        Glide.with(context).load(model.imageUri)
            .placeholder(context.resources.getDrawable(R.drawable.ic_imageplaceholder)).centerCrop()
            .into(holder.imageView)
        holder.tagName.setText(model.tagName)
        holder.rootView.setOnClickListener({
            if(model.isAdd){
                model.isAdd=false
            }else{
                model.isAdd=true
            }
            notifyItemChanged(position)
        })


    }

    override fun getItemCount(): Int {
        return list.size
    }
}