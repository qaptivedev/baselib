package com.loqqat.base.ui.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.loqqat.base.viewmodel.BaseViewModel
import com.loqqat.base.viewmodel.ViewModelCallBacks

abstract class BaseFragment<T : ViewDataBinding, VM : BaseViewModel> : Fragment(), ViewModelCallBacks {
    lateinit var binder: T
    var callBack: FragmentCallBacks? = null

    protected val vieModel: VM by lazy {
        if (isLifecycleOwnerActivity()) {
            ViewModelProviders.of(activity!!).get(getViewModelClass())
        } else {
            ViewModelProviders.of(this).get(getViewModelClass())
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

    override fun onResume() {
        super.onResume()
        callBack?.toggleFullScreen(isFullScreen())
        callBack?.toggleDrawerEnabled(navigationDrawerEnabled())
        callBack?.floatingActionButton(floatingActionButtonRequired())
        callBack?.floatingAction(floatingActionButton())
    }

    override fun showInfo(message: String, actionString: Int, onclick: () -> Unit) {
        callBack?.showInfo(message, actionString, onclick)
    }

    override fun showInfo(message: Int) {

    }

    override fun showInfo(message: String) {

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

    override fun showInfo(message: String, actionString: String, onclick: () -> Unit) {
        callBack?.showInfo(message, actionString, onclick)
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
    ) {
        callBack?.showInfo(title, message, positiveButton, positiveAction, negativeButton, negativeAction,triggerActionOnDismiss,canDismiss)
    }

    override fun showInfo(
        @StringRes title: Int?, @StringRes message: Int, @StringRes positiveButton: Int?, positiveAction: (() -> Unit)?,
        @StringRes negativeButton: Int?,
        negativeAction: (() -> Unit)?,
        triggerActionOnDismiss: Boolean,
        canDismiss:Boolean
    ) {
        callBack?.showInfo(
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

    override fun    onNavigate(navigationActionId: Int, bundle: Bundle?) {
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
        findNavController().navigateUp()
    }

    override fun onNavigateAction(navigationActionId: NavDirections) {
        findNavController().navigate(navigationActionId)
    }

    override fun onNavigateToActivity(intent: Intent, finishCurrent: Boolean) {
        activity?.startActivity(intent)
        if (finishCurrent)
            activity?.finish()
    }

    override fun onNavigateToActivity(activityClass: Class<BaseActivity>, finishCurrent: Boolean) {
        val intent=Intent(activity,activityClass)
        activity?.startActivity(intent)
        if(finishCurrent)
            activity?.finish()

    }
}