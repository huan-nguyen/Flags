package dev.huannguyen.flags.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.huannguyen.flags.R
import dev.huannguyen.flags.domain.Flag

class FlagAdapter(private val clickListener: OnItemClickListener) : RecyclerView.Adapter<FlagViewHolder>() {

    private var items: List<Flag> = emptyList()

    fun set(items: List<Flag>) {
        if (this.items != items) {
            this.items = items
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlagViewHolder {
        return FlagViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.flag_item, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: FlagViewHolder, position: Int) {
        holder.populate(items[position], clickListener)
    }
}
