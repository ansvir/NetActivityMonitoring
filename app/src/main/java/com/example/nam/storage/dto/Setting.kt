package com.example.nam.storage.dto

data class Setting(val smtpAccount: String,
                   val smtpAccountPassword: String,
                   val smtpAccountDomain: String,
                   val smtpAccountPort: Int,
                   val smtpAccountUseSsl: Boolean,
                   val reportEmail: String,
                   val smtpEmailFromText: String)