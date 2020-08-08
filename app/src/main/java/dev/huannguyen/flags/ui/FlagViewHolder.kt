package dev.huannguyen.flags.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.huannguyen.flags.domain.Flag
import kotlinx.android.synthetic.main.flag_item.view.imageView
import kotlinx.android.synthetic.main.flag_item.view.name
import kotlinx.coroutines.channels.BroadcastChannel

class FlagViewHolder(
    itemView: View,
    eventChannel: BroadcastChannel<Pair<View, Flag>>
) : RecyclerView.ViewHolder(itemView) {
    private var data: Flag? = null

    init {
        itemView.setOnClickListener {
            eventChannel.offer(it.imageView to requireNotNull(data))
        }
    }

    fun populate(data: Flag) {
        this.data = data
        data.run {
            Picasso.get()
                .load(url)
                .into(itemView.imageView)

            itemView.name.text = country
            // For shared element transition purposes. Assuming country name is unique
            itemView.imageView.transitionName = country
        }
    }
}