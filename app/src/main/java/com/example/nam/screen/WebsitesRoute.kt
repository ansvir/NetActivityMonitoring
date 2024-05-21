

package com.example.nam.screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WebsitesRoute() {
    val mainViewModel: MainViewModel = viewModel(factory = MainViewModel.MainViewModelFactory())
    WebsitesScreen(
        mainViewModel.getAllWebsites()
    )
}
