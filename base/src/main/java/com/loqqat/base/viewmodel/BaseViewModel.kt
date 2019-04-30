package com.loqqat.base.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

abstract class BaseViewModel:ViewModel() {
    var actionCallBacks:ViewModelCallBacks?=null


    override fun onCleared() {
        actionCallBacks=null
        Log.v("Log","Clear VM : ${javaClass.canonicalName}")
        super.onCleared()
    }
}