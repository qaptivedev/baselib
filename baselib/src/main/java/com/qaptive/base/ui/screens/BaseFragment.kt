package com.qaptive.base.ui.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.qaptive.base.R
import com.qaptive.base.models.LoadingMessage
import com.qaptive.base.models.Message
import com.qaptive.base.utils.EventObserver
import com.qaptive.base.viewmodel.BaseActivityViewModel
import com.qaptive.base.viewmodel.BaseViewModel

abstract class BaseFragment<T : ViewDataBinding, VM : BaseViewModel> : Fragment() {

    lateinit var binder: T

    var isPaused = false

    var pendingNavigationActionId = 0
    var pendingNavigationActionBundle: Bundle? = null
    var pendingNavigateUp = false
    var pendingNavigationIntent: Intent? = null
    var pendingNavigationActivityClass: Class<*>? = null
    var pendingNavigationFinishCurrent = false
    var pendingNavDirections: NavDirections? = null

    protected abstract val viewModel: VM
    protected val activityViewModel:BaseActivityViewModel by activityViewModels()

    abstract fun isFullScreen(): Boolean
    abstract fun navigationDrawerEnabled(): Boolean
    abstract fun floatingActionButtonRequired(): Boolean

    open fun floatingActionButton(): (() -> Unit)? {
        return null
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binder = DataBindingUtil.inflate(inflater, inflateLayout(), container, false)
        onBinderCreated()
        binder.lifecycleOwner = this
        return binder.root
    }

    abstract fun inflateLayout(): Int

    override fun onPause() {
        isPaused = true
        super.onPause()
    }

    override fun onResume() {
        isPaused = false
        super.onResume()
        activityViewModel.toggleFullScreen(isFullScreen())
        activityViewModel.toggleDrawerEnabled(navigationDrawerEnabled())
        activityViewModel.floatingActionButton(floatingActionButtonRequired())
        activityViewModel.floatingAction(floatingActionButton())
        continuePendingNavigation()
    }

    fun showInfo(context: Context, message: String, actionString: Int, onclick: () -> Unit) {
        val messageData = Message(context)
        messageData.messageStr = message
        messageData.positiveButtonRes = actionString
        messageData.positiveAction = onclick
        showInfo(messageData)
    }

    fun showInfo(context: Context, message: Int) {
        val messageData = Message(context)
        messageData.messageRes = message
        showInfo(messageData)
    }

    fun showInfo(message: String) {
        val messageData = Message()
        messageData.messageStr = message
        showInfo(messageData)
    }

    fun showLoading(context: Context, resId: Int = R.string.loading) {
        val loadingMessage = LoadingMessage(context)
        loadingMessage.messageRes = resId
        showLoading(loadingMessage)

    }

    fun showLoading(message: String) {
        val loadingMessage = LoadingMessage()
        loadingMessage.messageStr = message
        showLoading(loadingMessage)
    }

    open fun showLoading(loadingMessage: LoadingMessage) {
        activityViewModel.showLoading(loadingMessage)
    }

    open fun hideLoading() {
        activityViewModel.hideLoading()
    }

    fun showInfo(message: String, actionString: String = "Ok", onclick: () -> Unit) {
        showInfo(message = message, positiveButton = actionString, positiveAction = onclick)
    }

    fun showInfo(
        title: String? = null,
        message: String? = null,
        positiveButton: String? = null,
        positiveAction: (() -> Unit)? = null,
        negativeButton: String? = null,
        negativeAction: (() -> Unit)? = null,
        triggerActionOnDismiss: Boolean = false,
        canDismiss: Boolean = true,
        context: Context? = null
    ) {
        val messageData = Message(context)
        messageData.titleStr = title
        messageData.messageStr = message
        messageData.positiveButtonStr = positiveButton
        messageData.positiveAction = positiveAction
        messageData.negativeButtonStr = negativeButton
        messageData.negativeAction = negativeAction
        messageData.triggerActionOnDismiss = triggerActionOnDismiss
        messageData.canDismiss = canDismiss
        showInfo(messageData)
    }

    fun showInfo(
        context: Context,
        @StringRes title: Int? = null,
        @StringRes message: Int,
        @StringRes positiveButton: Int? = null,
        positiveAction: (() -> Unit)? = null,
        @StringRes negativeButton: Int? = null,
        negativeAction: (() -> Unit)? = null,
        triggerActionOnDismiss: Boolean = false,
        canDismiss: Boolean = true
    ) {
        val messageData = Message(context)
        messageData.titleRes = title
        messageData.messageRes = message
        messageData.positiveButtonRes = positiveButton
        messageData.positiveAction = positiveAction
        messageData.negativeButtonRes = negativeButton
        messageData.negativeAction = negativeAction
        messageData.triggerActionOnDismiss = triggerActionOnDismiss
        messageData.canDismiss = canDismiss
        showInfo(messageData)
    }

    open fun showInfo(messageData: Message) {
        activityViewModel.showInfo(messageData)
    }

    fun onNavigate(navigationActionId: Int, bundle: Bundle? = null) {
        if (isPaused) {
            pendingNavigationActionId = navigationActionId
            pendingNavigationActionBundle = bundle
            return
        }
        findNavController().navigate(navigationActionId, bundle)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.infoMessage.observe(viewLifecycleOwner, EventObserver {
            showInfo(it)
        })

        viewModel.loading.observe(viewLifecycleOwner, EventObserver {
            if (it.isLoading) {
                showLoading(it)
            } else {
                hideLoading()
            }
        })

        viewModel.action.observe(viewLifecycleOwner, EventObserver {
            performTask(it.task, it.any)
        })

        viewModel.navDirections.observe(viewLifecycleOwner, EventObserver {
            onNavigateAction(it)
        })

        viewModel.activityNavigation.observe(viewLifecycleOwner, EventObserver {
            if (it.intent != null) {
                onNavigateToActivity(it.intent!!, it.finishCurrent)
            } else {
                onNavigateToActivity(it.activityClass!!, it.finishCurrent)
            }
        })

        viewModel.upNavigation.observe(viewLifecycleOwner, EventObserver {
            onNavigateUp()
        })

        viewModel.navigate.observe(viewLifecycleOwner, EventObserver {
            onNavigate(it.id, it.bundle)
        })
        activityViewModel.actionPerformed.observe(viewLifecycleOwner, EventObserver {
            viewModel.taskPerformed(it)
        })
    }

    /***
     * Called after binder created
     */
    open fun onBinderCreated() {

    }

    fun onNavigateUp() {
        if (isPaused) {
            pendingNavigateUp = true
            return
        }
        if (!findNavController().navigateUp()) {
            activity?.onBackPressed()
        }
    }

    fun onNavigateAction(navigationActionId: NavDirections) {
        if (isPaused) {
            pendingNavDirections = navigationActionId
            return
        }
        findNavController().navigate(navigationActionId)
    }

    fun onNavigateToActivity(intent: Intent, finishCurrent: Boolean) {
        if (isPaused) {
            pendingNavigationIntent = intent
            pendingNavigationFinishCurrent = finishCurrent
            return
        }
        activity?.startActivity(intent)
        if (finishCurrent)
            activity?.finish()
    }

    fun onNavigateToActivity(activityClass: Class<*>, finishCurrent: Boolean) {
        if (isPaused) {
            pendingNavigationActivityClass = activityClass
            pendingNavigationFinishCurrent = finishCurrent
            return
        }
        val intent = Intent(activity, activityClass)
        activity?.startActivity(intent)
        if (finishCurrent)
            activity?.finish()

    }

    open fun performTask(task: Intent, any: Any?) {
        activityViewModel.performTask(task, any)
    }

    open fun toggleFloatingButtonSrc(@DrawableRes iconRes: Int) {
        activityViewModel.toggleFloatingButtonSrc(iconRes)
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
}