package com.example.mydairy.ui.leftmenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydairy.MainActivity
import com.example.mydairy.R
import com.example.mydairy.databinding.FragmentLeftMenuBinding
import com.example.mydairy.ui.diary.list.DiaryListFragment
import com.example.mydairy.ui.diary.list.bookmark.DiaryBookmarkListFragment
import common.data.local.MenuTextValueItem
import common.di.injector
import kotlinx.coroutines.launch

class LeftMenuFragment : Fragment() {

    companion object {
        fun newInstance() = LeftMenuFragment()
    }

    private lateinit var mBind: FragmentLeftMenuBinding
    private lateinit var mAdapter: LeftMenuAdapter
    private val mViewModel by lazy { requireActivity().injector.leftMenuViewModel }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = DataBindingUtil.inflate(inflater, R.layout.fragment_left_menu, container, false)
        mBind.lifecycleOwner = this
        return mBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customInit()
        setupEvents()
        setupRecyclerView()
    }


    private fun customInit() {

    }

    private fun setupEvents() {

        /**
         * 뷰모델에서 가져온 데이터를 Observe하여 RecyclerView에 등록
         */
        mViewModel.viewData.observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                mAdapter.items = it as List<MenuTextValueItem>
                mAdapter.notifyDataSetChanged()
                mBind.recyclerView.adapter = mAdapter
            }
        })

    }

    /**
     * RecyclerView 셋업
     */
    private fun setupRecyclerView() {
        mBind.recyclerView.apply {
            layoutManager = LinearLayoutManager(thisActivity())
        }
        mAdapter = LeftMenuAdapter()

        mAdapter.clickListener = { item ->
            if (thisActivity() != null) {
                when (item.itemId) {
                    1 -> {
                        thisActivity()!!.supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_diary_list, DiaryListFragment.newInstance())
                            .commit()
                        thisActivity()!!.onBackPressed()
                    }
                    2 -> {
                        thisActivity()!!.supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.fragment_diary_list,
                                DiaryBookmarkListFragment.newInstance()
                            ).commit()
                        thisActivity()!!.onBackPressed()
                    }
                }
            }
        }
    }

    private fun thisActivity(): MainActivity? {
        return activity as? MainActivity
    }

}
