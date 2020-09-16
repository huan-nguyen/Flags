package dev.huannguyen.flags.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.huannguyen.flags.R
import dev.huannguyen.flags.domain.Flag
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class FlagAdapter : ListAdapter<Flag, FlagViewHolder>(FlagItemDiffCallback) {

    init {
        setHasStableIds(true)
    }

    private val clickChannel = BroadcastChannel<Pair<View, Flag>>(1)
    val clicks: Flow<Pair<View, Flag>> = clickChannel.asFlow()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.flag_item, parent, false)
        return FlagViewHolder(view, clickChannel)
    }

    override fun onBindViewHolder(holder: FlagViewHolder, position: Int) {
        holder.populate(getItem(position))
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).country.hashCode().toLong()
    }
}

private object FlagItemDiffCallback : DiffUtil.ItemCallback<Flag>() {
    override fun areItemsTheSame(oldItem: Flag, newItem: Flag): Boolean {
        return oldItem.country == newItem.country
    }

    override fun areContentsTheSame(oldItem: Flag, newItem: Flag): Boolean {
        return oldItem == newItem
    }
}