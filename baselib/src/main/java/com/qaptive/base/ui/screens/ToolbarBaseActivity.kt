package com.qaptive.base.ui.screens

import androidx.appcompat.widget.Toolbar
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.CollapsingToolbarLayout

abstract class ToolbarBaseActivity:BaseActivity(),ToolbarProvider {
    abstract fun getAppBarConfiguration(): AppBarConfiguration

    override fun setUpToolbar(
        toolbar: Toolbar?,
        collapsingToolbarLayout: CollapsingToolbarLayout?
    ) {
        if(toolbar==null)
            return
        setSupportActionBar(toolbar)
        if (collapsingToolbarLayout != null)
            NavigationUI.setupWithNavController(
                collapsingToolbarLayout,
                toolbar,
                getNavController(),
                getAppBarConfiguration()
            )
        else
            NavigationUI.setupWithNavController(
                toolbar,
                getNavController(),
                getAppBarConfiguration()
            )
    }
}