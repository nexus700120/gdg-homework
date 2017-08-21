package com.gdg.homework.modules.recycler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gdg.homework.R
import com.gdg.homework.modules.common.domain.WeatherForecast
import com.gdg.homework.modules.common.formatDate

/**
 * Created by vkirillov on 21.08.2017.
 */
class Adapter : RecyclerView.Adapter<ItemViewHolder>() {

    var forecast: WeatherForecast? = null

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        forecast?.list?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = forecast?.list?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ItemViewHolder(view)
    }
}

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mornView: TextView = itemView.findViewById(R.id.morn) as TextView
    private val dayView: TextView = itemView.findViewById(R.id.day) as TextView
    private val eveView: TextView = itemView.findViewById(R.id.eve) as TextView
    private val nightView: TextView = itemView.findViewById(R.id.night) as TextView
    private val dateView: TextView = itemView.findViewById(R.id.date) as TextView

    fun bind(daily: WeatherForecast.DailyForecast) {
        daily.temp?.let {
            mornView.text = "Утро: ${it.morn}°C"
            dayView.text = "День: ${it.day}°C"
            eveView.text = "Вечер: ${it.eve}°C"
            nightView.text = "Ночь: ${it.night}°C"
        } ?: let {
            mornView.text = null
            dayView.text = null
            eveView.text = null
            nightView.text = null
        }

        daily.timestamp?.let {
            dateView.text = formatDate(it)
        } ?: let { dateView.text = null }
    }
}