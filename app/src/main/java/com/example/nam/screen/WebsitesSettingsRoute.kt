

package com.example.nam.screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WebsitesSettingsRoute(
    onNavUp: () -> Unit,
) {
    val mainViewModel: MainViewModel = viewModel(factory = MainViewModel.MainViewModelFactory())
    mainViewModel.getAllWebsites()
    WebsiteSettingsScreen(
        onEditWebsite = {
            mainViewModel.editWebsite(it)
        },
        onAddWebsite = {
            mainViewModel.addWebsite(it)
        },
        onNavUp = onNavUp
    )
}
