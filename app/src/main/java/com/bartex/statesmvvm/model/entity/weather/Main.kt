package com.bartex.statesmvvm.model.entity.weather

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
class Main(
    @Expose val temp :Float? = 0f,
    @Expose val pressure :Int? = 0,
    @Expose val humidity :Int? = 0
): Parcelable