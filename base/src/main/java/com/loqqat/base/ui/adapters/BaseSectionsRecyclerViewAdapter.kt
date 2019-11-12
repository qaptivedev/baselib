package com.loqqat.base.ui.adapters

import androidx.recyclerview.widget.RecyclerView

abstract class BaseSectionsRecyclerViewAdapter<KEY, DATA, VH : RecyclerView.ViewHolder>(var data: MutableMap<KEY, ArrayList<DATA>>) :
    RecyclerView.Adapter<VH>() {


    final override fun getItemCount(): Int {
        var count=0
        for (set in data)
        {
            count+=set.value.size
            count++
        }
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

    abstract fun bindHeader(key:KEY?, holder: VH)
    abstract fun bindItem(item:DATA?,holder: VH)
}