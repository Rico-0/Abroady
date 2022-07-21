package com.reve.abroady.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

// 현재 CompositeDisposable에 객체를 추가해서 리턴
fun Disposable.addTo(compositeDisposable: CompositeDisposable): Disposable =
    apply { compositeDisposable.add(this) }