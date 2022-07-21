package com.reve.abroady.ui.community

import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.reve.abroady.R
import com.reve.abroady.base.BaseActivity
import com.reve.abroady.databinding.ActivityBoardBinding
import com.reve.abroady.model.data.RetrofitInstance
import com.reve.abroady.model.data.post.OnePostGetModel
import com.reve.abroady.ui.community.adapter.BoardPostAdapter
import com.reve.abroady.util.addTo
import com.reve.abroady.util.recyclerViewItemDecoration.MarginItemDecoration
import com.reve.abroady.viewmodel.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class BoardActivity : BaseActivity<ActivityBoardBinding>() {

    // 게시판 이름으로 게시판 설명, 게시글 목록도 불러올 수 있게 DB에서 짠다는 가정을 일단 하고..
    private lateinit var boardName: String

    private val boardDao = RetrofitInstance.getBoardDaoInstance()

    override val layoutResourceId: Int
        get() = R.layout.activity_board

    private val boardPostAdapter by lazy {
        BoardPostAdapter(this)
    }

    override fun initStartView() {
        observeData()
        setBoardTitle()
        setCustomToolbar(R.id.board_toolbar)
        setInitData()
        initRecyclerView()
    }

    private fun observeData() {
        boardDao.getAllPost()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.d(TAG, "getAllPost Error : ${it}")
            }
            .unsubscribeOn(Schedulers.io())
            .subscribe({ response ->
                if (response.isSuccessful) {
                    response.body()?.let { postGetModel -> boardPostAdapter.update(postGetModel.postList) }
                }
            }, {
                if (it is HttpException) {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Failed to get posts. Please try later.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Failed to get posts. Check your Internet connection.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Log.e(TAG, "getAllPost error : $it")
                }
            }).addTo(compositeDisposable)
    }

    private fun setCustomToolbar(layout: Int) {
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

    private fun initRecyclerView() {
        binding.boardPostList.apply {
            layoutManager =
                LinearLayoutManager(this@BoardActivity, LinearLayoutManager.VERTICAL, false)
            adapter = boardPostAdapter
        }.addItemDecoration(MarginItemDecoration("top", 20))
    }

    private fun setInitData() {
        /* postList.add(BoardPost("Must go", "Plato", "6/29", "Best thai restaurant ever!!",
         "I've never been to such a cool thai restaurant like this!\nEven better than the one I had in Thailand.",
         3, 521,342))
         postList.add(BoardPost("Random", "Aristotle", "6/26", "Enrollment Hack",
             "I'll share my enrollment hacks.\nTry it on next semester.\nFirst, check the lecture reviews on everytime...",
             7, 374,153))
         postList.add(BoardPost("Must go", "Roger Bacon", "6/23", "Vietnamese Cafe",
             "This cafe serves authentic vietnamese coffee.\nMy favourite is 'Caphe Suada'.\nTrust me and try it...",
             2, 219,127)) */
    }

    private fun setBoardTitle() {
        boardName = intent.getStringExtra("boardName") ?: "None"
        if (!(boardName == "None")) {
            binding.boardTitle.text = boardName
        }
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