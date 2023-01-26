package com.photoeditor.photoeffect.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.piceditor.R

class ResizeAdapter(context: Context, resizeClickListener: OnResizeClickListener) :
    RecyclerView.Adapter<ResizeAdapter.ResizeHolder>() {

    var resizeListener: OnResizeClickListener = resizeClickListener

    interface OnResizeClickListener {
        fun onResizeClick(position: Int)
    }

    var mContext = context
    var texts = arrayOf(
        "Original",
        "Free",
        "1:1",
        "4:5",
        "2:3",
        "3:2",
        "3:4",
        "4:3",
        "1:2",
        "2:1",
        "9:16",
        "16:9"
    )
    var imgs = arrayOf(
        R.drawable.empty,
        R.drawable.empty,
        R.drawable.instagram_icon,
        R.drawable.instagram_icon,
        R.drawable.pinterest_icon,
        R.drawable.empty,
        R.drawable.empty,
        R.drawable.facebook_icon,
        R.drawable.empty,
        R.drawable.empty,
        R.drawable.tiktok_icon,
        R.drawable.youtube_icon
    )
    var selectedindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResizeHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.item_resize, parent, false)
        return ResizeHolder(view)
    }

    override fun getItemCount(): Int {
        return texts.size
    }

    override fun onBindViewHolder(holder: ResizeHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.item_resize.setText(texts[position])
        holder.imgs_src.setImageResource(imgs[position]!!)
        if (selectedindex == position) {
            holder.item_resize.setTextColor(mContext.resources.getColor(R.color.colorAccent))
        } else {
            holder.item_resize.setTextColor(mContext.resources.getColor(R.color.white))
        }

        holder.ll_resize.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                selectedindex = position
                resizeListener.onResizeClick(position)
                notifyDataSetChanged()
            }

        })
    }

    class ResizeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item_resize: TextView = itemView.findViewById(R.id.item_resize)
        var imgs_src: ImageView = itemView.findViewById(R.id.imgs)
        var ll_resize: LinearLayout = itemView.findViewById(R.id.ll_resize)
    }
}