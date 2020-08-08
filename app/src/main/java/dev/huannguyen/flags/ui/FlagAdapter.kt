package dev.huannguyen.flags.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.huannguyen.flags.R
import dev.huannguyen.flags.domain.Flag
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class FlagAdapter : RecyclerView.Adapter<FlagViewHolder>() {

    private var items: List<Flag> = emptyList()
    private val clickChannel = BroadcastChannel<Pair<View, Flag>>(1)

    val clicks: Flow<Pair<View, Flag>> = clickChannel.asFlow()

    fun set(items: List<Flag>) {
        if (this.items != items) {
            this.items = items
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.flag_item, parent, false)
        return FlagViewHolder(view, clickChannel)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: FlagViewHolder, position: Int) {
        holder.populate(items[position])
    }
}
