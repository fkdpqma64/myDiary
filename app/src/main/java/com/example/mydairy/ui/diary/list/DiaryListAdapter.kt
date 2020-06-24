package com.example.mydairy.ui.diary.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mydairy.R
import com.example.mydairy.databinding.DiaryListItemBinding
import common.data.local.DiaryItem
import java.time.format.DateTimeFormatter

class DiaryListAdapter :
    RecyclerView.Adapter<DiaryListAdapter.ItemViewHolder>() {
    class ItemViewHolder(val mbind: DiaryListItemBinding) : RecyclerView.ViewHolder(mbind.root)

    lateinit var items: List<DiaryItem>
    lateinit var clickListener: (item: DiaryItem) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.diary_list_item, parent, false)
        val viewHolder =
            ItemViewHolder(
                DiaryListItemBinding.bind(view)
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
                txtContent.text = it.contents
                txtDate.text = it.dateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            }
        }
    }

}
