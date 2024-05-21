/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.nam.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.nam.R
import com.example.nam.storage.CacheRepository
import com.example.nam.storage.dto.MetricaCounterResponseDto
import com.example.nam.storage.dto.Website
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun WebsiteSettingsScreen(
    onEditWebsite: (website: Website) -> Unit,
    onAddWebsite: (website: Website) -> Unit,
    onNavUp: () -> Unit
) {
    val websitesJson = CacheRepository.get(CacheRepository.CacheType.WEBSITES)
    val type = object : TypeToken<List<Website>>() {}.type
    val websites = Gson().fromJson<List<Website>>(websitesJson, type)

    val showAddDialog = remember { mutableStateOf(false) }
    val showEditDialog = remember { mutableStateOf(false) }
    val currentWebsite = remember { mutableStateOf(websites.firstOrNull()) }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { onNavUp() },
                shape = RoundedCornerShape(0.dp)
            ) {
                Text(text = stringResource(id = R.string.back))
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { showAddDialog.value = true },
                shape = RoundedCornerShape(0.dp)
            ) {
                Text(text = stringResource(id = R.string.add_new))
            }
        }
        websites.forEach { website ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = website.name)
                Button(
                    onClick = { currentWebsite.value = website },
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text(text = stringResource(id = R.string.edit))
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (showAddDialog.value) {
                showEditDialog.value = false
                AddSiteDialog(onAddWebsite = onAddWebsite) {
                    showAddDialog.value = false
                }
            }
            if (showEditDialog.value && currentWebsite.value != null) {
                showAddDialog.value = false
                EditSiteDialog(
                    website = currentWebsite.value!!,
                    onEditWebsite = onEditWebsite
                ) {
                    showEditDialog.value = false
                }
            }
        }
    }
}

@Composable
fun AddSiteDialog(onAddWebsite: (website: Website) -> Unit, onDismissRequest: () -> Unit) {
    val websiteCopy = remember { mutableStateOf(Website(null, "", -1)) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(0.dp),
        ) {
            TextField(
                value = websiteCopy.value.name,
                onValueChange = {
                    websiteCopy.value.name = it
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = websiteCopy.value.counterId.toString(),
                onValueChange = {
                    websiteCopy.value.counterId = it.toLong()
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onDismissRequest() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.cancel)
                )
            }
            Button(
                onClick = { onAddWebsite(websiteCopy.value) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.save)
                )
            }
        }
    }
}

@Composable
fun EditSiteDialog(website: Website, onEditWebsite: (website: Website) -> Unit, onDismissRequest: () -> Unit) {
    val websiteCopy = remember { mutableStateOf(website) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(0.dp),
        ) {
            TextField(
                value = websiteCopy.value.name,
                onValueChange = {
                    websiteCopy.value.name = it
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = websiteCopy.value.counterId.toString(),
                onValueChange = {
                    websiteCopy.value.counterId = it.toLong()
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onDismissRequest() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.cancel)
                )
            }
            Button(
                onClick = { onEditWebsite(websiteCopy.value) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.save)
                )
            }
        }
    }
}

/*
@Composable
fun WebsiteSettingsScreen2(
    onWebsiteAdd: (website: Website) -> Unit,
    onWebsiteEdit: (website: Website) -> Unit,
    onNavUp: () -> Unit,
) {

    val (apiResponse, setApiResponse) = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val snackbarErrorText = stringResource(id = R.string.feature_not_available)
    val snackbarActionLabel = stringResource(id = R.string.dismiss)

    Scaffold(
        topBar = {
            SignInSignUpTopAppBar(
                topAppBarText = stringResource(id = R.string.sign_in),
                onNavUp = onNavUp,
            )
        },
        content = { contentPadding ->
            SignInSignUpScreen(
                modifier = Modifier.supportWideScreen(),
                contentPadding = contentPadding,
                onSignInAsGuest = onSignInAsGuest,
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SignInContent(
                        email = email,
                        onSignInSubmitted = onSignInSubmitted,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = snackbarErrorText,
                                    actionLabel = snackbarActionLabel
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.forgot_password))
                    }
                }
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        ErrorSnackbar(
            snackbarHostState = snackbarHostState,
            onDismiss = { snackbarHostState.currentSnackbarData?.dismiss() },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun SignInContent(
    email: String?,
    onSignInSubmitted: (email: String, password: String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val focusRequester = remember { FocusRequester() }
        val emailState by rememberSaveable(stateSaver = EmailStateSaver) {
            mutableStateOf(EmailState(email))
        }
        Email(emailState, onImeAction = { focusRequester.requestFocus() })

        Spacer(modifier = Modifier.height(16.dp))

        val passwordState = remember { PasswordState() }

        val onSubmit = {
            if (emailState.isValid && passwordState.isValid) {
                onSignInSubmitted(emailState.text, passwordState.text)
            }
        }
        Password(
            label = stringResource(id = R.string.password),
            passwordState = passwordState,
            modifier = Modifier.focusRequester(focusRequester),
            onImeAction = { onSubmit() }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onSubmit() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = emailState.isValid && passwordState.isValid
        ) {
            Text(
                text = stringResource(id = R.string.sign_in)
            )
        }
    }
}

@Composable
fun ErrorSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { }
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = data.visuals.message,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                action = {
                    data.visuals.actionLabel?.let {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = stringResource(id = R.string.dismiss),
                                color = MaterialTheme.colorScheme.inversePrimary
                            )
                        }
                    }
                }
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom)
    )
}

@Preview(name = "Sign in light theme", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Sign in dark theme", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SignInPreview() {
    AppTheme {
        WebsiteSettingsScreen(
            email = null,
            onSignInSubmitted = { _, _ -> },
            onSignInAsGuest = {},
            onNavUp = {},
        )
    }
}

fun getToken() {
    CoroutineScope(Dispatchers.IO).launch {
        val url = URL(MetricaTokenDto.TOKEN_REQEUST_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val responseCode = connection.responseCode
        val responseBody = connection.inputStream.bufferedReader().use { it.readText() }

        if (responseCode in 200..299) {
            val gson = Gson()
            val post = gson.fromJson(responseBody, Map::class.java)
            setApiResponse(post.toString())
        } else {
            setApiResponse("Error: $responseCode")
        }
    }
}

fun makeApiRequest(setApiResponse: (String) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        val url = URL(MetricaTokenDto.TOKEN_REQEUST_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val responseCode = connection.responseCode
        val responseBody = connection.inputStream.bufferedReader().use { it.readText() }

        if (responseCode in 200..299) {
            val gson = Gson()
            val post = gson.fromJson(responseBody, Map::class.java)
            setApiResponse(post.toString())
        } else {
            setApiResponse("Error: $responseCode")
        }
    }
}
*/