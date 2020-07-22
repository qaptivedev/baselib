package com.qaptive.base.models

import android.content.Intent

class ActivityNavigationModel {
    var intent: Intent? = null
    var finishCurrent: Boolean = false
    var activityClass: Class<*>? = null
}