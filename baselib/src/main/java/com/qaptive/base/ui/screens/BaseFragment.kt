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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.qaptive.base.R
import com.qaptive.base.models.LoadingMessage
import com.qaptive.base.models.Message
import com.qaptive.base.utils.EventObserver
import com.qaptive.base.viewmodel.BaseActivityViewModel
import com.qaptive.base.viewmodel.BaseViewModel

abstract class BaseFragment<T : ViewDataBinding, VM : BaseViewModel> : Fragment(){
    lateinit var binder: T

    var isPaused = false

    var pendingNavigationActionId = 0
    var pendingNavigationActionBundle: Bundle? = null
    var pendingNavigateUp = false
    var pendingNavigationIntent: Intent? = null
    var pendingNavigationActivityClass: Class<*>? = null
    var pendingNavigationFinishCurrent = false
    var pendingNavDirections: NavDirections? = null


    protected val viewModel: VM by lazy {
        createViewModel()
    }

    protected val activityViewModel by lazy {
        ViewModelProvider(requireActivity()).get(BaseActivityViewModel::class.java)
    }

    open fun createViewModel(): VM {
        return if (isLifecycleOwnerActivity()) {
            ViewModelProvider(requireActivity()).get(getViewModelClass())
        } else {
            ViewModelProvider(this).get(getViewModelClass())
        }
    }

    abstract fun isFullScreen(): Boolean
    abstract fun navigationDrawerEnabled(): Boolean
    abstract fun floatingActionButtonRequired(): Boolean
    abstract fun getViewModelClass(): Class<VM>

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

    open fun showInfo(context: Context, message: String, actionString: Int, onclick: () -> Unit) {
        activityViewModel.showInfo(context, message, actionString, onclick)
    }

    open fun showInfo(context: Context, message: Int) {
        activityViewModel.showInfo(context, message)
    }

    open fun showInfo(message: String) {
        activityViewModel.showInfo(message)
    }

    open fun showLoading(context: Context, resId: Int = R.string.loading) {
        activityViewModel.showLoading(context, resId)
    }

    open fun showLoading(loadingMessage: LoadingMessage) {
        activityViewModel.showLoading(loadingMessage)
    }

    open fun hideLoading() {
        activityViewModel.hideLoading()
    }

    open fun showLoading(message: String) {
        activityViewModel.showLoading(message)
    }

    open fun showInfo(message: String, actionString: String = "Ok", onclick: () -> Unit) {
        activityViewModel.showInfo(message, actionString, onclick)
    }

    open fun showInfo(
        title: String?=null,
        message: String?=null,
        positiveButton: String?=null,
        positiveAction: (() -> Unit)?=null,
        negativeButton: String?=null,
        negativeAction: (() -> Unit)?=null,
        triggerActionOnDismiss: Boolean=false,
        canDismiss: Boolean=true
    ) {
        activityViewModel.showInfo(
            title,
            message,
            positiveButton,
            positiveAction,
            negativeButton,
            negativeAction,
            triggerActionOnDismiss,
            canDismiss
        )
    }

    open fun showInfo(
        context: Context,
        @StringRes title: Int?=null,
        @StringRes message: Int,
        @StringRes positiveButton: Int?=null,
        positiveAction: (() -> Unit)?=null,
        @StringRes negativeButton: Int?=null,
        negativeAction: (() -> Unit)?=null,
        triggerActionOnDismiss: Boolean=false,
        canDismiss: Boolean=true
    ) {
        activityViewModel.showInfo(
            context,
            title,
            message,
            positiveButton,
            positiveAction,
            negativeButton,
            negativeAction,
            triggerActionOnDismiss,
            canDismiss
        )
    }

    open fun showInfo(messageData: Message){
        activityViewModel.showInfo(messageData)
    }

    fun onNavigate(navigationActionId: Int, bundle: Bundle?=null) {
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
            showInfo(
                it.getTitle(),
                it.getMessage(),
                it.getPositiveButton(),
                it.positiveAction,
                it.getNegativeButton(),
                it.negativeAction,
                it.triggerActionOnDismiss,
                it.canDismiss
            )
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isLoading) {
                    showLoading(it)
                } else {
                    hideLoading()
                }
            }
        })

        viewModel.action.observe(viewLifecycleOwner, EventObserver {
            performTask(it.task, it.any)
        })

        viewModel.navDirections.observe(viewLifecycleOwner,EventObserver{
            onNavigateAction(it)
        })

        viewModel.activityNavigation.observe(viewLifecycleOwner,EventObserver{
            if(it.intent!=null)
            {
                onNavigateToActivity(it.intent!!,it.finishCurrent)
            }
            else{
                onNavigateToActivity(it.activityClass!!,it.finishCurrent)
            }
        })

        viewModel.upNavigation.observe(viewLifecycleOwner,EventObserver{
            onNavigateUp()
        })

        viewModel.navigate.observe(viewLifecycleOwner,EventObserver{
            onNavigate(it.id,it.bundle)
        })
        activityViewModel.actionPerformed.observe(viewLifecycleOwner,EventObserver{
            viewModel.taskPerformed(it)
        })
    }

    /**
     * check if lifecycle owner of ViewModel activity
     * @return [Boolean] true if owner if activity , false by default
     */
    open fun isLifecycleOwnerActivity(): Boolean {
        return false
    }

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

    open fun toggleFloatingButtonSrc(@DrawableRes iconRes:Int) {
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