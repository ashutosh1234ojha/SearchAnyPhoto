package com.example.myapplication.ui.viewPhotos

import com.example.myapplication.data.networkResponse.CommonResponse
import com.example.myapplication.utility.BasePresenterImpl
import com.example.myapplication.utility.Constants
import com.example.myapplication.networking.RestClient
import com.example.myapplication.networking.NetworkConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewPhotosPresenter : BasePresenterImpl<ViewPhotosContract.View>(), ViewPhotosContract.Presenter {

    override fun hitApiGetPhotos(showProgress: Boolean, pageNo: Int, searchText: String) {
        val map = HashMap<String, String>()
        map[NetworkConstants.METHOD] = Constants.API_METHOD
        map[NetworkConstants.API_KEY] = Constants.API_KEY
        map[NetworkConstants.NO_JSON_CALLBACK] = Constants.VALUE_1
        map[NetworkConstants.FORMAT] = Constants.JSON
        map[NetworkConstants.PER_PAGE] = Constants.VALUE_48
        map[NetworkConstants.TEXT] = searchText
        map[NetworkConstants.PAGE] = pageNo.toString()

        if (showProgress)
            getView()?.showLoading()

        RestClient.get()?.getImages(map)?.enqueue(object : Callback<CommonResponse> {

            override fun onResponse(call: Call<CommonResponse>?, response: Response<CommonResponse>) {
                if (showProgress)
                    getView()?.dismissLoading()
                if (response.isSuccessful) {
                    getView()?.setApiResponse(response.body()?.photos?.photo)
                } else {
                    getView()?.errorToast()
                }
            }

            override fun onFailure(call: Call<CommonResponse>?, t: Throwable?) {
                if (showProgress)
                    getView()?.dismissLoading()
                getView()?.errorToast()
            }

        })
    }

}