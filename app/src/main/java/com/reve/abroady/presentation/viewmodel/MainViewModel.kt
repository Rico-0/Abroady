package com.reve.abroady.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.reve.abroady.presentation.base.BaseViewModel
import com.reve.abroady.data.entity.post.OnePostGetModel
import com.reve.abroady.data.entity.post.OnePostSendModel
import com.reve.abroady.data.repository.RemoteRepository
import com.reve.abroady.util.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val remoteRepository: RemoteRepository
) : BaseViewModel() {

    private val _postData = MutableLiveData<List<OnePostGetModel>>()
    val postData: LiveData<List<OnePostGetModel>>
        get() = _postData

    fun getAllPost() {
        remoteRepository.getAllPost()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { // 디버깅 시 사용됨
                Log.d(TAG, "getAllPost Error : ${it}")
            }
            .unsubscribeOn(Schedulers.io())
            .subscribe({ response ->
                if (response.isSuccessful) {
                    response.body()?.let {
                        _postData.postValue(response.body()?.postList)
                    }
                }
            }, {
                errorSubject.onNext(it)
            }).addTo(compositeDisposable)
    }

    fun sendPost(post: OnePostSendModel) {
        addDisposable(
            remoteRepository.sendPost(post)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    if (response.isSuccessful) {
                        if (response.body()?.httpStatus == 200) {
                            val postList = (postData.value ?: emptyList()) + post
                            _postData.postValue(postList as List<OnePostGetModel>?)
                        }
                    }
                }, {
                    errorSubject.onNext(it)
                })
        )
    }
}