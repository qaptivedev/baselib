package com.qaptive.base.ui.screens

import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.CollapsingToolbarLayout

interface ToolbarProvider {
    fun setUpToolbar(toolbar:Toolbar?, collapsingToolbarLayout:CollapsingToolbarLayout?=null)
}