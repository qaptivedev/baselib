package com.loqqat.base.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseSectionsRecyclerViewAdapter<KEY, DATA, VH : RecyclerView.ViewHolder>(var data: MutableMap<KEY, ArrayList<DATA>>) :
    RecyclerView.Adapter<VH>() {

    var emptyView: View? = null
    var progressBar: View? = null
    var errorView: View? = null


    final override fun getItemCount(): Int {
        var count=0
        for (set in data)
        {
            count+=set.value.size
            count++
        }
        if (count == 0 && progressBar?.visibility ?: View.GONE == View.GONE)
            View.VISIBLE
        else
            View.GONE
        return count
    }

    final override fun onBindViewHolder(holder: VH, position: Int) {
        var count=0
        for(set in data)
        {
            if(count==position) {
                bindHeader(set.key,holder)
                return
            }
            count++
            for(item in set.value)
            {
                if(position==count) {
                    bindItem(item,holder)
                    return
                }
                count++
            }
        }
    }

    final override fun getItemViewType(position: Int): Int {
        var count=0
        for(set in data)
        {
            if(count==position)
                return 1
            count++
            count+=set.value.size
            if(position<count)
                return 0
        }
        return 0
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
     * Set data set
     * @param data data to be set
     */
    open fun setItems(data:  MutableMap<KEY, ArrayList<DATA>>?) {
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

    open fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }

    abstract fun bindHeader(key:KEY?, holder: VH)
    abstract fun bindItem(item:DATA?,holder: VH)
}