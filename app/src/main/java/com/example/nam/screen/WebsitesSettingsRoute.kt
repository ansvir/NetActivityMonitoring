

package com.example.nam.screen

import androidx.compose.runtime.Composable
import com.example.nam.storage.ErrorViewModel
import com.example.nam.storage.WebsiteService
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
            WebsiteService.editWebsite(it)
        },
        onAddWebsite = {
            WebsiteRepository.save(it, errorViewModel)
            WebsiteService.addWebsite(it, errorViewModel)
        },
        onNavUp = onNavUp
    )
}
