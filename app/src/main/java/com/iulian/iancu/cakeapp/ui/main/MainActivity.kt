@file:OptIn(ExperimentalMaterialApi::class)

package com.iulian.iancu.cakeapp.ui.main

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.iulian.iancu.cakeapp.R.drawable
import com.iulian.iancu.cakeapp.R.string
import com.iulian.iancu.cakeapp.data.Cake
import com.iulian.iancu.cakeapp.data.CakeListRepository
import com.iulian.iancu.cakeapp.data.CakeService
import com.iulian.iancu.cakeapp.ui.theme.CakeAppTheme

class MainActivity : ComponentActivity() {
    var cakeList = emptyList<Cake>()

    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            //TODO This should be replaced with some sort of DI (Dagger)
            MainViewModelFactory(
                CakeListRepository(CakeService.getInstance())
            )
        )[MainViewModel::class.java]

        viewModel.state.observe(this, ::onStateChange)
        setContent {
            Content()
        }
        viewModel.getCakeList()
    }

    private fun onStateChange(state: State?) {
        if (state == null) return
        when (state.error) {
            Error.Network ->
                Toast.makeText(this, string.network_error, Toast.LENGTH_SHORT).show()
            Error.Unknown ->
                Toast.makeText(this, string.unknown_error, Toast.LENGTH_SHORT).show()
            else -> {
                //TODO maybe some analytics
            }
        }

        state.cakeList?.let { cakeList = it }

        setContent {
            Content()
        }
    }

    @Preview
    @Composable
    fun Content() {
        val imageLoader = ImageLoader.Builder(this)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .error(drawable.ic_baseline_signal_wifi_off_24)
            .build()

        val isRefreshing by viewModel.isRefreshing.collectAsState()

        CakeAppTheme {
            Surface(color = MaterialTheme.colors.background) {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = { viewModel.refresh() },
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
                    ) {
                        items(cakeList) {
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                val openDialog = remember { mutableStateOf(false) }
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable {
                                            openDialog.value = !openDialog.value
                                        }
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        AsyncImage(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(4.dp),
                                            placeholder = painterResource(drawable.ic_baseline_timer_24),
                                            model = it.image,
                                            contentDescription = null,
                                            alignment = Alignment.Center,
                                            imageLoader = imageLoader
                                        )
                                        Text(text = it.title, Modifier.padding(4.dp))
                                        if (openDialog.value) {
                                            Text(text = it.desc, Modifier.padding(4.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
