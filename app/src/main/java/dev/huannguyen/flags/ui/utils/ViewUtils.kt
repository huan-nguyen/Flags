package dev.huannguyen.flags.ui.utils

import android.content.res.Resources
import android.view.View

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()