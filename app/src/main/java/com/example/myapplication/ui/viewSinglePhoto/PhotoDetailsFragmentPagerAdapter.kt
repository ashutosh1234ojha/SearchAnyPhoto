package com.example.myapplication.ui.viewSinglePhoto

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import com.example.myapplication.data.networkResponse.Photo

class PhotoDetailsFragmentPagerAdapter(fm: FragmentManager, private val startingPosition: Int,
                                       private val photosList: ArrayList<Photo?>) :
        FragmentStatePagerAdapter(fm) {

    lateinit var mCurrentDetailsFragment : PhotoDetailsFragment

    override fun getItem(position: Int): Fragment {
        return PhotoDetailsFragment.newInstance(position, startingPosition, photosList[position])
    }

    override fun getCount(): Int {
        return photosList.size
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        mCurrentDetailsFragment = `object` as PhotoDetailsFragment
    }
}