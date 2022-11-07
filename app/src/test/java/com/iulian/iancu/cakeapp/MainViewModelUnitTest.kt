@file:OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)

package com.iulian.iancu.cakeapp

import com.iulian.iancu.cakeapp.data.Cake
import com.iulian.iancu.cakeapp.data.CakeListRepository
import com.iulian.iancu.cakeapp.data.CakeService
import com.iulian.iancu.cakeapp.ui.main.Error.Network
import com.iulian.iancu.cakeapp.ui.main.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.BufferedSource
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MainViewModelUnitTest {
    lateinit var viewModel: MainViewModel
    lateinit var cakeListRepository: CakeListRepository

    @MockK
    lateinit var cakeService: CakeService

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        Dispatchers.setMain(mainThreadSurrogate)
        cakeListRepository = CakeListRepository(cakeService)
        viewModel = MainViewModel(cakeListRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun givenGetCakeList_theStateIsUpdate_Success() = runTest {
        launch(Dispatchers.Main) {
            val testCake = Cake("Test cake", "Test cake desc", "Test cake image")
            coEvery { cakeListRepository.getCakeList() } returns Response.success(listOf(testCake))
            viewModel.getCakeList()

            coVerify { cakeListRepository.getCakeList() }
            Assert.assertEquals(testCake, viewModel.state.value?.cakeList?.first())
        }
    }

    @Test
    fun givenGetCakeList_theStateIsUpdate_Failure() = runTest {
        launch(Dispatchers.Main) {
            coEvery { cakeListRepository.getCakeList() } returns Response.error(404,
                object : ResponseBody() {
                    override fun contentLength(): Long {
                        return 404
                    }

                    override fun contentType(): MediaType? {
                        return null
                    }

                    override fun source(): BufferedSource {
                        TODO("I don't even")
                    }
                })

            viewModel.getCakeList()

            coVerify { cakeListRepository.getCakeList() }
            Assert.assertEquals(Network, viewModel.state.value?.error)
        }
    }
}