package com.bartex.statesmvvm.model.entity.state

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

//Аннотация @Parcelize говорит о необходимости сгенерировать весь boilerplate-код,
// необходимый для работы Parcelable, автоматически, избавляя нас от рутины его написания вручную.
@Parcelize
data class State(
    @Expose val capital :String? = null,
    @Expose val flag :String? = null,
    @Expose val name :String? = null,
    @Expose var region :String? = null,
    @Expose val population :Int? = null,
    @Expose var area :Float? = null,
    @Expose val latlng:Array<Float>? = null
): Parcelable