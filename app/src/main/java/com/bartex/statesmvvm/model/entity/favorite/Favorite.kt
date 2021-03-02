package com.bartex.statesmvvm.model.entity.favorite

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite(
    val name :String? = null,
    var area :String? = null,
    val population :String? = null,
    val flag :String? = null
): Parcelable