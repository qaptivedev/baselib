package com.qaptive.base.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Base Class for RecyclerView Adapters, all the general handling for adapter are done here, Like adding , appending ,removing Items etc.
 *
 * @param T Data type or model of adapter holding , an ArrayList<T> will be crated
 * @param V ViewHolder of the adapter
 * @property data initial data set for the adapter
 *@constructor Creates an RecyclerView.Adapter with provided data
 */
abstract class BaseRecyclerViewAdapter<T, V : RecyclerView.ViewHolder>(data: ArrayList<T>? = null) :
    RecyclerView.Adapter<V>() {

    var data: ArrayList<T>? = null
    var emptyView: View? = null
    var progressBar: View? = null
    var errorView: View? = null

    init {
        data?.let {
            this.data = ArrayList(data)
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     *override from RecyclerView.Adapter
     *
     * @return The total number of items in this adapter. if data set is null returns 0
     */
    override fun getItemCount(): Int {
        /*
       For showing empty view as per data size
     */
        emptyView?.visibility =
            if (data.isNullOrEmpty() && progressBar?.visibility ?: View.GONE == View.GONE) View.VISIBLE else View.GONE
        return if (progressBar?.visibility == View.VISIBLE)
            0
        else data?.size ?: 0
    }

    /*
       For showing loader in timeline and contact fragment only
     */
    open fun showLoading() {
        emptyView?.visibility = View.GONE
        errorView?.visibility = View.GONE
        progressBar?.visibility = View.VISIBLE
        notifyDataSetChanged()
    }

    open fun hideLoading() {
        progressBar?.visibility = View.GONE
        errorView?.visibility = View.GONE
        emptyView?.visibility = if (data?.size == 0) View.VISIBLE else View.GONE
    }

    /*
    For showing error view in timeline and contact fragment only
  */
    open fun showError() {
        emptyView?.visibility = View.GONE
        progressBar?.visibility = View.GONE
        errorView?.visibility = View.VISIBLE
        notifyDataSetChanged()
    }

    /**
     * Adds a single Item to data set
     * @param item item to be added to the data set
     */
    open fun addItem(item: T) {
        if (data == null) {
            data = ArrayList()
        }
        data?.add(item)
        notifyDataSetChanged()
    }

    /**
     * Set data set
     * @param data data to be set
     */
    open fun setItems(data: List<T>?) {
        this.data?.clear()
        if (data != null) {
            this.data = ArrayList(data)
        }
        notifyDataSetChanged()
    }

    /**
     * Set data set
     * @param data data to be set
     */
    open fun setItems(data: ArrayList<T>?) {
        this.data?.clear()
        this.data = data
        notifyDataSetChanged()
    }

    /**
     * Appends data to existing data set
     *
     * @param data dato to be append
     */
    open fun addAll(data: ArrayList<T>) {
        if (this.data == null) {
            setItems(data)
        } else {
            this.data?.addAll(data)
        }
        notifyDataSetChanged()
    }

    /**
     * Appends data to existing data set
     *
     * @param data dato to be append
     */
    open fun addAll(data: List<T>) {
        if (this.data == null) {
            setItems(ArrayList(data))
        } else {
            this.data?.addAll(data)
        }
        notifyDataSetChanged()
    }

    /**
     * Clear all data in recyclerview
     */

    open fun clearData() {
        data?.clear()
        data = null
        notifyDataSetChanged()
    }
}
