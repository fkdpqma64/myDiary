package com.example.mydairy.ui.diarylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mydairy.R
import com.example.mydairy.databinding.DiaryListItemBinding
import common.data.local.DiaryItem

/**
 * 뉴스 리스트 어뎁터
 */
class DiaryListAdapter :
    RecyclerView.Adapter<DiaryListAdapter.ItemViewHolder>() {
    class ItemViewHolder(val mbind: DiaryListItemBinding) : RecyclerView.ViewHolder(mbind.root)

    lateinit var items: List<DiaryItem>
    lateinit var clickListener: (item: DiaryItem) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.diary_list_item, parent, false)
        val viewHolder = ItemViewHolder(DiaryListItemBinding.bind(view))
        view.setOnClickListener {
            clickListener.invoke(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        items[position].let {
            with(holder.mbind) {
                txtNewsTitle.text = it.title
            }
        }
    }

}
