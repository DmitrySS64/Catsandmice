package ru.sergeevdmitry8i11.catsandmice

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import ru.sergeevdmitry8i11.catsandmice.placeholder.PlaceholderContent.PlaceholderItem
import ru.sergeevdmitry8i11.catsandmice.databinding.FragmentStatsBinding


class MyItemRecyclerViewAdapter(
    private val values: List<GameStats>
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentStatsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentStatsBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context

        private val idView: TextView = binding.itemNumber
        private val time: TextView = binding.itemTime
        private val totalClicksView: TextView = binding.totalClicks
        private val mouseClicksView: TextView = binding.mouseClicks
        private val percentView: TextView = binding.percent

        fun bind(item: GameStats){
            idView.text = context.getString(R.string.stats_item_id, item.id)
            time.text = context.getString(R.string.stats_item_time, item.gameTime)
            totalClicksView.text = context.getString(R.string.stats_item_total_clicks, item.totalClicks)
            mouseClicksView.text = context.getString(R.string.stats_item_mouse_clicks, item.mouseClicks)
            percentView.text = context.getString(R.string.stats_item_percentage_of_hits, item.percentage.toInt())
        }
    }

}