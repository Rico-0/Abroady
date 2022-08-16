package com.reve.abroady.presentation.community

import android.content.Intent
import com.reve.abroady.R
import com.reve.abroady.presentation.base.BaseActivity
import com.reve.abroady.databinding.ActivitySearchBoardBinding

class SearchBoardActivity : BaseActivity<ActivitySearchBoardBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.activity_search_board

    override fun initStartView() {
        binding.makeNewBoardButton.setOnClickListener {
            val intent = Intent(this, MakeNewBoardActivity::class.java)
            startActivity(intent)
        }
    }
}