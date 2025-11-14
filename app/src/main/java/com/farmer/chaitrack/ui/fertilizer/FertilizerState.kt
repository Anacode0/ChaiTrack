package com.farmer.chaitrack.ui.fertilizer

import com.farmer.chaitrack.domain.model.FertilizerApplication

data class FertilizerState(
    val isLoading: Boolean = false,
    val currentApplication: FertilizerApplication? = null,
    val applicationHistory: List<FertilizerApplication> = emptyList(),
    val canApply: Boolean = false,
    val error: String? = null
)