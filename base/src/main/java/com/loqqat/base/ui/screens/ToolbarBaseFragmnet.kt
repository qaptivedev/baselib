package com.loqqat.base.ui.screens

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.loqqat.base.viewmodel.BaseViewModel

abstract class ToolbarBaseFragmnet<T : ViewDataBinding, VM : BaseViewModel>:BaseFragment<T,VM>() {

    abstract fun getToolbar(): Toolbar?

    abstract fun getCollapsingToolbarLayout(): CollapsingToolbarLayout?

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(activity!=null)
        {
            (activity as ToolbarProvider).setUpToolbar(getToolbar(),getCollapsingToolbarLayout())
            activity?.title = ""
        }
    }
}