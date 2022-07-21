package com.reve.abroady.ui.community

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.reve.abroady.R
import com.reve.abroady.base.BaseActivity
import com.reve.abroady.databinding.ActivityMakeNewBoardBinding
import com.reve.abroady.model.data.post.OnePostSendModel
import com.reve.abroady.viewmodel.MainViewModel

class MakeNewBoardActivity : BaseActivity<ActivityMakeNewBoardBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.activity_make_new_board

    override fun initStartView() {
        setCustomToolbar(R.id.make_new_board_toolbar)
        setTextWatcher()
    }

    private fun setTextWatcher() {
        binding.boardNameInputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val boardNameLength = binding.boardNameInputText.text.toString().length
                binding.boardNameTextCount.text = boardNameLength.toString() + " / 20"
            }

            override fun afterTextChanged(s: Editable?) { }
        })
        binding.boardDescriptionInputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val boardDescriptionLength = binding.boardDescriptionInputText.text.toString().length
                binding.boardDescriptionTextCount.text = boardDescriptionLength.toString() + " / 100"
            }

            override fun afterTextChanged(s: Editable?) { }
        })
    }

    private fun setCustomToolbar(layout : Int) {
        val toolbar = findViewById<Toolbar>(layout)
        // 커스텀 툴바를 액션바로 설정
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayShowCustomEnabled(true)
        // 액션바에서 앱 이름 보이지 않게 제거
        actionBar?.setDisplayShowTitleEnabled(false)
        // 자동으로 뒤로가기 버튼
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // 뒤로가기 버튼
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}