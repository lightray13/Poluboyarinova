package com.test.filmlist.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.test.filmlist.R

object ImageLoader {

    fun loadImage(view: ImageView, url: String, placeholder: Int = R.drawable.ic_baseline_image) {
        Glide.with(view)
            .load(url)
            .transform(RoundedCorners(16))
            .placeholder(placeholder)
            .error(placeholder)
            .fallback(placeholder)
            .into(view)
    }
}