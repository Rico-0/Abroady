package com.reve.abroady.ui.community

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.reve.abroady.R
import com.reve.abroady.base.BaseActivity
import com.reve.abroady.databinding.ActivityBoardsEditBinding
import com.reve.abroady.model.data.board.BoardEdit
import com.reve.abroady.ui.community.adapter.BoardsEditAdapter
import com.reve.abroady.util.recyclerViewItemDecoration.MarginItemDecoration
import com.reve.abroady.viewmodel.MainViewModel

class BoardsEditActivity : BaseActivity<ActivityBoardsEditBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.activity_boards_edit

    private var myBoardList = ArrayList<BoardEdit>()
    private var recommendedList = ArrayList<BoardEdit>()


    override fun initStartView() {
        setInitData()
        initRecyclerView()
        binding.editDoneButton.setOnClickListener {
            finish()
        }
    }


    private fun setInitData() {
        myBoardList.add(BoardEdit("Must go", true, "Introduce hot places you must visit in Korea"))
        myBoardList.add(BoardEdit("Bazaar", true, "Buying and selling used items"))
        myBoardList.add(BoardEdit("Fashion", false, "Share your daily fashion with others"))
        myBoardList.add(BoardEdit("Fitness", false, "Board for every gym workers"))
        myBoardList.add(BoardEdit("Pet", true, "Get information about raising pets"))

        recommendedList.add(BoardEdit("Korean conversation", true, "Practice speaking in korean each other"))
        recommendedList.add(BoardEdit("Game mates", true, "Find someone to play game together"))
        recommendedList.add(BoardEdit("Random", true, "Talk about any topics"))

    }

    private fun initRecyclerView() {
        binding.myBoardsList.apply {
            layoutManager = LinearLayoutManager(this@BoardsEditActivity, LinearLayoutManager.VERTICAL, false)
            adapter = BoardsEditAdapter(myBoardList)
        }.addItemDecoration(MarginItemDecoration("top", 20))
        binding.recommendedList.apply {
            layoutManager = LinearLayoutManager(this@BoardsEditActivity, LinearLayoutManager.VERTICAL, false)
            adapter = BoardsEditAdapter(recommendedList)
        }.addItemDecoration(MarginItemDecoration("top", 20))
    }
}