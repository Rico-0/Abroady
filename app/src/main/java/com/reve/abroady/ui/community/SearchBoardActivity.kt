package com.reve.abroady.ui.community

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.reve.abroady.R
import com.reve.abroady.base.BaseActivity
import com.reve.abroady.databinding.ActivitySearchBoardBinding
import com.reve.abroady.viewmodel.MainViewModel

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