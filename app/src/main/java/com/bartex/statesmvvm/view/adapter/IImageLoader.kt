package com.bartex.statesmvvm.view.adapter

interface IImageLoader<T> {
    fun loadInto(url: String, container: T)
}