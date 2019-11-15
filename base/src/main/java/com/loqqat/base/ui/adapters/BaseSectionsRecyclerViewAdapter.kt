package com.loqqat.base.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Base Class for RecyclerView with sections, all the general handling for adapter are done here, Like adding , appending ,removing Items etc.
 * A 2 dimension array is created based on Key amd Data , each row is treated as a section
 * @param KEY Data type of key or header data
 * @param DATA Data type or model of adapter holding , an ArrayList<T> will be crated
 * @param VH ViewHolder of the adapter
 * @property data initial data set for the adapter
 *@constructor Creates an RecyclerView.Adapter with provided data
 */
abstract class BaseSectionsRecyclerViewAdapter<KEY, DATA, VH : RecyclerView.ViewHolder>(var data: MutableMap<KEY?, List<DATA>> = mutableMapOf()) :
    RecyclerView.Adapter<VH>() {

    var emptyView: View? = null
    var progressBar: View? = null
    var errorView: View? = null

    /**
     * Type of view
     */
    object ViewType {
        val HEADER = 1
        val ITEM = 0
    }


    /**
     * calculates total number of items in recycler view including headers
     */
    final override fun getItemCount(): Int {
        var count = 0
        for (set in data) {
            count += set.value.size
            count++
        }
        emptyView?.visibility = if (count == 0 && progressBar?.visibility ?: View.GONE == View.GONE)
            View.VISIBLE
        else
            View.GONE
        return count
    }

    final override fun onBindViewHolder(holder: VH, position: Int) {
        var count=0
        for ( key in data.keys)
        {
            count+=(data[key]?.size?:0)+1
            if(count<=position)
                continue
            val p=((data[key]?.size?:0)+1)-(count-position)
            if(p==0)
            {
                bindHeader(key, holder)
                return
            }
            else
            {
                bindItem(data[key]?.get(p-1),holder)
                return
            }
        }
    }

    final override fun getItemViewType(position: Int): Int {
        var count=0
        for ( key in data.keys)
        {
            count+=(data[key]?.size?:0)+1
            if(count<=position)
                continue
            val p=((data[key]?.size?:0)+1)-(count-position)
            return if(p==0) {
                ViewType.HEADER
            } else {
                ViewType.ITEM
            }
        }
        return ViewType.ITEM
    }

    /**
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
        emptyView?.visibility = if (data.isEmpty()) View.VISIBLE else View.GONE
    }

    /**
    For showing error view in timeline and contact fragment only
  */
    open fun showError() {
        emptyView?.visibility = View.GONE
        progressBar?.visibility = View.GONE
        errorView?.visibility = View.VISIBLE
        notifyDataSetChanged()
    }

    /**
     * Set data set
     * @param data data to be set
     */
    fun setItems(data: Map<KEY?, List<DATA>>?) {
        this.data.clear()
        if (data != null) {
            this.data.putAll(data)
        }
        notifyDataSetChanged()
    }

    /**
     * Clear all data in recyclerview
     *
     * @param data cleared
     */

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }

    fun addRow(key: KEY,data:List<DATA>){
        this.data[key] = data
    }

    /**
     * Removes the specified key and its corresponding value from this map.
     *
     * @return the previous value associated with the key, or `null` if the key was not present in the map.
     */
    fun removeRow(key: KEY):List<DATA>?
    {
        return this.data.remove(key)
    }

    fun getHeaderCount():Int
    {
        return data.size
    }

    fun getChildCount():Int
    {
        return data.values.size
    }

    /**
     * abstract method to bind data of header holder and data is provided based on position
     * header view bindings can be done here
     * @param key data in header
     * @param holder viewHolder of header
     */
    abstract fun bindHeader(key: KEY?, holder: VH)

    /**
     * abstract method to bind data of child holder and data is provided based on position
     * child view bindings can be done here
     * @param item data to bind
     * @param holder ViewHolder of child
     */
    abstract fun bindItem(item: DATA?, holder: VH)
}