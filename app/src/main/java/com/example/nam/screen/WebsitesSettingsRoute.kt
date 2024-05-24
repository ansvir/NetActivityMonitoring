package com.example.nam.screen

import androidx.compose.runtime.Composable
import com.example.nam.storage.ErrorViewModel
import com.example.nam.service.WebsiteService
import com.example.nam.storage.WebsiteRepository

@Composable
fun WebsitesSettingsRoute(
    onNavUp: () -> Unit,
    errorViewModel: ErrorViewModel
) {
    WebsiteService.getAllWebsites(errorViewModel)
    WebsiteSettingsScreen(
        onEditWebsite = {
            WebsiteRepository.edit(it)
        },
        onAddWebsite = {
            WebsiteService.addWebsite(it, errorViewModel)
        },
        errorViewModel = errorViewModel,
        onNavUp = onNavUp
    )
}
