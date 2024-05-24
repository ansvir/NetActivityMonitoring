

package com.example.nam.screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nam.storage.ErrorViewModel

@Composable
fun WebsitesRoute(errorViewModel: ErrorViewModel) {
    WebsitesScreen(errorViewModel)
}
