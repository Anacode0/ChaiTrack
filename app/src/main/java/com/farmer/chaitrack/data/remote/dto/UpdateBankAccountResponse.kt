package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateBankAccountResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: UpdateBankAccountData?
)

data class UpdateBankAccountData(
    @SerializedName("updated_account")
    val updatedAccount: BankDetails,

    @SerializedName("next_payment_affected")
    val nextPaymentAffected: Boolean
)