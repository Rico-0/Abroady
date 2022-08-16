package com.reve.abroady.presentation.community

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.reve.abroady.R
import com.reve.abroady.presentation.base.BaseActivity
import com.reve.abroady.databinding.ActivityBoardsBinding
import com.reve.abroady.data.entity.board.BoardInfo
import com.reve.abroady.presentation.community.adapter.BoardsAdapter
import com.reve.abroady.util.recyclerViewItemDecoration.MarginItemDecoration

class BoardsActivity : BaseActivity<ActivityBoardsBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.activity_boards

    private var myBoardList = ArrayList<BoardInfo>()
    private var recommendedList = ArrayList<BoardInfo>()


    override fun initStartView() {
        setCustomToolbar(R.id.boards_toolbar)
        initClickListener()
        initListData()
        initRecycierView()
    }

    private fun initClickListener() {
        binding.boardsSearchButton.setOnClickListener {
            val intent = Intent(this, SearchBoardActivity::class.java)
            startActivity(intent)
        }
        binding.makeNewBoardButton.setOnClickListener {
            val intent = Intent(this, MakeNewBoardActivity::class.java)
            startActivity(intent)
        }
    }

    // 임시 데이터 추가
    private fun initListData() {
        myBoardList.add(BoardInfo("Must go", true, "Introduce hot places you must visit in Korea"))
        myBoardList.add(BoardInfo("Bazaar", true, "Buying and selling used items"))
        myBoardList.add(BoardInfo("Fashion", false, "Share your daily fashion with others"))
        myBoardList.add(BoardInfo("Fitness", false, "Board for every gym workers"))
        myBoardList.add(BoardInfo("Pet", true, "Get information about raising pets"))

        recommendedList.add(BoardInfo("Korean conversation", true, "Practice speaking in korean each other"))
        recommendedList.add(BoardInfo("Game mates", true, "Find someone to play game together"))
        recommendedList.add(BoardInfo("Random", true, "Talk about any topics"))
    }

    private fun initRecycierView() {
        binding.myBoardsList.apply {
            layoutManager = LinearLayoutManager(this@BoardsActivity, LinearLayoutManager.VERTICAL, false)
            adapter = BoardsAdapter(myBoardList)
        }.addItemDecoration(MarginItemDecoration("top", 20))
        binding.recommendedList.apply {
            layoutManager = LinearLayoutManager(this@BoardsActivity, LinearLayoutManager.VERTICAL, false)
            adapter = BoardsAdapter(recommendedList)
        }.addItemDecoration(MarginItemDecoration("top", 20))
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.boards_menu, menu)
        /* 커스텀 메뉴에 대해 클릭 이벤트 설정
        val editMenu = menu?.findItem(R.id.boards_edit_menu)
        editMenu?.actionView?.setOnClickListener {
            onOptionsItemSelected(editMenu)
        } */
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // 뒤로가기 버튼
                finish()
            }
            R.id.boards_edit_menu -> {
                val intent = Intent(this@BoardsActivity, BoardsEditActivity::class.java)
                // 추후에 my boards, recommended 게시판 정보 Parcelable?? 로 intent 넘길 것
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}