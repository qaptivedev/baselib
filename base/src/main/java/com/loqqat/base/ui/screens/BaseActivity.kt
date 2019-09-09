package com.loqqat.base.ui.screens

import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.loqqat.base.R
import com.loqqat.base.ktx.ActivityHelper.enterFullScreen
import com.loqqat.base.ktx.ActivityHelper.exitFullScreen
import com.loqqat.base.utils.DisableableAppBarLayoutBehavior

abstract class BaseActivity:AppCompatActivity(),FragmentCallBacks,ToolbarProvider {
     var isInFulScreen = false
     var isDrawerEnabled = true
     var isFloatingButtonEnabled = true
     var floatingAction: (() -> Unit)? = null

    override fun toggleFullScreen(enableFullScreen: Boolean) {
        if (enableFullScreen) {
            if (!isInFulScreen) {
                enterFullScreen()
                isInFulScreen = true
            }
        } else {
            if (isInFulScreen) {
                exitFullScreen()
                isInFulScreen = false
            }
        }
    }

    override fun toggleDrawerEnabled(enableDrawer: Boolean) {
        if (enableDrawer) {
            if (!isDrawerEnabled) {
                isDrawerEnabled = true
                getNavigationView()?.isEnabled = false
                getDrawerLayout()?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                getActionBarDrawerToggle()?.isDrawerIndicatorEnabled=true
            }
        } else {
            if (isDrawerEnabled) {

                isDrawerEnabled = false
                getDrawerLayout()?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                getActionBarDrawerToggle()?.isDrawerIndicatorEnabled=false
            }
        }
    }

    override fun floatingActionButton(enableFloatingActionButton: Boolean) {
        if (enableFloatingActionButton) {
            if (!isFloatingButtonEnabled) {
                (getFloatingActionButton() as View?)?.visibility = View.VISIBLE
                isFloatingButtonEnabled = true
            }
        } else {
            if (isFloatingButtonEnabled) {
                (getFloatingActionButton() as View?)?.visibility = View.GONE
                isFloatingButtonEnabled = false
            }
        }
    }

    override fun floatingAction(floatingAction: (() -> Unit)?) {
        if (floatingAction != null)
            this.floatingAction = floatingAction
    }

    override fun showInfo(message: String, actionString: String?, onclick: () -> Unit) {
        showDialog(null, message, actionString, onclick, null, {})
    }

    override fun showInfo(message: String, resId: Int, onclick: () -> Unit) {
        showInfo(message, getString(resId), onclick)
    }

    override fun showInfo(message: String) {
        showInfo(message, null) {}
    }

    override fun showInfo(message: Int) {
        showInfo(getString(message))
    }

    override fun showInfo(
        title: String?,
        message: String,
        positiveButton: String?,
        positiveAction: (() -> Unit)?,
        negativeButton: String?,
        negativeAction: (() -> Unit)?,
        triggerActionOnDismiss: Boolean,
        canDismiss: Boolean
    ) {
        showDialog(title, message, positiveButton, positiveAction, negativeButton, negativeAction,triggerActionOnDismiss,canDismiss)
    }

    override fun showInfo(
        @StringRes title: Int?, @StringRes message: Int, @StringRes positiveButton: Int?, positiveAction: (() -> Unit)?, @StringRes negativeButton: Int?,
        negativeAction: (() -> Unit)?,
        triggerActionOnDismiss: Boolean,
        canDismiss: Boolean
    ) {
        var titleString: String? = null
        var positiveButtonString: String? = null
        var negativeButtonString: String? = null
        if (title != null) {
            titleString = getString(title)
        }
        if (positiveButton != null) {
            positiveButtonString = getString(positiveButton)
        }
        if (negativeButton != null) {
            negativeButtonString = getString(negativeButton)
        }
        showInfo(
            titleString,
            getString(message),
            positiveButtonString,
            positiveAction,
            negativeButtonString,
            negativeAction,
            triggerActionOnDismiss,
            canDismiss
        )
    }




    open fun showDialog(
        title: String? = null,
        message: String,
        positiveButton: String? = null,
        positiveAction: (() -> Unit)?,
        negativeButton: String? = null,
        negativeAction: (() -> Unit)?,
        triggerActionOnDismiss: Boolean=false,
        dialogDismiss:Boolean=true
    ) {
        var isClicked=false
        val dialogBuilder = AlertDialog.Builder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
        dialogBuilder.setTitle(title ?: getString(R.string.app_name))
        dialogBuilder.setMessage(message)
        positiveButton?.let {
            dialogBuilder.setCancelable(true)
            dialogBuilder.setPositiveButton(it) { dialog, _ ->
                isClicked=true
                positiveAction?.invoke()
                dialog?.dismiss()
            }
        }
        negativeButton?.let {
            dialogBuilder.setCancelable(true)
            dialogBuilder.setNegativeButton(it) { dialog, _ ->
                isClicked=true
                negativeAction?.invoke()
                dialog?.dismiss()
            }
        }
        if (negativeButton == null && positiveButton == null) {
            dialogBuilder.setCancelable(true)
            dialogBuilder.setNeutralButton(
                R.string.ok
            ) { dialog, _ ->
                isClicked=true
                dialog?.dismiss() }
        }
        if (triggerActionOnDismiss) {
            dialogBuilder.setOnDismissListener {
                if (negativeAction != null && !isClicked) {
                    negativeAction.invoke()
                } else if (positiveAction != null && !isClicked) {
                    positiveAction.invoke()
                }
            }
        }
        dialogBuilder.setCancelable(dialogDismiss)
        runOnUiThread { dialogBuilder.create().show() }

    }

    override fun setUpToolbar(
        toolbar: Toolbar,
        collapsingToolbarLayout: CollapsingToolbarLayout?
    ) {

    }

    abstract fun getNavigationView(): NavigationView?
    abstract fun getDrawerLayout(): DrawerLayout?
    abstract fun getActionBarDrawerToggle(): ActionBarDrawerToggle?
    abstract fun getFloatingActionButton(): FloatingActionButton?
}