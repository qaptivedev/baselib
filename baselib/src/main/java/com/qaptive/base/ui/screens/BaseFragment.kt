package com.qaptive.base.ui.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.qaptive.base.viewmodel.BaseViewModel
import com.qaptive.base.viewmodel.ViewModelCallBacks

abstract class BaseFragment<T : ViewDataBinding, VM : BaseViewModel> : Fragment(), ViewModelCallBacks {
    lateinit var binder: T
    var callBack: FragmentCallBacks? = null

    var isPaused=false

    var pendingNavigationActionId=0
    var pendingNavigationActionBundle:Bundle?=null
    var pendingNavigateUp=false
    var pendingNavigationIntent: Intent?=null
    var pendingNavigationActivityClass: Class<*>?=null
    var pendingNavigationFinishCurrent=false
    var pendingNavDirections:NavDirections?=null


    protected val vieModel: VM by lazy {
        if (isLifecycleOwnerActivity()) {
            ViewModelProvider(activity!!).get(getViewModelClass())
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binder = DataBindingUtil.inflate(inflater, inflateLayout(), container, false)
        onBinderCreated()
        binder.lifecycleOwner = this
        return binder.root
    }

    abstract fun inflateLayout(): Int

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentCallBacks) {
            callBack = context
        } else {
            throw Exception("FragmentActivityInterface not Implemented")
        }
    }

    override fun onPause() {
        isPaused = true
        super.onPause()
    }

    override fun onResume() {
        isPaused = false
        super.onResume()
        callBack?.toggleFullScreen(isFullScreen())
        callBack?.toggleDrawerEnabled(navigationDrawerEnabled())
        callBack?.floatingActionButton(floatingActionButtonRequired())
        callBack?.floatingAction(floatingActionButton())
        continuePendingNavigation()
    }

    override fun showInfo(message: String, actionString: Int, onclick: () -> Unit): AlertDialog? {
        return callBack?.showInfo(message, actionString, onclick)
    }

    override fun showInfo(message: Int):AlertDialog? {
        return callBack?.showInfo(message)
    }

    override fun showInfo(message: String):AlertDialog? {
        return callBack?.showInfo(message)
    }

    override fun showLoading(resId: Int) {
        callBack?.showLoading(resId)
    }

    override fun hideLoading() {
        callBack?.hideLoading()
    }

    override fun showLoading(message: String) {
        callBack?.showLoading(message)
    }

    override fun showInfo(message: String, actionString: String, onclick: () -> Unit) :AlertDialog?{
        return callBack?.showInfo(message, actionString, onclick)
    }

    override fun showInfo(
        title: String?,
        message: String,
        positiveButton: String?,
        positiveAction: (() -> Unit)?,
        negativeButton: String?,
        negativeAction: (() -> Unit)?,
        triggerActionOnDismiss: Boolean,
        canDismiss:Boolean
    ) :AlertDialog?{
        return callBack?.showInfo(title, message, positiveButton, positiveAction, negativeButton, negativeAction,triggerActionOnDismiss,canDismiss)
    }

    override fun showInfo(
        @StringRes title: Int?, @StringRes message: Int, @StringRes positiveButton: Int?, positiveAction: (() -> Unit)?,
        @StringRes negativeButton: Int?,
        negativeAction: (() -> Unit)?,
        triggerActionOnDismiss: Boolean,
        canDismiss:Boolean
    ) :AlertDialog?{
        return callBack?.showInfo(
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

    override fun onNavigate(navigationActionId: Int, bundle: Bundle?) {
        if(isPaused)
        {
            pendingNavigationActionId=navigationActionId
            pendingNavigationActionBundle=bundle
            return
        }
        findNavController().navigate(navigationActionId, bundle)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vieModel.actionCallBacks = this
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

    override fun onNavigateUp() {
        if(isPaused)
        {
            pendingNavigateUp=true
            return
        }
        if(!findNavController().navigateUp())
        {
            activity?.onBackPressed()
        }
    }

    override fun onNavigateAction(navigationActionId: NavDirections) {
        if(isPaused)
        {
            pendingNavDirections=navigationActionId
            return
        }
        findNavController().navigate(navigationActionId)
    }

    override fun onNavigateToActivity(intent: Intent, finishCurrent: Boolean) {
        if(isPaused)
        {
            pendingNavigationIntent=intent
            pendingNavigationFinishCurrent=finishCurrent
            return
        }
        activity?.startActivity(intent)
        if (finishCurrent)
            activity?.finish()
    }

    override fun onNavigateToActivity(activityClass: Class<*>, finishCurrent: Boolean) {
        if(isPaused)
        {
            pendingNavigationActivityClass=activityClass
            pendingNavigationFinishCurrent=finishCurrent
            return
        }
        val intent=Intent(activity,activityClass)
        activity?.startActivity(intent)
        if(finishCurrent)
            activity?.finish()

    }

    override fun performTask(task: Intent, any: Any?) {
        callBack?.performTask(task,any)
    }

    protected fun continuePendingNavigation()
    {
        if(pendingNavigationActionId!=0)
        {
            onNavigate(pendingNavigationActionId,pendingNavigationActionBundle)
        }
        else if(pendingNavDirections!=null)
        {
            onNavigateAction(pendingNavDirections!!)
        }
        else if(pendingNavigationIntent!=null)
        {
            onNavigateToActivity(pendingNavigationIntent!!,pendingNavigationFinishCurrent)
        }
        else if(pendingNavigationActivityClass!=null)
        {
            onNavigateToActivity(pendingNavigationActivityClass!!,pendingNavigationFinishCurrent)
        }
        else if(pendingNavigateUp)
        {
            onNavigateUp()
        }
        resentPendingState()
    }

    protected fun resentPendingState()
    {
        pendingNavigationActionId=0
        pendingNavigationActionBundle=null
        pendingNavigateUp=false
        pendingNavigationIntent=null
        pendingNavigationActivityClass=null
        pendingNavigationFinishCurrent=false
        pendingNavDirections=null
    }
}