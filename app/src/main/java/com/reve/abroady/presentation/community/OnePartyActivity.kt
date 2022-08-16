package com.reve.abroady.presentation.community

import android.content.pm.PackageManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.reve.abroady.R
import com.reve.abroady.presentation.base.BaseActivity
import com.reve.abroady.databinding.ActivityOnePartyBinding
import com.reve.abroady.data.entity.party.PartyComment
import com.reve.abroady.presentation.community.adapter.PartyCommentAdapter

class OnePartyActivity : BaseActivity<ActivityOnePartyBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.activity_one_party

    private var commentList = ArrayList<PartyComment>()

    private var flag = true

    override fun initStartView() {
        initAdMob()
        setCustomToolbar(R.id.one_party_toolbar)
        binding.buttonGoBack.setOnClickListener {
            finish()
        }
        binding.commentDefaultButton.setOnClickListener {
            initButtonListener()
        }
        binding.commentRecentButton.setOnClickListener {
            initButtonListener()
        }
        initComments()
        initRecyclerView()
    }

    private fun initAdMob() {
        // 광고 초기화
        MobileAds.initialize(this) { }
        // 광고 띄우기
        val adRequest = AdRequest.Builder().build()

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            binding.adViewBanner.loadAd(adRequest)
        }
    }

    private fun initButtonListener() {
        if (flag) {
            binding.commentDefaultIcon.setImageResource(R.drawable.icon_comment_circle_colored)
            binding.commentDefaultButton.setTextColor(ContextCompat.getColor(this, R.color.dark))
            binding.commentRecentIcon.setImageResource(R.drawable.icon_comment_circle_default)
            binding.commentRecentButton.setTextColor(ContextCompat.getColor(this, R.color.line))
            flag = false
        } else {
            binding.commentDefaultIcon.setImageResource(R.drawable.icon_comment_circle_default)
            binding.commentDefaultButton.setTextColor(ContextCompat.getColor(this, R.color.line))
            binding.commentRecentIcon.setImageResource(R.drawable.icon_comment_circle_colored)
            binding.commentRecentButton.setTextColor(ContextCompat.getColor(this, R.color.dark))
            flag = true
        }
    }

    private fun initRecyclerView() {
        binding.partyCommentList.apply {
            layoutManager = LinearLayoutManager(this@OnePartyActivity, LinearLayoutManager.VERTICAL, false)
            adapter = PartyCommentAdapter(this@OnePartyActivity, commentList)
        }
    }

    private fun initComments() {
        commentList.add(PartyComment("", "Mickey Mouse", "16:38", "What serves there?", false))
        commentList.add(PartyComment("", "Donald Duck", "16:42", "Japanese rice bowl!", true))
        commentList.add(PartyComment("", "Mickey Mouse", "16:44", "Thanks :)", true))
        commentList.add(PartyComment("", "Mickey Mouse", "16:38", "What serves there?", false))
        commentList.add(PartyComment("", "Donald Duck", "16:42", "Japanese rice bowl!", true))
        commentList.add(PartyComment("", "Mickey Mouse", "16:38", "What serves there?", false))
        commentList.add(PartyComment("", "Donald Duck", "16:42", "Japanese rice bowl!", true))
    }

    private fun setCustomToolbar(layout : Int) {
        val toolbar = findViewById<Toolbar>(layout)
        // 커스텀 툴바를 액션바로 설정
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayShowCustomEnabled(true)
        // 액션바에서 앱 이름 보이지 않게 제거
        actionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.one_party_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.party_send_message -> {
                Toast.makeText(this, "Send Message", Toast.LENGTH_SHORT).show()
            }
            R.id.party_report -> {
                Toast.makeText(this, "Report", Toast.LENGTH_SHORT).show()
            }
            R.id.party_block_user -> {
                Toast.makeText(this, "Block this user", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}