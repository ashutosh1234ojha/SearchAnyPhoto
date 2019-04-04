package com.example.myapplication.ui.viewPhotos

import com.example.myapplication.data.networkResponse.Photo
import com.example.myapplication.utility.BasePresenter
import com.example.myapplication.utility.BaseView

class ViewPhotosContract {

    interface View : BaseView {

        fun showLoading()

        fun dismissLoading()
        fun setListeners()

        fun onSearchClick()

        fun errorToast()


        fun setApiResponse(photo: ArrayList<Photo>?)
    }

    interface Presenter : BasePresenter<View> {
        fun hitApiGetPhotos(showProgress: Boolean, pageNo: Int, searchText: String)
    }


}