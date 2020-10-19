package com.qaptive.base.ktx

import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

object ActivityHelper{

    private fun AppCompatActivity.enterFullScreenForSplash()
    {
        var flags=(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            flags=flags or View.SYSTEM_UI_FLAG_IMMERSIVE
        }
        window.decorView.systemUiVisibility=flags
        supportActionBar?.hide()
    }

    /**
     * Enter activity to full screen
     */
    private fun AppCompatActivity.enterFullScreen()
    {
        var flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            flags=flags or View.SYSTEM_UI_FLAG_IMMERSIVE
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility=flags
        supportActionBar?.hide()
    }

    /**
     * Enter activity to full screen
     */
    fun AppCompatActivity.enterFullScreen(hideNavigation: Boolean=false)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_SWIPE
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.hide(WindowInsets.Type.statusBars())
            supportActionBar?.hide()
            if (hideNavigation) {
                window.insetsController?.hide(WindowInsets.Type.navigationBars())
            }
        }else{
            if(hideNavigation){
                enterFullScreenForSplash()
            }else{
                enterFullScreen()
            }
        }
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.

    /**
     * Exit Full screen for the activity
     */
    fun AppCompatActivity.exitFullScreen()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(true)
            window.insetsController?.show(WindowInsets.Type.statusBars())
            window.insetsController?.show(WindowInsets.Type.navigationBars())
            supportActionBar?.show()
            window.insetsController?.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_TOUCH
        }else{
            window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            supportActionBar?.show()
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    @Deprecated("use com.qaptive.base.ktx.ByteArrayHelper.byteArrayToHexStringReversed",replaceWith = ReplaceWith("byteArrayToHexStringReversed()","com.qaptive.base.ktx.ByteArrayHelper.byteArrayToHexStringReversed"),DeprecationLevel.ERROR)
    fun ByteArray.byteArrayToHexStringReversed():String
    {
        var i: Int
        var j = size-1
        var `in`: Int
        val hex = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")
        var out = ""

        while (j >= 0) {
            `in` = get(j).toInt() and 0xff
            i = `in` shr 4 and 0x0f
            out += hex[i]
            i = `in` and 0x0f
            out += hex[i]
            --j
        }
        return out
    }

    @Deprecated("use com.qaptive.base.ktx.ByteArrayHelper.byteArrayToHexString",replaceWith = ReplaceWith("byteArrayToHexString()","com.qaptive.base.ktx.ByteArrayHelper.byteArrayToHexString"),DeprecationLevel.ERROR)
    fun ByteArray.byteArrayToHexString():String
    {
        var i: Int
        var j = 0
        var `in`: Int
        val hex = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")
        var out = ""

        while (j < size) {
            `in` = get(j).toInt() and 0xff
            i = `in` shr 4 and 0x0f
            out += hex[i]
            i = `in` and 0x0f
            out += hex[i]
            ++j
        }
        return out
    }
}