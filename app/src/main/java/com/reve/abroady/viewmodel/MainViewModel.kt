package com.reve.abroady.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.reve.abroady.base.BaseViewModel
import com.reve.abroady.model.data.post.AllPostGetModel
import com.reve.abroady.model.data.post.OnePostGetModel
import com.reve.abroady.model.data.post.OnePostSendModel
import com.reve.abroady.model.datasource.RemoteDataSource
import com.reve.abroady.util.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val remoteDataSource: RemoteDataSource
) : BaseViewModel() {

    private val _postData = MutableLiveData<List<OnePostGetModel>>()
    val postData: LiveData<List<OnePostGetModel>>
        get() = _postData

    fun getAllPost() {
        remoteDataSource.getAllPost()
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
            remoteDataSource.sendPost(post)
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