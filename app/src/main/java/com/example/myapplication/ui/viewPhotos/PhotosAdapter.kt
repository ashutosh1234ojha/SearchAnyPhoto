package com.example.myapplication.ui.viewPhotos

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.data.networkResponse.Photo
import kotlinx.android.synthetic.main.list_item_image.view.*

class PhotosAdapter(private val context: Context, private val photosList: ArrayList<Photo?>,
                    private val imagesInterface: Interface) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var imageFormat = "n.jpg"
    private var flickrLink = "staticflickr.com"
    private var initials = "https://farm"

    override fun getItemCount(): Int {
        return photosList.size
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.transitionName = position.toString()
        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.color.colorGray)
        val imageUrl = "$initials${photosList[position]?.farm}.$flickrLink/${photosList[position]?.server}" +
                "/${photosList[position]?.id}_${photosList[position]?.secret}_$imageFormat"
        Glide.with(context).setDefaultRequestOptions(requestOptions).asBitmap().load(imageUrl)
                .into(holder.itemView.ivImage!!)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhotoViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_image, parent,
                false))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.ivImage.transitionName = adapterPosition.toString()
            itemView.ivImage.setOnClickListener {
                imagesInterface.onPhotoClick(adapterPosition, itemView.ivImage)
            }
        }
    }

    interface Interface {
        fun onPhotoClick(position: Int, ivImage: ImageView)
    }
}