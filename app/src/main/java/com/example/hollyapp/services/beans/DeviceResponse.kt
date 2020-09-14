package com.example.hollyapp.services.beans

data class DeviceResponse(
    val copyright: String,
    val has_more: Boolean,
    val num_results: Int,
    val results: List<MovieReviewBean>,
    val status: String
)