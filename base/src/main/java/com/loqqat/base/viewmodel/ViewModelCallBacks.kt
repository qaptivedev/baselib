package com.loqqat.base.viewmodel

import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.navigation.NavDirections
import com.loqqat.base.R

interface ViewModelCallBacks {
    fun showInfo(message: String, actionString: String = "Ok", onclick: () -> Unit)
    fun showInfo(message: String)
    fun showInfo(message: String, @StringRes actionString: Int = R.string.ok, onclick: () -> Unit)
    fun showInfo(@StringRes message: Int)
    fun showInfo(
        title: String? = null,
        message: String,
        positiveButton: String? = null,
        positiveAction: (() -> Unit)? = null,
        negativeButton: String? = null,
        negativeAction: (() -> Unit)? = null,
        triggerActionOnDismiss: Boolean=false,
        canDismiss:Boolean=true

    )
    fun showInfo(
        @StringRes title: Int? = null,
        @StringRes message: Int,
        @StringRes positiveButton: Int? = null,
        positiveAction: (() -> Unit)? = null,
        @StringRes negativeButton: Int? = null,
        negativeAction: (() -> Unit)? = null,
        triggerActionOnDismiss: Boolean=false,
        canDismiss:Boolean=true
    )

    fun onNavigate(navigationActionId: Int,bundle: Bundle?=null)
    fun onNavigateAction(navigationActionId: NavDirections)
    fun showLoading(message: String)
    fun showLoading(@StringRes resId: Int = R.string.loading)
    fun hideLoading()

    fun onNavigateToActivity(intent: Intent,finishCurrent:Boolean=false)
    fun onNavigateToActivity(activityClass:Class<*>,finishCurrent:Boolean=false)

    fun onNavigateUp()
}