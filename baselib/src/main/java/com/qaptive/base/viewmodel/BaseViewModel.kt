package com.qaptive.base.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.qaptive.base.R
import com.qaptive.base.models.*
import com.qaptive.base.models.ActivityNavigationModel
import com.qaptive.base.utils.Log

abstract class BaseViewModel:ViewModel() {


    private val infoMessageLiveData = MutableLiveData<Event<Message>>()
    val infoMessage: LiveData<Event<Message>> = infoMessageLiveData

    private val actionLiveData = MutableLiveData<Event<Action>>()
    val action:LiveData<Event<Action>> = actionLiveData

    private val loadingLiveData = MutableLiveData<LoadingMessage>()
    val loading:LiveData<LoadingMessage> = loadingLiveData

    private val navDirectionsLiveData =MutableLiveData<Event<NavDirections>>()
    val navDirections:LiveData<Event<NavDirections>> = navDirectionsLiveData

    private val navigateLiveData =MutableLiveData<Event<NavigationModel>>()
    val navigate:LiveData<Event<NavigationModel>> = navigateLiveData

    private val upNavigationLiveData = MutableLiveData<Event<Boolean>>()
    internal val upNavigation:LiveData<Event<Boolean>> =upNavigationLiveData

    private val activityNavigationLiveData = MutableLiveData<Event<ActivityNavigationModel>>()
    internal val activityNavigation:LiveData<Event<ActivityNavigationModel>> = activityNavigationLiveData



    fun showInfo(message: String, actionString: String = "Ok", onclick: () -> Unit) {
        showInfo(null, message, actionString, onclick, null)
    }

    fun showInfo(message: String) {
        showInfo(null, message, null, null, null, {})
    }

    fun showInfo(
        context: Context,
        message: String,
        @StringRes actionString: Int = R.string.ok,
        onclick: () -> Unit
    ) {
        val messageData = Message(context)
        messageData.messageStr = message
        messageData.positiveButtonRes = actionString
        messageData.positiveAction = onclick
        showInfo(messageData)
    }

    fun showInfo(context: Context, @StringRes message: Int) {
        showInfo(context = context, title = null, message = message)
    }

    fun showInfo(
        title: String? = null,
        message: String?,
        positiveButton: String? = null,
        positiveAction: (() -> Unit)? = null,
        negativeButton: String? = null,
        negativeAction: (() -> Unit)? = null,
        triggerActionOnDismiss: Boolean = false,
        canDismiss: Boolean = true

    ) {
        val messageData = Message()
        messageData.titleStr = title
        messageData.messageStr = message
        messageData.positiveButtonStr = positiveButton
        messageData.positiveAction = positiveAction
        messageData.negativeButtonStr = negativeButton
        messageData.negativeAction = negativeAction
        messageData.triggerActionOnDismiss = triggerActionOnDismiss
        messageData.canDismiss = canDismiss
        showInfo(messageData)
    }

    fun showInfo(
        context: Context,
        @StringRes title: Int? = null,
        @StringRes message: Int,
        @StringRes positiveButton: Int? = null,
        positiveAction: (() -> Unit)? = null,
        @StringRes negativeButton: Int? = null,
        negativeAction: (() -> Unit)? = null,
        triggerActionOnDismiss: Boolean = false,
        canDismiss: Boolean = true
    ) {
        val messageData = Message(context)
        messageData.titleRes = title
        messageData.messageRes = message
        messageData.positiveButtonRes = positiveButton
        messageData.positiveAction = positiveAction
        messageData.negativeButtonRes = negativeButton
        messageData.negativeAction = negativeAction
        messageData.triggerActionOnDismiss = triggerActionOnDismiss
        messageData.canDismiss = canDismiss
        showInfo(messageData)
    }

    fun showInfo(messageData: Message) {
        infoMessageLiveData.postValue(Event(messageData))
    }

    fun showLoading(message: String) {
        val loadingMessage=LoadingMessage()
        loadingMessage.messageStr=message
        showLoading(loadingMessage)
    }

    fun showLoading(context: Context?=null, @StringRes resId: Int = R.string.loading) {
        val loadingMessage=LoadingMessage(context)
        loadingMessage.messageRes=resId
        showLoading(loadingMessage)
    }

    open fun showLoading(loadingMessage:LoadingMessage) {
        loadingLiveData.postValue(loadingMessage)
    }

    open fun hideLoading() {
        val loadingMessage= LoadingMessage()
        loadingMessage.isLoading=false
        loadingLiveData.postValue(loadingMessage)
    }

    fun onNavigateToActivity(intent: Intent, finishCurrent:Boolean=false)
    {
        val navigationModel=ActivityNavigationModel()
        navigationModel.intent=intent
        navigationModel.finishCurrent=finishCurrent
        activityNavigationLiveData.postValue(Event(navigationModel))
    }

    fun onNavigateToActivity(activityClass:Class<*>,finishCurrent:Boolean=false)
    {
        val navigationModel= ActivityNavigationModel()
        navigationModel.activityClass=activityClass
        navigationModel.finishCurrent=finishCurrent
        activityNavigationLiveData.postValue(Event(navigationModel))
    }

    fun onNavigate(@IdRes id:Int, bundle: Bundle?=null)
    {
        navigateLiveData.postValue(Event(NavigationModel(id,bundle)))
    }

    fun onNavigateUp()
    {
        upNavigationLiveData.postValue(Event(true))
    }

    fun onNavigateAction(navigationActionId: NavDirections){
        navDirectionsLiveData.postValue(Event(navigationActionId))
    }

    fun performTask(task: Intent, any: Any?) {
        actionLiveData.postValue(Event(Action(task,any)))
    }

    open fun taskPerformed(action: Action){

    }

    override fun onCleared() {
        Log.v("Log", "Clear VM : ${javaClass.canonicalName}")
        super.onCleared()
    }
}