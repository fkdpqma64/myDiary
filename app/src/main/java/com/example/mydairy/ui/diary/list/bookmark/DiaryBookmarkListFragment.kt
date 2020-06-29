package com.example.mydairy.ui.diary.list.bookmark

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mydairy.MainActivity
import com.example.mydairy.R
import com.example.mydairy.databinding.FragmentDiaryListBinding
import com.example.mydairy.ui.diary.view.DiaryViewActivity
import common.data.local.DiaryItem
import common.di.injector
import kotlinx.coroutines.launch

class DiaryBookmarkListFragment : Fragment() {

    companion object {
        fun newInstance() = DiaryBookmarkListFragment()
        const val EXTRA_DIARY_ITEM_ID = "EXTRA_DIARY_ITEM_ID"
    }

    private lateinit var mBind: FragmentDiaryListBinding
    private lateinit var mAdapterBookmark: DiaryBookmarkListAdapter
    private val mViewModel by lazy { requireActivity().injector.diaryBookmarkListViewModel }

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
        mBind.diaryList.post {
            mAdapterBookmark.notifyDataSetChanged()
        }
    }

    private fun customInit() {
        //mViewModel.refreshViewData()
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
                mAdapterBookmark.items = it as List<DiaryItem>
                mBind.progressBar.visibility = View.GONE
                mAdapterBookmark.notifyDataSetChanged()
                mBind.diaryList.adapter = mAdapterBookmark
                mBind.diaryList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            }
        })

        /**
         * 새로고침 리스너
         */
        mBind.swipeLayout.setOnRefreshListener {
            mViewModel.refreshViewData()
            mBind.diaryList.post {
                mAdapterBookmark.notifyDataSetChanged()
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
                val layoutManager =
                    StaggeredGridLayoutManager::class.java.cast(recyclerView.layoutManager)!!
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastCompletelyVisibleItemPositions(null)

                if (lastVisible[0] >= totalItemCount - 1 || lastVisible[1] >= totalItemCount - 1) {
                    Log.d("XXX", "lastVisibled $totalItemCount")
                    mViewModel.scrollViewData()
                    recyclerView.post {
                        mAdapterBookmark.notifyDataSetChanged()
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
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
        mAdapterBookmark = DiaryBookmarkListAdapter()

        mAdapterBookmark.clickListener = { item ->
            DiaryViewActivity.createIntent(requireContext()).also {
                it.putExtra(EXTRA_DIARY_ITEM_ID, item.id)
                startActivity(it)
            }
        }
    }

    private fun thisActivity(): MainActivity? {
        return activity as? MainActivity
    }

}
