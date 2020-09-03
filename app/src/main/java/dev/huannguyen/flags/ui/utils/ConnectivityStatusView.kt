package dev.huannguyen.flags.ui.utils

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import com.google.android.material.textview.MaterialTextView
import dev.huannguyen.flags.R
import dev.huannguyen.flags.connectivity.ConnectivityStatus

class ConnectivityStatusView(context: Context, attrs: AttributeSet) : MaterialTextView(context, attrs) {

    init {
        minHeight = 40.toPx()
        gravity = Gravity.CENTER
        setTextColor(context.getColor(android.R.color.white))
        setTextAppearance(R.style.TextAppearance_MaterialComponents_Body2)
    }

    fun set(status: ConnectivityStatus, animationAction: () -> Unit) {
        when (status) {
            ConnectivityStatus.Connected -> {
                setBackgroundColor(context.getColor(android.R.color.holo_green_dark))
                text = context.getString(R.string.back_online)
            }

            else -> {
                setBackgroundColor(context.getColor(android.R.color.holo_red_dark))
                text = context.getString(R.string.connection_lost)
            }
        }

        animationAction()
    }
}