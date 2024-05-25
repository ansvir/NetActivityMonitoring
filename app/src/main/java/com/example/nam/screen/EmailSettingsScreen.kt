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
import com.example.nam.R
import com.example.nam.storage.EmailRepository
import com.example.nam.storage.dto.Setting
import com.example.nam.vaidation.DomainValidator
import com.example.nam.vaidation.EmailValidator
import com.example.nam.vaidation.IntegerValidator
import com.example.nam.vaidation.LengthValidator

@Composable
fun EmailSettingsScreen(
    onNavUp: () -> Unit
) {

    var smtpAccountError by remember { mutableStateOf<String?>(null) }
    var smtpAccountPasswordError by remember { mutableStateOf<String?>(null) }
    var smtpAccountDomainError by remember { mutableStateOf<String?>(null) }
    var smtpAccountPortError by remember { mutableStateOf<String?>(null) }
    var reportEmailError by remember { mutableStateOf<String?>(null) }
    var smtpEmailFromTextError by remember { mutableStateOf<String?>(null) }

    val isFormValid by remember {
        derivedStateOf {
            smtpAccountError == null && smtpAccountPasswordError == null
                    && smtpAccountDomainError == null
                    && smtpAccountPortError == null
                    && reportEmailError == null
                    && smtpEmailFromTextError == null
        }
    }

    val email = remember { mutableStateOf(EmailRepository.find()) }

    val smtpAccountField = remember { mutableStateOf(TextFieldValue(email.value?.smtpAccount?: "")) }
    val smtpAccountPasswordField = remember { mutableStateOf(TextFieldValue(email.value?.smtpAccountPassword?: "")) }
    val smtpAccountDomainField = remember { mutableStateOf(TextFieldValue(email.value?.smtpAccountDomain?: "")) }
    val smtpAccountPortField = remember { mutableStateOf(TextFieldValue(email.value?.smtpAccountPort.toString()?: "")) }
    val smtpAccountUseSslCheckbox = remember { mutableStateOf(email.value?.smtpAccountUseSsl?: true) }
    val reportEmailField = remember { mutableStateOf(TextFieldValue(email.value?.reportEmail?: "")) }
    val smtpEmailFromTextField = remember { mutableStateOf(TextFieldValue(email.value?.smtpEmailFromText?: "")) }

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
                    smtpAccountError = EmailValidator.isValid(smtpAccountField.value.text)
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.smtp_account))
                },
                isError = smtpAccountError != null
            )
            smtpAccountError?.let { Text(text = it) }
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
                    smtpAccountPasswordError = LengthValidator.isValid(smtpAccountPasswordField.value.text, 2, 19)
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.smtp_account_password))
                },
                isError = smtpAccountPasswordError != null
            )
            smtpAccountPasswordError?.let { Text(text = it) }
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
                    smtpAccountDomainError = DomainValidator.isValid(smtpAccountDomainField.value.text)
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.smtp_account_domain))
                },
                isError = smtpAccountDomainError != null
            )
            smtpAccountDomainError?.let { Text(text = it) }
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
                    smtpAccountPortError = IntegerValidator.isValid(smtpAccountPortField.value.text)
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.smtp_account_port))
                },
                isError = smtpAccountPortError != null
            )
            smtpAccountPortError?.let { Text(text = it) }
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
                    reportEmailError = EmailValidator.isValid(reportEmailField.value.text)
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.report_email))
                },
                isError = reportEmailError != null
            )
            reportEmailError?.let { Text(text = it) }
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
                    smtpEmailFromTextError = EmailValidator.isValid(smtpEmailFromTextField.value.text)
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.smtp_email_from_text))
                },
                isError = smtpEmailFromTextError != null
            )
            smtpEmailFromTextError?.let { Text(text = it) }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Button(
                enabled = isFormValid,
                onClick = {
                        EmailRepository.save(Setting(
                            smtpAccount = smtpAccountField.value.text,
                            smtpAccountPassword = smtpAccountPasswordField.value.text,
                            smtpAccountDomain = smtpAccountDomainField.value.text,
                            smtpAccountPort = smtpAccountPortField.value.text.toInt(),
                            smtpAccountUseSsl = smtpAccountUseSslCheckbox.value,
                            reportEmail = reportEmailField.value.text,
                            smtpEmailFromText = smtpEmailFromTextField.value.text))
                        onNavUp()
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