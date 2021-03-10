package com.bartex.statesmvvm.view.fragments.details

sealed class DetailsSealed {
    data class Success(val isFavorite:Boolean): DetailsSealed()
    data class Error(val error:Throwable): DetailsSealed()
}