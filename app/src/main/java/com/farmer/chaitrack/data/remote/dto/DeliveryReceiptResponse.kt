package com.farmer.chaitrack.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DeliveryReceiptResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: DeliveryReceiptData?
)

data class DeliveryReceiptData(
    @SerializedName("receipt")
    val receipt: ReceiptInfo,

    @SerializedName("qr_code_url")
    val qrCodeUrl: String?,

    @SerializedName("pdf_url")
    val pdfUrl: String?
)

data class ReceiptInfo(
    @SerializedName("receipt_number")
    val receiptNumber: String,

    @SerializedName("delivery_date")
    val deliveryDate: String,

    @SerializedName("delivery_time")
    val deliveryTime: String,

    @SerializedName("farmer_name")
    val farmerName: String,

    @SerializedName("farmer_id")
    val farmerId: String,

    @SerializedName("collection_center")
    val collectionCenter: String,

    @SerializedName("weight_kg")
    val weightKg: Double,

    @SerializedName("price_per_kg")
    val pricePerKg: Double,

    @SerializedName("total_amount")
    val totalAmount: Double,

    @SerializedName("clerk_name")
    val clerkName: String,

    @SerializedName("clerk_signature")
    val clerkSignature: String?,

    @SerializedName("grade")
    val grade: String?,

    @SerializedName("notes")
    val notes: String?,

    @SerializedName("issued_at")
    val issuedAt: String
)