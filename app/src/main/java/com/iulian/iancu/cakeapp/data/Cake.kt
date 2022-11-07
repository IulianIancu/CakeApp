package com.iulian.iancu.cakeapp.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Cake(
    @Expose
    @SerializedName("title")
    val title: String,
    @Expose
    @SerializedName("desc")
    val desc: String,
    @Expose
    @SerializedName("image")
    val image: String,
)