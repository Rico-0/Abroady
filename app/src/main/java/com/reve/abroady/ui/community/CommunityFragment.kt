package com.reve.abroady.ui.community

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.reve.abroady.R
import com.reve.abroady.base.BaseFragment
import com.reve.abroady.databinding.FragmentCommunityBinding
import com.reve.abroady.model.data.post.HotBoard
import com.reve.abroady.model.data.post.NowTrending
import com.reve.abroady.model.data.Party
import com.reve.abroady.model.data.post.OnePostPerBoard
import com.reve.abroady.ui.community.adapter.HotBoardAdapter
import com.reve.abroady.ui.community.adapter.NowTrendingAdapter
import com.reve.abroady.ui.community.adapter.OnePostPerBoardAdapter
import com.reve.abroady.ui.community.adapter.PartyAdapter
import com.reve.abroady.util.LoginInstance
import com.reve.abroady.util.PreferenceManager.login_type
import com.reve.abroady.util.recyclerViewItemDecoration.MarginItemDecoration
import com.reve.abroady.viewmodel.MainViewModel
import kotlin.collections.ArrayList

class CommunityFragment : BaseFragment<FragmentCommunityBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_community

    private var partyList = ArrayList<Party>()
    private var nowTrendingList = ArrayList<NowTrending>()
    private var hotBoardList = ArrayList<HotBoard>()
    private var onePostPerBoardList = ArrayList<OnePostPerBoard>()

    var flag : Boolean = true

    // fragment가 activity에 종속되어 있으므로 Fragment가 생성된 Activity의 Lifecycle에 ViewModel 종속시킴
    // 이로 인해 같은 Activity(ViewModelStoreOwner) 를 공유하는 Fragment 간의 데이터 전달이 가능해짐
    private val mainViewModel : MainViewModel by activityViewModels()

    override fun initStartView() {
        Log.d(TAG, "login Type - 로그인 눌렀을 때 : $login_type")
        initClickListener()
        initListData()
        initRecyclerView()
    }

    private fun initClickListener() {
        binding.myPartyButton.setOnClickListener {
            partyButtonListener()
        }
        binding.hotPartyButton.setOnClickListener {
            partyButtonListener()
        }
        binding.seeBoardsLayout.setOnClickListener {
            val intent = Intent(requireContext(), BoardsActivity::class.java)
            startActivity(intent)
        }
        binding.hotBoardMoreLayout.setOnClickListener {
            val intent = Intent(requireContext(), BoardActivity::class.java)
            intent.putExtra("boardName", "Hot Board")
            startActivity(intent)
        }
        binding.communityWritePostButton.setOnClickListener {
            val intent = Intent(requireContext(), WritePostActivity::class.java)
            startActivity(intent)
        }
        binding.logout.setOnClickListener {
            Log.d(TAG, "login Type : $login_type")
            LoginInstance.getLoginInstance(requireContext(), requireActivity(), login_type!!).logout()
        }
    }

    private fun partyButtonListener() {
        if (flag) {
            binding.myPartyButton.setBackgroundResource(R.drawable.tap_background_colored)
            binding.myPartyButton.setTextColor(resources.getColor(R.color.white))
            binding.hotPartyButton.setBackgroundResource(R.drawable.tap_background_default)
            binding.hotPartyButton.setTextColor(resources.getColor(R.color.sliver))
            flag = false
        }
        else {
            binding.hotPartyButton.setBackgroundResource(R.drawable.tap_background_colored)
            binding.hotPartyButton.setTextColor(resources.getColor(R.color.white))
            binding.myPartyButton.setBackgroundResource(R.drawable.tap_background_default)
            binding.myPartyButton.setTextColor(resources.getColor(R.color.sliver))
            flag = true
        }
    }

    private fun initRecyclerView() {
        binding.partyList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = PartyAdapter(partyList)
        }.addItemDecoration(MarginItemDecoration("left", 20))
        binding.nowTrendingList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = NowTrendingAdapter(requireContext(), nowTrendingList)
        }.addItemDecoration(MarginItemDecoration("left", 20))
        binding.hotBoardList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
            adapter = HotBoardAdapter(hotBoardList)
        }
        binding.onePostPerBoardList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
            adapter = OnePostPerBoardAdapter(onePostPerBoardList)
        }.addItemDecoration(MarginItemDecoration("bottom" , 100))
    }

    // 임시 데이터
    private fun initListData() {
        partyList.add(Party("17:30", "Sinchon", "having japanese dishes", "Donburi Mono", "3/4 participating"))
        partyList.add(Party("21:00", "Gangnam", "clubbing all night", "Arena", "8/8 participating"))
        partyList.add(Party("17:30", "Sinchon", "having japanese dishes", "Donburi Mono", "3/4 participating"))
        partyList.add(Party("21:00", "Gangnam", "clubbing all night", "Arena", "8/8 participating"))

        nowTrendingList.add(NowTrending("Random", "Daisy Duck", "10:09", "Dear Professor", "What happened to my grade?...", 19, 7))
        nowTrendingList.add(NowTrending("Random", "Alan Macken", "12:31", "Hi guys!!", "Is there anyone who can...", 23, 11))
        nowTrendingList.add(NowTrending("Random", "Daisy Duck", "10:09", "Dear Professor", "What happened to my grade?...", 19, 7))
        nowTrendingList.add(NowTrending("Random", "Alan Macken", "12:31", "Hi guys!!", "Is there anyone who can...", 23, 11))

        hotBoardList.add(HotBoard("Best Thai restaurant ever!!", "Must go", "6/29", 521, 342))
        hotBoardList.add(HotBoard("Enrollment hack", "Random", "6/26", 374, 153))
        hotBoardList.add(HotBoard("Vietnamese Cafe", "Must go", "6/23", 279, 127))

        onePostPerBoardList.add(OnePostPerBoard("Must go", "Sandwitch restaurant", "Bread and meat basis sandwitch restaurant\nCheck the location below...", "Wheat & Meat" , "616-4 Yeoksam-dong, Gangnam-gu, Seoul", "Thor" , "Just now", 2, 1, 1))
        onePostPerBoardList.add(OnePostPerBoard("Must go", "Sandwitch restaurant", "Bread and meat basis sandwitch restaurant\nCheck the location below...", "Wheat & Meat" , "616-4 Yeoksam-dong, Gangnam-gu, Seoul", "Thor" , "Just now", 2, 1, 1))
    }
}