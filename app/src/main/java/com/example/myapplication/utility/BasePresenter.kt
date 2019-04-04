package com.example.myapplication.utility

interface BasePresenter<in V : BaseView> {
    fun attachView(view: V)

    fun detachView()
}