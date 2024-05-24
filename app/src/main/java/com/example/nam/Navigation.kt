package com.example.nam

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nam.Destinations.EMAIL_SETTINGS
import com.example.nam.Destinations.SETTINGS_ROUTE
import com.example.nam.Destinations.WEBSITES_ROUTE
import com.example.nam.Destinations.WEBSITES_SETTINGS_ROUTE
import com.example.nam.screen.EmailSettingsScreen
import com.example.nam.screen.SettingsScreen
import com.example.nam.screen.WebsitesRoute
import com.example.nam.screen.WebsitesSettingsRoute
import com.example.nam.storage.ErrorViewModel

object Destinations {
    const val WEBSITES_ROUTE = "websites"
    const val SETTINGS_ROUTE = "settings"
    const val WEBSITES_SETTINGS_ROUTE = "settings/websites"
    const val EMAIL_SETTINGS = "settings/email"
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    errorViewModel: ErrorViewModel = viewModel(modelClass = ErrorViewModel::class.java)
) {
    MainLayout(navController = navController, errorViewModel = errorViewModel)
}

@Composable
fun MainLayout(
    navController: NavHostController,
    errorViewModel: ErrorViewModel
) {

    val errorText by errorViewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    navController.navigate(WEBSITES_ROUTE) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text(text = stringResource(id = R.string.websites))
            }
            Button(
                onClick = { navController.navigate(SETTINGS_ROUTE) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text(text = stringResource(id = R.string.settings))
            }
        }
    }

    if (errorViewModel.errorMessage.collectAsState().value != null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.error)
                .clickable { errorViewModel.clearErrorMessage() }
        ) {
            Text(
                text = errorText ?: "",
                color = Color(Color.White.value),
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        NavHost(
            navController = navController,
            startDestination = WEBSITES_ROUTE,
        ) {

            composable(WEBSITES_ROUTE) {
                WebsitesRoute(errorViewModel)
            }

            composable(SETTINGS_ROUTE) {
                SettingsScreen(
                    onNavigateToWebsitesSettings = {
                        navController.navigate(WEBSITES_SETTINGS_ROUTE)
                    },
                    onNavigateToEmailSettings= {
                        navController.navigate(EMAIL_SETTINGS)
                    }
                )
            }

            composable(WEBSITES_SETTINGS_ROUTE) {
                WebsitesSettingsRoute(
                    onNavUp = navController::navigateUp,
                    errorViewModel = errorViewModel
                )
            }

            composable(EMAIL_SETTINGS) {
                EmailSettingsScreen(onNavUp = navController::navigateUp)
            }

        }
    }
}
