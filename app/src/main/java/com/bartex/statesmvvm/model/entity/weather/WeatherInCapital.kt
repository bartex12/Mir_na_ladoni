package com.bartex.statesmvvm.model.entity.weather

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherInCapital(
    @Expose val weather: List<Weather>? = null,
    @Expose val main: Main? = null,
    @Expose val sys: Sys? = null,
    @Expose val name: String? = ""
) : Parcelable