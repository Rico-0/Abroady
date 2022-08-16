package com.reve.abroady.presentation.community

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.reve.abroady.R
import com.reve.abroady.data.AuthState
import com.reve.abroady.presentation.base.BaseFragment
import com.reve.abroady.databinding.FragmentCommunityBinding
import com.reve.abroady.data.entity.post.HotBoard
import com.reve.abroady.data.entity.post.NowTrending
import com.reve.abroady.data.entity.party.Party
import com.reve.abroady.data.entity.post.OnePostPerBoard
import com.reve.abroady.presentation.MainActivity
import com.reve.abroady.presentation.community.adapter.HotBoardAdapter
import com.reve.abroady.presentation.community.adapter.NowTrendingAdapter
import com.reve.abroady.presentation.community.adapter.OnePostPerBoardAdapter
import com.reve.abroady.presentation.community.adapter.PartyAdapter
import com.reve.abroady.presentation.login.TmpLoginSignUpActivity
import com.reve.abroady.presentation.login.loginviewmodel.FireBaseLoginViewModel
import com.reve.abroady.util.LoginInstance
import com.reve.abroady.util.PreferenceManager.login_type
import com.reve.abroady.util.recyclerViewItemDecoration.MarginItemDecoration
import com.reve.abroady.presentation.viewmodel.MainViewModel
import org.koin.android.ext.android.inject
import kotlin.collections.ArrayList

class CommunityFragment : BaseFragment<FragmentCommunityBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_community

    private var partyList = ArrayList<Party>()
    private var nowTrendingList = ArrayList<NowTrending>()
    private var hotBoardList = ArrayList<HotBoard>()
    private var onePostPerBoardList = ArrayList<OnePostPerBoard>()

    companion object {
        private val auth = FirebaseAuth.getInstance()
    }

    private val fireBaseLoginViewModel : FireBaseLoginViewModel by inject()

    var flag : Boolean = true

    // fragment가 activity에 종속되어 있으므로 Fragment가 생성된 Activity의 Lifecycle에 ViewModel 종속시킴
    // 이로 인해 같은 Activity(ViewModelStoreOwner) 를 공유하는 Fragment 간의 데이터 전달이 가능해짐
   // private val mainViewModel : MainViewModel by activityViewModels()

    override fun initStartView() {
        initClickListener()
        initListData()
        initRecyclerView()
        setUserData()
        observeAuthState()
    }

    private fun observeAuthState() {
        fireBaseLoginViewModel.authState.observe(this, { authState ->
            if (authState == AuthState.Idle) {
                val intent = Intent(requireActivity(), TmpLoginSignUpActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                requireActivity().finish()
            }
        })
    }

    private fun setUserData() {
        val email = auth.currentUser?.email
        binding.userName.text = email?.substring(0, email?.indexOf("@")) + "님"
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
            fireBaseLoginViewModel.logout()
          //  Log.d(TAG, "login Type : $login_type")
           // LoginInstance.getLoginInstance(requireContext(), requireActivity(), login_type!!).logout()
        }

       // binding.deleteAccount.setOnClickListener {
          //  LoginInstance.getLoginInstance(requireContext(), requireActivity(), login_type!!).deleteAccount()
      //  }

        // 파티 하나 액티비티 디자인 열람용. 추후 삭제
        binding.hotPartyButton.setOnClickListener {
            val intent = Intent(requireContext(), OnePartyActivity::class.java)
            startActivity(intent)
        }
    }

    private fun partyButtonListener() {
        if (flag) {
            binding.myPartyButton.setBackgroundResource(R.drawable.tap_background_colored)
            binding.myPartyButton.setTextColor(resources.getColor(R.color.white))
            binding.hotPartyButton.setBackgroundResource(R.drawable.tap_background_default)
            binding.hotPartyButton.setTextColor(resources.getColor(R.color.line))
            flag = false
        }
        else {
            binding.hotPartyButton.setBackgroundResource(R.drawable.tap_background_colored)
            binding.hotPartyButton.setTextColor(resources.getColor(R.color.white))
            binding.myPartyButton.setBackgroundResource(R.drawable.tap_background_default)
            binding.myPartyButton.setTextColor(resources.getColor(R.color.line))
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
