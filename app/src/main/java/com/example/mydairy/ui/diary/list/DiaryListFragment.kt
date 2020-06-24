package com.example.mydairy.ui.diary.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydairy.MainActivity
import com.example.mydairy.R
import com.example.mydairy.databinding.FragmentDiaryListBinding
import common.data.local.DiaryItem
import common.di.injector
import kotlinx.coroutines.launch

class DiaryListFragment : Fragment() {

    companion object {
        fun newInstance() = DiaryListFragment()
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

    private fun customInit() {
        mViewModel.refreshViewData()
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
                mAdapter.notifyDataSetChanged()
                mBind.diaryList.adapter = mAdapter
            }
        })

        /**
         * 새로고침 리스너
         */
        mBind.swipeLayout.setOnRefreshListener {
            mViewModel.refreshViewData()
        }

        /**
         * 데이터 로딩 상태 Observe하여 스와이프 레이어 관리
         */
        mViewModel.dataLoading.observe(viewLifecycleOwner, Observer { yes ->
            if (!yes) {
                mBind.swipeLayout.isRefreshing = false
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

        /**
         * 클릭 리스너 - 클릭시 해당 뉴스가 담긴 웹뷰 액티비티로 전환
         */
        mAdapter.clickListener = { item ->
            Toast.makeText(thisActivity(), "${item.id}", Toast.LENGTH_SHORT).show()
//            val intent = Intent(thisActivity(), WebViewActivity::class.java).also {
//                it.putExtra(EXTRA_NEWS_TITLE, item.title)
//                startActivity(intent)
//            }

        }
    }
    private fun thisActivity(): MainActivity? {
        return activity as? MainActivity
    }
}
