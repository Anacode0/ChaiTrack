package com.farmer.chaitrack.ui.finance

import java.io.File

data class FinanceState(
    val isLoading: Boolean = false,
    val selectedMonth: Int? = null,
    val selectedYear: Int? = null,
    val totalWeight: Double = 0.0,
    val pricePerKg: Double = 0.0,
    val totalEarnings: Double = 0.0,
    val pdfGenerated: Boolean = false,
    val pdfFile: File? = null,
    val shouldShare: Boolean = false,
    val issueReported: Boolean = false,
    val error: String? = null
)