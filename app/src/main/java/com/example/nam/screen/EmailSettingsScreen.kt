package com.example.nam.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.nam.R
import com.example.nam.storage.EmailRepository
import com.example.nam.storage.dto.Setting
import com.example.nam.storage.dto.Website

@Composable
fun EmailSettingsScreen(
    onNavUp: () -> Unit
) {
    val smtpAccountField = remember { mutableStateOf(TextFieldValue("")) }
    val smtpAccountPasswordField = remember { mutableStateOf(TextFieldValue("")) }
    val smtpAccountDomainField = remember { mutableStateOf(TextFieldValue("")) }
    val smtpAccountPortField = remember { mutableStateOf(TextFieldValue("")) }
    val smtpAccountUseSslCheckbox = remember { mutableStateOf(false) }
    val reportEmailField = remember { mutableStateOf(TextFieldValue("")) }
    val smtpEmailFromTextField = remember { mutableStateOf(TextFieldValue("")) }

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
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = stringResource(id = R.string.email_settings))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = smtpAccountField.value,
                onValueChange = {
                    smtpAccountField.value = it
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.smtp_account))
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = smtpAccountPasswordField.value,
                onValueChange = {
                    smtpAccountPasswordField.value = it
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.smtp_account_password))
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = smtpAccountDomainField.value,
                onValueChange = {
                    smtpAccountDomainField.value = it
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.smtp_account_domain))
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = smtpAccountPortField.value,
                onValueChange = {
                    smtpAccountPortField.value = it
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.smtp_account_port))
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically
        ){
            Checkbox(
                checked = smtpAccountUseSslCheckbox.value,
                onCheckedChange = {
                    smtpAccountUseSslCheckbox.value = it
                },
            )
            Text(text = stringResource(id = R.string.smtp_account_use_ssl))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = reportEmailField.value,
                onValueChange = {
                    reportEmailField.value = it
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.report_email))
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = smtpEmailFromTextField.value,
                onValueChange = {
                    smtpEmailFromTextField.value = it
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.smtp_email_from_text))
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Button(
                onClick = {
                        EmailRepository.save(Setting(null,
                            smtpAccount = smtpAccountField.value.text,
                            smtpAccountPassword = smtpAccountPasswordField.value.text,
                            smtpAccountDomain = smtpAccountDomainField.value.text,
                            smtpAccountPort = smtpAccountPortField.value.text.toInt(),
                            smtpAccountUseSs = smtpAccountUseSslCheckbox.value,
                            reportEmail = reportEmailField.value.text,
                            smtpEmailFromText = smtpEmailFromTextField.value.text))
                    },
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