package com.bartex.statesmvvm.view.adapter.imageloader

import android.app.Activity
import android.net.Uri
import android.widget.ImageView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

class GlideToVectorYouLoader(private val activity: Activity):
    IImageLoader<ImageView> {

    override fun loadInto(url: String, container: ImageView) {
        GlideToVectorYou
            .init()
            .with(activity)
            .load(Uri.parse(url), container)
    }
}