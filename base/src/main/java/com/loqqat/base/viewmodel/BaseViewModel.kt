package com.loqqat.base.viewmodel

import androidx.lifecycle.ViewModel
import com.loqqat.base.utils.Log

abstract class BaseViewModel:ViewModel() {
    var actionCallBacks:ViewModelCallBacks?=null


    override fun onCleared() {
        actionCallBacks=null
        Log.v("Log","Clear VM : ${javaClass.canonicalName}")
        super.onCleared()
    }
}