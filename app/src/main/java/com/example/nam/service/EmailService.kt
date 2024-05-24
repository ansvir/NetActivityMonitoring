package com.example.nam.service

import android.util.Log
import com.example.nam.storage.ErrorViewModel
import com.example.nam.storage.dto.Setting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Properties
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

object EmailService {

    const val EMAIL_TAG = "[EMAIL-SENDER]"

    suspend fun sendEmail(
        setting: Setting,
        attachmentFile: File,
        errorViewModel: ErrorViewModel
    ) {
        withContext(Dispatchers.IO) {
            try {
                val properties = Properties()
                properties["mail.smtp.host"] = setting.smtpAccountDomain
                properties["mail.smtp.port"] = setting.smtpAccountPort
                properties["mail.smtp.auth"] = "true"
                properties["mail.smtp.starttls.enable"] = setting.smtpAccountUseSsl

                val session = Session.getInstance(properties, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(setting.smtpAccount, setting.smtpAccountPassword)
                    }
                })

                val message = MimeMessage(session)
                message.setFrom(InternetAddress(setting.smtpEmailFromText))
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(setting.reportEmail))
                message.subject = "ПС мониторинга интернет-сайтов"
                val messageBodyPart = MimeBodyPart()
                messageBodyPart.setText("Отчёт о мониторинге веб-сайтов.")

                val multipart = MimeMultipart()
                multipart.addBodyPart(messageBodyPart)
                val attachmentBodyPart = MimeBodyPart()
                val dataSource = FileDataSource(attachmentFile)
                attachmentBodyPart.dataHandler = DataHandler(dataSource)
                attachmentBodyPart.fileName = attachmentFile.name
                multipart.addBodyPart(attachmentBodyPart)

                message.setContent(multipart)

                Transport.send(message)
                Log.d(EMAIL_TAG, "E-mail сообщение отправлено успешно")
            } catch (e: Exception) {
                errorViewModel.setErrorMessage("Ошибка отправки e-mail сообщения!")
                Log.e(EMAIL_TAG, "Ошибка отправки e-mail сообщения, подробности: ${e.message}")
            }
        }
    }
}