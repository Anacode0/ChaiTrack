package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VerifyRecoveryCodeRequest(
    @SerializedName("phone_number")
    val phoneNumber: String,

    @SerializedName("recovery_code")
    val recoveryCode: String
)