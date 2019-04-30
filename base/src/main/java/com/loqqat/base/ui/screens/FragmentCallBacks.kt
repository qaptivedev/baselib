package com.loqqat.base.ui.screens

import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.loqqat.base.R

interface FragmentCallBacks {
    fun toggleFullScreen(enableFullScreen: Boolean)
    fun toggleDrawerEnabled(enableDrawer: Boolean)
    fun floatingActionButton(enableFloatingActionButton: Boolean)
    fun toggleFloatingButtonSrc(@DrawableRes iconRes:Int)
    fun showLoading(message: String)
    fun showLoading(@StringRes resId: Int = R.string.loading)
    fun floatingAction(floatingAction:(() -> Unit)?=null)
    fun hideLoading()
    fun showInfo(
        message: String,
        actionString: String? = null,
        onclick: () -> Unit
    )

    fun showInfo(
        message: String,
        @StringRes resId: Int,
        onclick: () -> Unit
    )

    fun showInfo(
        message: String
    )

    fun showInfo(
        @StringRes message: Int
    )

    fun showInfo(
        title: String? = null,
        message: String,
        positiveButton: String? = null,
        positiveAction: (() -> Unit)? = null,
        negativeButton: String? = null,
        negativeAction: (() -> Unit)? = null,
        triggerActionOnDismiss: Boolean=false
    )

    fun showInfo(
        @StringRes title: Int? = null,
        @StringRes message: Int,
        @StringRes positiveButton: Int? = null,
        positiveAction: (() -> Unit)? = null,
        @StringRes negativeButton: Int? = null,
        negativeAction: (() -> Unit)? = null,
        triggerActionOnDismiss: Boolean=false
    )

    fun performTask(task:Intent,any: Any?)

//    fun startNfcScan(nfcCallBack: NFCCallBack)
//    fun stopNfcScan()
}