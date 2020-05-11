package dev.huannguyen.flags.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.huannguyen.flags.domain.Flag
import kotlinx.android.synthetic.main.details_fragment.view.imageView
import kotlinx.android.synthetic.main.flag_item.view.name

interface OnItemClickListener {
    fun onClick(data: Flag, sharedElement: View)
}

class FlagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun populate(data: Flag, clickListener: OnItemClickListener) {
        data.run {
            Picasso.with(itemView.context)
                .load(url)
                .into(itemView.imageView)

            itemView.name.text = country

            itemView.setOnClickListener { clickListener.onClick(data, itemView.imageView) }
            // For shared element transition purposes. Assuming country name is unique
            itemView.imageView.transitionName = country
        }
    }
}