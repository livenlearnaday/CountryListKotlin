package io.github.livenlearnaday.countrylistkotlin.utils

import androidx.appcompat.widget.AppCompatImageView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import io.github.livenlearnaday.countrylistkotlin.R

fun AppCompatImageView.loadUrl(url: String) {
    val imageLoader = ImageLoader.Builder(context)
        .components { add(SvgDecoder.Factory()) }
        .build()

    val request = ImageRequest.Builder(context)
        .crossfade(true)
        .crossfade(500)
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.ic_baseline_error_outline_24)
        .data(url)
        .target(this)
        .build()

    imageLoader.enqueue(request)
}