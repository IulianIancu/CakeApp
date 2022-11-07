package com.iulian.iancu.cakeapp.data

//TODO this class should live in its own data module, for clean architecture
class CakeListRepository constructor(private val retrofitService: CakeService) {
    suspend fun getCakeList() =
        retrofitService.getCakeList()
}