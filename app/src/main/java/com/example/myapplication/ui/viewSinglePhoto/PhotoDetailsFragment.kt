package com.example.myapplication.ui.viewSinglePhoto

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.myapplication.R
import com.example.myapplication.data.networkResponse.Photo
import com.example.myapplication.utility.Constants
import kotlinx.android.synthetic.main.fragment_details_photo.*

class PhotoDetailsFragment : Fragment() {

    private var mStartingPosition: Int? = 0
    private var mAlbumPosition: Int? = 0
    private var photo: Photo? = null

    companion object {

        fun newInstance(position: Int, startingPosition: Int, photo: Photo?): PhotoDetailsFragment {
            val bundle = Bundle()
            bundle.putInt(Constants.PHOTO_POSITION, position)
            bundle.putInt(Constants.START_ITEM_POSITION, startingPosition)
            bundle.putParcelable(Constants.PHOTO, photo)
            val fragment = PhotoDetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mStartingPosition = arguments?.getInt(Constants.START_ITEM_POSITION)
        mAlbumPosition = arguments?.getInt(Constants.PHOTO_POSITION)
        photo = arguments?.getParcelable(Constants.PHOTO)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details_photo, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageUrl = "https://farm${photo?.farm}.staticflickr.com/${photo?.server}" +
                "/${photo?.id}_${photo?.secret}_n.jpg"

        ivImage.transitionName = mAlbumPosition.toString()
        tvPhotoTitle.text=photo?.title

        activity?.let {
            Glide.with(it).asBitmap().load(imageUrl).listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?,
                                          isFirstResource: Boolean): Boolean {
                    activity?.startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?,
                                             dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    activity?.startPostponedEnterTransition()
                    return false
                }

            }).into(ivImage)
        }
    }


}