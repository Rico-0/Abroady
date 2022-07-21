package com.reve.abroady.base


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.reve.abroady.util.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment<T : ViewDataBinding> : Fragment() { // , R : BaseViewModel
    val TAG: String = this.javaClass.simpleName

   // val mainActivity by lazy { activity as? MainActivity } // MainActivity를 받아옴

    lateinit var binding: T // 바인딩할 뷰

  //  abstract val viewModel: R // 리턴값인 뷰모델 변수

  //  lateinit var viewModelFactory: ViewModelFactory

    abstract val layoutResourceId: Int

    internal val compositeDisposable = CompositeDisposable()

    private lateinit var callback: OnBackPressedCallback

   // val click by lazy { ClickUtil(this.lifecycle) }

  //  val progressBar: ProgressDialog by lazy {
      //  ProgressDialog(requireActivity())
  //  }

    abstract fun initStartView()

  /*  fun observeErrorEvent() {
        viewModel.errorSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.e("error subject ", it.message.toString())
            }
            .addTo(compositeDisposable)
    } */

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        d(TAG, "++onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        d(TAG, "++onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        d(TAG, "++onCreateView!!!")
        // 프래그먼트 뷰가 생성될 때 데이터 바인딩
        binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        binding.lifecycleOwner = this
        // 의존성 주입
       // viewModelFactory = Injection.provideViewModelFactory(activity as Context)
        initStartView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        d(TAG, "++onViewCreated!!!")
    }

    override fun onStart() {
        super.onStart()
        d(TAG, "++onStart")
    }

    override fun onResume() {
        super.onResume()
        d(TAG, "++onResume")
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
        d(TAG, "++onDestroyView")
    }

    override fun onPause() {
        super.onPause()
        d(TAG, "++onPause")
    }

    override fun onStop() {
        super.onStop()
        d(TAG, "++onStop")
    }

    override fun onDetach() {
        super.onDetach()
        d(TAG, "++onDetach()")
    }

    override fun onDestroy() {
        d(TAG, "++onDestroy!!!")
        /*
      서버 통신을 한 화면에서 여러 번 수행하는 경우, 사용자가 화면 이탈 시 한꺼번에 구독 해제시키기 위해 CompositeDisposable에 넣고 onDestroy() 쯤에서 clear()를 호출
      dispose()와는 다르게 새로운 disposable 객체를 다시 받을 수 있다.
        */
        compositeDisposable.clear()
        super.onDestroy()
    }

}