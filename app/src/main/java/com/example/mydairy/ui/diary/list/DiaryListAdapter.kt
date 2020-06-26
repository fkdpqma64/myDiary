package com.example.mydairy.ui.diary.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mydairy.R
import com.example.mydairy.databinding.ListItemDiaryBinding
import common.BuildVar
import common.data.local.DiaryItem

class DiaryListAdapter :
    RecyclerView.Adapter<DiaryListAdapter.ItemViewHolder>() {
    class ItemViewHolder(val mbind: ListItemDiaryBinding) : RecyclerView.ViewHolder(mbind.root)

    lateinit var items: List<DiaryItem>
    lateinit var clickListener: (item: DiaryItem) -> Unit
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_diary, parent, false)
        val viewHolder =
            ItemViewHolder(
                ListItemDiaryBinding.bind(view)
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
                txtDiaryTitle.text = it.title
                txtDiaryContent.text = it.contents
                txtDate.text = BuildVar.longToTime(it.createTime)
                if (it.bookMark)
                    imgBookmark.visibility = View.VISIBLE
                else
                    imgBookmark.visibility = View.INVISIBLE
            }
        }


    }

}
