package com.qaptive.base.ui.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.qaptive.base.R
import com.qaptive.base.ktx.ActivityHelper.enterFullScreen
import com.qaptive.base.ktx.ActivityHelper.exitFullScreen
import com.qaptive.base.utils.EventObserver
import com.qaptive.base.viewmodel.BaseActivityViewModel

abstract class BaseActivity : AppCompatActivity() {

    val viewModel:BaseActivityViewModel by viewModels()
    var isInFulScreen = false
    var isDrawerEnabled = true
    var isFloatingButtonEnabled = true
    var floatingAction: (() -> Unit)? = null

    var isPaused = false

    var pendingNavigationActionId = 0
    var pendingNavigationActionBundle: Bundle? = null
    var pendingNavigateUp = false
    var pendingNavigationIntent: Intent? = null
    var pendingNavigationActivityClass: Class<*>? = null
    var pendingNavigationFinishCurrent = false
    var pendingNavDirections: NavDirections? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.isFullScreen.observe(this, Observer { toggleFullScreen(it == true) })
        viewModel.enableDrawer.observe(this, Observer { toggleDrawerEnabled(it == true) })
        viewModel.hasFloatingAction.observe(this, Observer { floatingActionButton(it == true) })
        viewModel.floatingAction.observe(this, Observer { floatingAction(it?.task) })
        viewModel.floatingButtonSrc.observe(this, Observer {
            it?.let { toggleFloatingButtonSrc(it) }
        })
        viewModel.infoMessage.observe(this, EventObserver {
            if (it.context == null) {
                it.context = this
            }
            showDialog(
                it.getTitle(),
                it.getMessage(),
                it.getPositiveButton(),
                it.positiveAction,
                it.getNegativeButton(),
                it.negativeAction,
                it.getNeutralButton(),
                it.triggerActionOnDismiss,
                it.canDismiss
            )
        })

        viewModel.loading.observe(this, EventObserver {
            if (it.isLoading) {
                if (it.context == null) {
                    it.context = this
                }
                showLoading(
                    it.getTitle(),
                    it.getMessage(),
                    it.canDismiss
                )
            } else {
                hideLoading()
            }
        })

        viewModel.action.observe(this, EventObserver {
            performTask(it.task, it.any)
        })

        viewModel.navDirections.observe(this, EventObserver {
            onNavigateAction(it)
        })

        viewModel.activityNavigation.observe(this, EventObserver {
            if (it.intent != null) {
                onNavigateToActivity(it.intent!!, it.finishCurrent)
            } else {
                onNavigateToActivity(it.activityClass!!, it.finishCurrent)
            }
        })

        viewModel.upNavigation.observe(this, EventObserver {
            onNavigateUp()
        })

        viewModel.navigate.observe(this, EventObserver {
            onNavigate(it.id, it.bundle)
        })
    }

    private fun toggleFullScreen(enableFullScreen: Boolean) {
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

    override fun onPause() {
        isPaused = true
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        isPaused = false
        super.onResume()
        continuePendingNavigation()
    }

    private fun toggleDrawerEnabled(enableDrawer: Boolean) {
        if (enableDrawer) {
            if (!isDrawerEnabled) {
                isDrawerEnabled = true
                getNavigationView()?.isEnabled = false
                getDrawerLayout()?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                getActionBarDrawerToggle()?.isDrawerIndicatorEnabled = true
            }
        } else {
            if (isDrawerEnabled) {
                isDrawerEnabled = false
                getDrawerLayout()?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                getActionBarDrawerToggle()?.isDrawerIndicatorEnabled = false
            }
        }
    }

    private fun floatingActionButton(enableFloatingActionButton: Boolean) {
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

    private fun floatingAction(floatingAction: (() -> Unit)?) {
        if (floatingAction != null)
            this.floatingAction = floatingAction
    }

    open fun showDialog(
        title: String? = null,
        message: String?,
        positiveButton: String? = null,
        positiveAction: (() -> Unit)?,
        negativeButton: String? = null,
        negativeAction: (() -> Unit)?,
        neutralButton: String? = null,
        triggerActionOnDismiss: Boolean = false,
        dialogDismiss: Boolean = true
    ): AlertDialog {
        var isClicked = false
        val dialogBuilder = MaterialAlertDialogBuilder(this)
        dialogBuilder.setTitle(title ?: getString(R.string.default_dialogue_title))
        dialogBuilder.setMessage(message)
        positiveButton?.let {
            dialogBuilder.setPositiveButton(it) { dialog, _ ->
                isClicked = true
                positiveAction?.invoke()
                dialog?.dismiss()
            }
        }
        negativeButton?.let {
            dialogBuilder.setNegativeButton(it) { dialog, _ ->
                isClicked = true
                negativeAction?.invoke()
                dialog?.dismiss()
            }
        }

        neutralButton?.let {
            dialogBuilder.setNeutralButton(it) { dialog, _ ->
                isClicked = true
                dialog?.dismiss()
            }
        }
        if (negativeButton == null && positiveButton == null && neutralButton == null) {
            dialogBuilder.setNeutralButton(
                R.string.ok
            ) { dialog, _ ->
                isClicked = true
                dialog?.dismiss()
            }
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
        val dialog = dialogBuilder.create()
        runOnUiThread { dialog.show() }
        return dialog
    }

    open fun onNavigateAction(navigationActionId: NavDirections) {
        if (isPaused) {
            pendingNavDirections = navigationActionId
            return
        }
        getNavController().navigate(navigationActionId)
    }

    open fun onNavigateToActivity(intent: Intent, finishCurrent: Boolean) {
        if (isPaused) {
            pendingNavigationIntent = intent
            pendingNavigationFinishCurrent = finishCurrent
            return
        }
        startActivity(intent)
        if (finishCurrent)
            finish()
    }

    open fun onNavigateToActivity(activityClass: Class<*>, finishCurrent: Boolean) {
        if (isPaused) {
            pendingNavigationActivityClass = activityClass
            pendingNavigationFinishCurrent = finishCurrent
            return
        }
        val intent = Intent(this, activityClass)
        startActivity(intent)
        if (finishCurrent)
            finish()
    }

    fun onNavigate(navigationActionId: Int, bundle: Bundle? = null) {
        if (isPaused) {
            pendingNavigationActionId = navigationActionId
            pendingNavigationActionBundle = bundle
            return
        }
        getNavController().navigate(navigationActionId, bundle)
    }

    protected fun continuePendingNavigation() {
        if (pendingNavigationActionId != 0) {
            onNavigate(pendingNavigationActionId, pendingNavigationActionBundle)
        } else if (pendingNavDirections != null) {
            onNavigateAction(pendingNavDirections!!)
        } else if (pendingNavigationIntent != null) {
            onNavigateToActivity(pendingNavigationIntent!!, pendingNavigationFinishCurrent)
        } else if (pendingNavigationActivityClass != null) {
            onNavigateToActivity(pendingNavigationActivityClass!!, pendingNavigationFinishCurrent)
        } else if (pendingNavigateUp) {
            onNavigateUp()
        }
        resentPendingState()
    }

    protected fun resentPendingState() {
        pendingNavigationActionId = 0
        pendingNavigationActionBundle = null
        pendingNavigateUp = false
        pendingNavigationIntent = null
        pendingNavigationActivityClass = null
        pendingNavigationFinishCurrent = false
        pendingNavDirections = null
    }

    abstract fun getNavigationView(): NavigationView?
    abstract fun getDrawerLayout(): DrawerLayout?
    abstract fun getActionBarDrawerToggle(): ActionBarDrawerToggle?
    abstract fun getFloatingActionButton(): FloatingActionButton?
    abstract fun toggleFloatingButtonSrc(iconRes: Int)
    abstract fun showLoading(title: String? = null, message: String?, dialogDismiss: Boolean = true)
    abstract fun hideLoading()
    abstract fun performTask(task: Intent, any: Any?)
    abstract fun getNavController(): NavController
}