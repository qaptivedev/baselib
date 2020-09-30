package com.qaptive.base.ktx

import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity

object ActivityHelper{

    /**
     * Enter activity to full screen
     */
    fun AppCompatActivity.enterFullScreen(hideNavigation: Boolean=false)
    {
        window.insetsController?.systemBarsBehavior =
            WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_SWIPE
        window.setDecorFitsSystemWindows(false)
        window.insetsController?.hide(WindowInsets.Type.statusBars())
        supportActionBar?.hide()
        if(hideNavigation){
            window.insetsController?.hide(WindowInsets.Type.navigationBars())
        }
    }

    /**
     * Exit Full screen for the activity
     */
    fun AppCompatActivity.exitFullScreen()
    {
        window.setDecorFitsSystemWindows(true)
        window.insetsController?.show(WindowInsets.Type.statusBars())
        window.insetsController?.show(WindowInsets.Type.navigationBars())
        supportActionBar?.show()
        window.insetsController?.systemBarsBehavior =
            WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_TOUCH

    }

    @Deprecated("use exitFullScreen(hideNavigation: Boolean)",replaceWith = ReplaceWith("exitFullScreen(true)"),DeprecationLevel.ERROR)
    fun AppCompatActivity.enterFullScreenForSplash()
    {

    }

    @Deprecated("use com.qaptive.base.ktx.ByteArrayHelper.byteArrayToHexStringReversed",replaceWith = ReplaceWith("byteArrayToHexStringReversed()","com.qaptive.base.ktx.ByteArrayHelper.byteArrayToHexStringReversed"),DeprecationLevel.ERROR)
    fun ByteArray.byteArrayToHexStringReversed():String
    {
        return ""
    }

    @Deprecated("use com.qaptive.base.ktx.ByteArrayHelper.byteArrayToHexString",replaceWith = ReplaceWith("byteArrayToHexString()","com.qaptive.base.ktx.ByteArrayHelper.byteArrayToHexString"),DeprecationLevel.ERROR)
    fun ByteArray.byteArrayToHexString():String
    {
        return ""
    }
}