package com.qaptive.base.models

import android.content.Context

open class LoadingMessage(context: Context?=null):Message(context) {
    var isLoading :Boolean =true
}