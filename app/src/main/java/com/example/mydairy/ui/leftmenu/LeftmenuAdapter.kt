package com.example.mydairy.ui.leftmenu

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mydairy.R
import com.example.mydairy.databinding.ListItemLeftMenuBinding
import common.data.local.MenuTextValueItem

class LeftmenuAdapter :
    RecyclerView.Adapter<LeftmenuAdapter.ItemViewHolder>() {
    class ItemViewHolder(val mbind: ListItemLeftMenuBinding) : RecyclerView.ViewHolder(mbind.root)

    lateinit var items: List<MenuTextValueItem>
    lateinit var clickListener: (item: MenuTextValueItem) -> Unit
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_left_menu, parent, false)
        val viewHolder =
            ItemViewHolder(
                ListItemLeftMenuBinding.bind(view)
            )
        view.setOnClickListener {
            clickListener.invoke(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        items[position].let {
            with(holder.mbind) {
                txtLabel.text = it.label
            }
        }
    }
}
