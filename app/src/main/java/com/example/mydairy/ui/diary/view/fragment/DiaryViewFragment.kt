package com.example.mydairy.ui.diary.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.mydairy.R
import com.example.mydairy.databinding.FragmentDiaryViewBinding
import com.example.mydairy.ui.diary.view.DiaryViewActivity
import common.di.injector
import common.util.Util

class DiaryViewFragment : Fragment() {

    companion object {
        const val FRAGMENT_MODE_VIEW_DIARY = "FRAGMENT_MODE_VIEW_DIARY"
        const val FRAGMENT_MODE_UPDATE_DIARY = "FRAGMENT_MODE_UPDATE_DIARY"
        const val EXTRA_DIARY_ITEM_ID = "EXTRA_DIARY_ITEM_ID"
        fun newInstance() = DiaryViewFragment()
    }

    private var mFragmentMode = FRAGMENT_MODE_VIEW_DIARY
    private lateinit var mBind: FragmentDiaryViewBinding
    private val mViewModel by lazy { requireActivity().injector.diaryViewFragmentViewModel }
    var mDiaryId = 1
    lateinit var mMenu: Menu

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = DataBindingUtil.inflate(inflater, R.layout.fragment_diary_view, container, false)
        mBind.lifecycleOwner = this
        setHasOptionsMenu(true)
        return mBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDiaryId = arguments?.getInt(EXTRA_DIARY_ITEM_ID) ?: 1
        customInit()
        setupEvents()
    }

    private fun customInit() {
        mViewModel.selectByIdData(mDiaryId)
    }

    @SuppressLint("SetTextI18n")
    private fun setupEvents() {
        mViewModel.viewData.observe(viewLifecycleOwner, Observer {
            mBind.txtDiaryTitle.text.append(it.title)
            mBind.txtDiaryContent.text.append(it.contents)
            mBind.txtDate.text = "- ${Util.longToTime(it.createTime)} -"
            mBind.txtUpdate.text = "수정됨: ${Util.longToTime(it.updateTime)}"
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        mMenu = menu
        mViewModel.viewData.observe(this, Observer { diary ->
            when (diary.bookMark) {
                true -> {
                    menu.findItem(R.id.bookmark_diary).setIcon(R.drawable.ic_baseline_bookmark_24_on)
                }
                false -> {
                    menu.findItem(R.id.bookmark_diary).setIcon(R.drawable.ic_baseline_bookmark_24_off)
                }
            }
        })
        super.onPrepareOptionsMenu(menu)
    }


    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.update_diary -> {
                mFragmentMode = FRAGMENT_MODE_UPDATE_DIARY
                changeFragmentMode()
            }

            R.id.bookmark_diary -> {
                val diary = mViewModel.viewData.value
                if (diary != null) {
                    diary.bookMark = !diary.bookMark
                    when (diary.bookMark) {
                        true -> {
                            item.setIcon(R.drawable.ic_baseline_bookmark_24_on)
                        }
                        false -> {
                            item.setIcon(R.drawable.ic_baseline_bookmark_24_off)
                        }
                    }
                    mViewModel.updateData(diary)
                }
            }

            R.id.delete_diary -> {
                lateinit var dialog: AlertDialog
                val builder = AlertDialog.Builder(this.requireContext())
                dialog = builder.apply {
                    setMessage("정말로 삭제합니까?")
                    setPositiveButton("예") { _, _ ->
                        mViewModel.deleteData(mDiaryId)
                        dialog.dismiss()
                        thisActivity()?.finish()
                    }
                    setNegativeButton("아니오") { _, _ ->
                        dialog.dismiss()
                    }
                }.create()
                dialog.show()
            }

            R.id.confirm_diary -> {
                mFragmentMode = FRAGMENT_MODE_VIEW_DIARY
                changeFragmentMode()
                mViewModel.updateData(
                    mViewModel.updateDiary(
                        mViewModel.viewData.value!!,
                        mBind.txtDiaryTitle.text.toString(),
                        mBind.txtDiaryContent.text.toString()
                    )
                )
                Toast.makeText(thisActivity(), "수정 완료!", Toast.LENGTH_SHORT).show()
            }

            android.R.id.home -> {
                thisActivity()?.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeFragmentMode() {
        when (mFragmentMode) {
            FRAGMENT_MODE_VIEW_DIARY -> {
                mMenu.findItem(R.id.delete_diary).isVisible = true
                mMenu.findItem(R.id.update_diary).isVisible = true
                mMenu.findItem(R.id.bookmark_diary).isVisible = true
                mMenu.findItem(R.id.confirm_diary).isVisible = false

                mBind.txtDiaryTitle.isEnabled = false
                mBind.txtDiaryContent.isEnabled = false
            }

            FRAGMENT_MODE_UPDATE_DIARY -> {
                mMenu.findItem(R.id.delete_diary).isVisible = false
                mMenu.findItem(R.id.update_diary).isVisible = false
                mMenu.findItem(R.id.bookmark_diary).isVisible = false
                mMenu.findItem(R.id.confirm_diary).isVisible = true

                mBind.txtDiaryTitle.isEnabled = true
                mBind.txtDiaryContent.isEnabled = true

                mBind.txtDiaryTitle.requestFocus()

                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(
                    InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )

            }
        }
    }

    private fun thisActivity(): DiaryViewActivity? {
        return activity as? DiaryViewActivity
    }
}