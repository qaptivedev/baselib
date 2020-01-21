package com.qaptive.base.ui.adapters

/**
 * Interface for handling item click on lists
 * @param T Model used in the list
 */
interface ItemClickCallBack<T> {
    fun onClick(item:T)
}