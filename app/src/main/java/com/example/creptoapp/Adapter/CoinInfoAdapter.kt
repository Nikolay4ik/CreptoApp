package com.example.creptoapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.creptoapp.Pojo.CoinPriceInfo
import com.example.creptoapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_coin_info.view.*

class CoinInfoAdapter(private val context:Context) : RecyclerView.Adapter<CoinInfoAdapter.CoinInfoViveHolder>() {
    var coinInfoList:List<CoinPriceInfo> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onCoinClikLictner:OnCoinClikLictner?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViveHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_coin_info, parent, false)
        return CoinInfoViveHolder(view)
    }

    override fun getItemCount(): Int {
        return coinInfoList.size
    }



    override fun onBindViewHolder(holder: CoinInfoViveHolder, position: Int) {
        val coin = coinInfoList[position]
        with(holder) {

                val symbolTable = context.resources.getString(R.string.symbols_template)
                val lasrOfDate = context.resources.getString(R.string.last_update_template)
                tvSymbols.text =String.format(symbolTable,coin.fromSymbol,coin.toSymbol)  // можно не указывать слово holder и coin
                tvPrice.text =coin. price
                tvLastUpdate.text = String.format(lasrOfDate,coin.getFormatTime())
                Picasso.get().load(coin.getFullImgUrl()).into(logoCoin)
                itemView.setOnClickListener{
                    onCoinClikLictner?.onCoinClick(coin.copy())


            }
        }

    }

    inner class CoinInfoViveHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logoCoin = itemView.ivLogoCoin
        val tvSymbols = itemView.tvSymbols
        val tvPrice = itemView.tvPrice
        val tvLastUpdate = itemView.tvLastUpdate

    }

    interface OnCoinClikLictner{ // для кликабельности объектов в списке ресайклвив
        fun onCoinClick(coinPriceInfo: CoinPriceInfo)

    }
}