package com.globa.balinasofttestapp.network.api.model

import com.google.gson.annotations.SerializedName

data class CommentPost(
    @SerializedName("text") val text: String
)

data class Comment(
    @SerializedName("id") val id: Int,
    @SerializedName("date") val date: Long,
    @SerializedName("text") val text: String
)