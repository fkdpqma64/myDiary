package com.example.mydairy.ui.diary.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydairy.MainActivity
import com.example.mydairy.R
import com.example.mydairy.databinding.FragmentDiaryListBinding
import com.example.mydairy.ui.diary.view.DiaryViewActivity
import common.data.local.DiaryItem
import common.di.injector
import kotlinx.coroutines.launch


class DiaryListFragment : Fragment() {

    companion object {
        fun newInstance() = DiaryListFragment()
        const val EXTRA_DIARY_ITEM_ID = "EXTRA_DIARY_ITEM_ID"
    }

    private lateinit var mBind: FragmentDiaryListBinding
    private lateinit var mAdapter: DiaryListAdapter
    private val mViewModel by lazy { requireActivity().injector.diaryListViewModel }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = DataBindingUtil.inflate(inflater, R.layout.fragment_diary_list, container, false)
        mBind.lifecycleOwner = this
        return mBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customInit()
        setupEvents()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.refreshViewData()
    }

    private fun customInit() {
      // mViewModel.refreshViewData()
        val animation = AnimationUtils.loadLayoutAnimation(thisActivity(), R.anim.layout_animation_fall_down)
        mBind.diaryList.layoutAnimation = animation
    }

    private fun setupEvents() {

        /**
         * 뷰모델에서 가져온 데이터를 Observe하여 RecyclerView에 등록
         */
        mViewModel.viewData.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                mBind.progressBar.visibility = View.VISIBLE
            }
            lifecycleScope.launch {
                mAdapter.items = it as List<DiaryItem>
                mBind.progressBar.visibility = View.GONE
               // mAdapter.notifyDataSetChanged()
                mBind.diaryList.adapter = mAdapter
                runLayoutAnimation(mBind.diaryList)
            }
        })

        /**
         * 새로고침 리스너
         */
        mBind.swipeLayout.setOnRefreshListener {
            mViewModel.refreshViewData()
            mBind.diaryList.post {
                mAdapter.notifyDataSetChanged()
            }
        }

        /**
         * 데이터 로딩 상태 Observe하여 스와이프 레이어 관리
         */
        mViewModel.dataLoading.observe(viewLifecycleOwner, Observer { yes ->
            if (!yes) {
                mBind.swipeLayout.isRefreshing = false
            }
        })

        mBind.diaryList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = LinearLayoutManager::class.java.cast(recyclerView.layoutManager)!!
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()

                if (lastVisible >= totalItemCount - 1) {
                    Log.d("XXX", "lastVisibled $totalItemCount")
                    mViewModel.scrollViewData()
                    recyclerView.post {
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    /**
     * RecyclerView 셋업
     */
    private fun setupRecyclerView() {
        mBind.diaryList.apply {
            layoutManager = LinearLayoutManager(thisActivity())
        }
        mAdapter = DiaryListAdapter()

        mAdapter.clickListener = { item ->
            DiaryViewActivity.createIntent(requireContext()).also {
                it.putExtra(EXTRA_DIARY_ITEM_ID, item.id)
                startActivity(it)
            }
        }
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context: Context = recyclerView.context
        val controller =
            AnimationUtils.loadLayoutAnimation(
                context,
                R.anim.layout_animation_fall_down
            )
        recyclerView.layoutAnimation = controller
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    private fun thisActivity(): MainActivity? {
        return activity as? MainActivity
    }

}
