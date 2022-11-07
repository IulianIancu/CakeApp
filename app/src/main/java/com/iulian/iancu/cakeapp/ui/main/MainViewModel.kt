package com.iulian.iancu.cakeapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iulian.iancu.cakeapp.data.Cake
import com.iulian.iancu.cakeapp.data.CakeListRepository
import kotlinx.coroutines.*

class MainViewModel constructor(
    //TODO Inject with dagger instead of this nonsense
    private val cakeListRepository: CakeListRepository
) : ViewModel() {
    private val _state = MutableLiveData(State(null, null))
    val state: LiveData<State> get() = _state

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _state.postValue(_state.value?.copy(error = Error.Unknown))
    }

    private var job: Job? = null

    fun getCatFacts() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = cakeListRepository.getCakeList()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    val cakes = response.body()?.filter {
                        !(it.image.isEmpty() || it.title.isEmpty() || it.desc.isEmpty())
                    }?.distinct()?.sortedBy {
                        it.title
                    }

                    _state.postValue(
                        _state.value?.copy(
                            error = null,
                            cakeList = cakes
                        )
                    )
                } else {
                    _state.postValue(_state.value?.copy(error = Error.Network))
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}

data class State(
    val error: Error?,
    val cakeList: List<Cake>?
)

sealed class Error {
    object Network : Error()
    object Unknown : Error()
}