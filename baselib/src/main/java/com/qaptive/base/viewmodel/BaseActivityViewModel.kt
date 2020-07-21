package com.qaptive.base.viewmodel

import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.qaptive.base.models.Action
import com.qaptive.base.models.Event
import com.qaptive.base.models.Task

class BaseActivityViewModel:BaseViewModel() {
    private val isFullScreenLiveData = MutableLiveData<Boolean>()
    val isFullScreen: LiveData<Boolean> = isFullScreenLiveData

    private val enableDrawerLiveData = MutableLiveData<Boolean>()
    val enableDrawer: LiveData<Boolean> = enableDrawerLiveData

    private val hasFloatingActionButtonLiveData = MutableLiveData<Boolean>()
    val hasFloatingAction: LiveData<Boolean> = hasFloatingActionButtonLiveData

    private val floatingButtonSrcLiveData = MutableLiveData<Int>()
    val floatingButtonSrc:LiveData<Int> = floatingButtonSrcLiveData

    private val floatingActionLiveData = MutableLiveData<Task<Unit>>()
    val floatingAction:LiveData<Task<Unit>> = floatingActionLiveData

    private val actionPerformedLiveData = MutableLiveData<Event<Action>>()
    val actionPerformed:LiveData<Event<Action>> = actionPerformedLiveData

    fun toggleFullScreen(enableFullScreen: Boolean)
    {
        isFullScreenLiveData.postValue(enableFullScreen)
    }

    fun toggleDrawerEnabled(enableDrawer: Boolean)
    {
        enableDrawerLiveData.postValue(enableDrawer)
    }

    fun floatingActionButton(enableFloatingActionButton: Boolean)
    {
        hasFloatingActionButtonLiveData.postValue(enableFloatingActionButton)
    }

    fun toggleFloatingButtonSrc(@DrawableRes iconRes:Int){
        floatingButtonSrcLiveData.postValue(iconRes)
    }

    fun floatingAction(floatingAction:(() -> Unit)?=null){
        var task:Task<Unit>?=null
        floatingAction?.let {
            task=Task(it)
        }
        floatingActionLiveData.postValue(task)
    }

    override fun taskPerformed(action: Action) {
        super.taskPerformed(action)
        actionPerformedLiveData.postValue(Event(action))
    }
}