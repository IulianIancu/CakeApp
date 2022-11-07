package com.iulian.iancu.cakeapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iulian.iancu.cakeapp.data.CakeListRepository

// TODO This should probably provide the repository with dagger
class MainViewModelFactory(
    private val cakeListRepository: CakeListRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(cakeListRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}