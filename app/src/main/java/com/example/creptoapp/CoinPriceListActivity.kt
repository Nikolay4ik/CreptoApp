package com.example.creptoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.creptoapp.Adapter.CoinInfoAdapter
import com.example.creptoapp.Pojo.CoinPriceInfo
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.coin_price_list_activity.*

class CoinPriceListActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var viewModel: CoinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.coin_price_list_activity)
        val adapter = CoinInfoAdapter(this)
        adapter.onCoinClikLictner = object : CoinInfoAdapter.OnCoinClikLictner {
            override fun onCoinClick(coinPriceInfo: CoinPriceInfo) {
                val intent = CoinDetailActivity.newnItent(
                    this@CoinPriceListActivity,
                    coinPriceInfo.fromSymbol
                ) // так правельно передовать
                startActivity(intent)

            }
        }
        rcCoinPriceList.adapter = adapter
        viewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(CoinViewModel::class.java) // реализация viewModel
        viewModel.priceList.observe(this, Observer {
            adapter.coinInfoList = it
        })


    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}

