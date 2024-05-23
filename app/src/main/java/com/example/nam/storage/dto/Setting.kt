package com.example.nam.storage.dto

data class Setting(var id: Int?,
                   val smtpAccount: String,
                   val smtpAccountPassword: String,
                   val smtpAccountDomain: String,
                   val smtpAccountPort: Int,
                   val smtpAccountUseSs: Boolean,
                   val reportEmail: String,
                   val smtpEmailFromText: String)