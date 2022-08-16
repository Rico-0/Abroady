package com.reve.abroady.presentation.base

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

open class BaseViewModel : ViewModel() {

    val TAG = this.javaClass.simpleName

    val errorSubject: BehaviorSubject<Throwable> = BehaviorSubject.create<Throwable>()

    val loadingSubject: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    val compositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable){
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        Log.d(TAG, "++onCleared()")
        compositeDisposable.clear()
        super.onCleared()
    }
}