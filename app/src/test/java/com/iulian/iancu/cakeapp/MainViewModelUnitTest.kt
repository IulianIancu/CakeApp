package com.iulian.iancu.cakeapp

import com.iulian.iancu.cakeapp.data.CakeListRepository
import com.iulian.iancu.cakeapp.data.CakeService
import com.iulian.iancu.cakeapp.ui.main.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MainViewModelUnitTest {
    @InjectMockKs
    lateinit var viewModel: MainViewModel

    @MockK
    lateinit var cakeListRepository: CakeListRepository

    @MockK
    lateinit var cakeService: CakeService

    @Before
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks

    @Test
    fun givenGetCakeList_theStateIsUpdate_Success() {
        
    }
}