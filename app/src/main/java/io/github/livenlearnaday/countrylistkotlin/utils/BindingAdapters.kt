package io.github.livenlearnaday.countrylistkotlin.utils

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import io.github.livenlearnaday.countrylistkotlin.R
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(url)
            .placeholder(
                R.drawable.ic_baseline_error_outline_24)
            .transform(CircleCrop())
            .into(view)
    }
}

@BindingAdapter("svgImageUrl")
fun loadSvgImage(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        val uri = Uri.parse(url)
        GlideToVectorYou
            .init()
            .with(view.context)
            .setPlaceHolder(
                R.drawable.border,
                R.drawable.ic_baseline_error_outline_24
            ) //loading,error
            .load(uri, view)
    }
}

@BindingAdapter("dateFormatter")
fun TextView.dateFormatter(string: String?) {
    if (string?.isNotEmpty() == true) {
        val date = SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(string)
        date?.let {
            val format = SimpleDateFormat("dd/MM/yyy", Locale.getDefault())
            this.text = format.format(it)
        }

    }

}