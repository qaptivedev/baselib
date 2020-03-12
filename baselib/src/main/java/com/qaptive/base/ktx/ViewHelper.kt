package com.qaptive.base.ktx

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object ViewHelper {

    fun EditText.hideKeyboard(){
        val im = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        im.hideSoftInputFromWindow(windowToken,0)
    }

    fun EditText.showKeyboard(){
        val im = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        im.showSoftInput(this,0)
        requestFocus()
    }
}