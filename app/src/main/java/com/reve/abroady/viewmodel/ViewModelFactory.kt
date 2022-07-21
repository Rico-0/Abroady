package com.reve.abroady.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.reve.abroady.model.datasource.RemoteDataSource

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val remoteDataSource: RemoteDataSource) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(remoteDataSource) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}



