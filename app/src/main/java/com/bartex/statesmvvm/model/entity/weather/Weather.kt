package com.bartex.statesmvvm.model.entity.weather

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
class Weather(
    @Expose val description: String? = null,
    @Expose val icon: String? = null
): Parcelable