package dev.decagon.godday.xkcdcomicsapp.common.utils

import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dev.decagon.godday.xkcdcomicsapp.common.R
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

inline fun CoroutineScope.createExceptionHandler(
    message: String,
    crossinline action: (throwable: Throwable) -> Unit
) = CoroutineExceptionHandler { _, throwable ->
    Log.e("CoroutineScope", message, throwable)
    throwable.printStackTrace()

    launch {
        action(throwable)
    }
}

fun ImageView.setImage(url: String) {
    Glide.with(this.context)
        .load(url.ifEmpty { null })
        .error(R.drawable.ic_baseline_cloud_off_24)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}