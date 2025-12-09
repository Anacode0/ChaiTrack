package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateBankAccountRequest(
    @SerializedName("bank_name")
    val bankName: String,

    @SerializedName("account_number")
    val accountNumber: String,


    @SerializedName("account_name")
    val accountName: String = "",

    @SerializedName("branch")
    val branch: String? = null,

    @SerializedName("swift_code")
    val swiftCode: String? = null
)