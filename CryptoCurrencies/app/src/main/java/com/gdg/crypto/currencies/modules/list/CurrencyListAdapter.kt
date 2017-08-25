package com.gdg.crypto.currencies.modules.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.gdg.crypto.currencies.R
import com.gdg.crypto.currencies.modules.common.domain.CurrencyInfo
import org.greenrobot.eventbus.EventBus

/**
 * Created by vkirillov on 24.08.2017.
 */
class CurrencyListAdapter: RecyclerView.Adapter<CurrencyListAdapter.CurrencyViewHolder>() {

    var items = listOf<CurrencyInfo>()

    private lateinit var requestManager: RequestManager

    private val requestOptions = RequestOptions()
            .circleCrop()

            .error(R.drawable.ic_currency_placeholder)
            .placeholder(R.drawable.ic_currency_placeholder)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        requestManager = Glide.with(recyclerView)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder =
            CurrencyViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.currenci_list_item, parent, false))

    fun pauseImagesLoading() {
        requestManager.pauseRequests()
    }

    fun resumeImagesLoading() {
        requestManager.resumeRequests()
    }

    inner class CurrencyViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

        private val name = view.findViewById<TextView>(R.id.name)
        private val coinName = view.findViewById<TextView>(R.id.coin_name)
        private val icon = view.findViewById<ImageView>(R.id.icon)

        fun bind(info: CurrencyInfo) {
            name.text = info.name
            coinName.text = info.coinName

            icon.setBackgroundResource(R.drawable.ic_currency_placeholder)

            if (!info.imageUrl.isNullOrEmpty()) {
                icon.setBackgroundResource(R.drawable.ic_currency_placeholder)
                requestManager.load(info.imageUrl)
                        .apply(requestOptions)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(icon)
            }
            view.setOnClickListener {
                EventBus.getDefault().post(OnCurrencySelectedEvent(info))
            }
        }
    }
}