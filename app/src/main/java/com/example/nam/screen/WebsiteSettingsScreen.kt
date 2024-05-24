package com.example.nam.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.nam.R
import com.example.nam.storage.WebsiteRepository
import com.example.nam.storage.dto.Website
import com.example.nam.vaidation.DomainValidator
import com.example.nam.vaidation.IntegerValidator

@Composable
fun WebsiteSettingsScreen(
    onEditWebsite: (website: Website) -> Unit,
    onAddWebsite: (website: Website) -> Unit,
    onNavUp: () -> Unit
) {
    val showAddDialog = remember { mutableStateOf(false) }
    val showEditDialog = remember { mutableStateOf(false) }
    val currentWebsite = remember { mutableStateOf(Website.createEmpty()) }

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
        WebsiteRepository.find().forEach { website ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                website.name?.let { Text(text = it) }
                Button(
                    onClick = { currentWebsite.value = website
                              showEditDialog.value = true },
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
            if (showEditDialog.value) {
                showAddDialog.value = false
                EditSiteDialog(
                    website = currentWebsite.value,
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
    val nameTextField = remember { mutableStateOf(TextFieldValue("")) }
    val counterIdTextField = remember { mutableStateOf(TextFieldValue("")) }

    var domainError by remember { mutableStateOf<String?>(null) }
    var counterIdError by remember { mutableStateOf<String?>(null) }

    val isFormValid by remember {
        derivedStateOf {
            domainError == null && counterIdError == null
        }
    }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(0.dp),
        ) {
            TextField(
                modifier = Modifier.padding(16.dp),
                value = nameTextField.value,
                onValueChange = {
                    nameTextField.value = it
                    domainError = DomainValidator.isValid(nameTextField.value.text)
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.website_domain_name))
                },
                isError = domainError != null
            )
            domainError?.let { Text(it) }
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                modifier = Modifier.padding(16.dp),
                value = counterIdTextField.value,
                onValueChange = {
                    counterIdTextField.value = it
                    counterIdError = IntegerValidator.isValid(counterIdTextField.value.text)
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.website_counter_id))
                },
                isError = counterIdError != null
            )
            counterIdError?.let { Text(it) }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onDismissRequest() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.cancel)
                )
            }
            Button(
                enabled = isFormValid,
                onClick = {
                    onAddWebsite(Website(nameTextField.value.text, counterIdTextField.value.text.toLong(), 0))
                    onDismissRequest()
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
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
    val nameTextField = remember { mutableStateOf(TextFieldValue(website.name ?: "")) }
    val counterIdTextField = remember { mutableStateOf(TextFieldValue(website.counterId.toString())) }

    var domainError by remember { mutableStateOf<String?>(null) }
    var counterIdError by remember { mutableStateOf<String?>(null) }

    val isFormValid by remember {
        derivedStateOf {
            domainError == null && counterIdError == null
        }
    }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(0.dp),
        ) {
            TextField(
                modifier = Modifier.padding(16.dp),
                value = nameTextField.value,
                onValueChange = {
                    nameTextField.value = it
                    domainError = DomainValidator.isValid(nameTextField.value.text)
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.website_domain_name))
                },
                isError = domainError != null
            )
            domainError?.let { Text(it) }
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                modifier = Modifier.padding(16.dp),
                value = counterIdTextField.value,
                onValueChange = {
                    counterIdTextField.value = it
                    counterIdError = IntegerValidator.isValid(counterIdTextField.value.text)
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.website_counter_id))
                },
                isError = counterIdError != null
            )
            counterIdError?.let { Text(it) }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onDismissRequest() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.cancel)
                )
            }
            Button(
                enabled = isFormValid,
                onClick = {
                    onEditWebsite(Website(nameTextField.value.text, counterIdTextField.value.text.toLong(), 0))
                    onDismissRequest()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.save)
                )
            }
        }
    }
}