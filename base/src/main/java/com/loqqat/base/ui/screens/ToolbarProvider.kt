package com.loqqat.base.ui.screens

import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.CollapsingToolbarLayout

interface ToolbarProvider {
    fun setUpTooolbar(toolbar:Toolbar,collapsingToolbarLayout:CollapsingToolbarLayout?=null)
}