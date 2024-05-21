

package com.example.nam.screen

import androidx.compose.runtime.Composable
import com.example.nam.storage.WebsiteService
import com.example.nam.storage.WebsiteRepository

@Composable
fun WebsitesSettingsRoute(
    onNavUp: () -> Unit,
) {
    WebsiteService.getAllWebsites()
    WebsiteSettingsScreen(
        onEditWebsite = {
            WebsiteRepository.edit(it)
            WebsiteService.editWebsite(it)
        },
        onAddWebsite = {
            WebsiteRepository.save(it)
            WebsiteService.addWebsite(it)
        },
        onNavUp = onNavUp
    )
}
