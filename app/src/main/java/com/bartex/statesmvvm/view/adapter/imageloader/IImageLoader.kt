package com.bartex.statesmvvm.view.adapter.imageloader

interface IImageLoader<T> {
    fun loadInto(url: String, container: T)
}