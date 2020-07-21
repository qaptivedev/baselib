package com.qaptive.base.models

import android.content.Intent

internal class ActivityNavigationModel {
    var intent: Intent? = null
    var finishCurrent: Boolean = false
    var activityClass: Class<*>? = null
}