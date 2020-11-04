package com.example.creptoapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_coin_detail.*

class CoinDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: CoinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_detail)
        viewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(CoinViewModel::class.java)
        if (!intent.hasExtra(EXTRA_FROM_SYMBOL)){
            finish()
            return
        }//проверка если интент не содержит такой ключ то мы выйдем из этой активити
        val fromSymbol = intent.getStringExtra(EXTRA_FROM_SYMBOL) // создаём интент
        viewModel.getDetailInfo(fromSymbol).observe(this,Observer{
            tvPrice.text=it.price // присваиваем значения
            tvMinPrice.text=it.lowDay
            tvMaxPrice.text=it.highDay
            tvLastMarket.text=it.lastMarket
            tvLastUpdate.text=it.getFormatTime()
            tvFromSymbol.text=it.fromSymbol
            tvToSymbol.text=it.toSymbol
            Picasso.get().load(it.getFullImgUrl()).into(ivLogoCoin)
        })
    }
    companion object{
      private  const val EXTRA_FROM_SYMBOL="fSym"

        fun newnItent(context: Context,fromSymbol: String):Intent{ // метод для создания и передачи интента
            val intent=Intent(context,CoinDetailActivity::class.java)
            intent.putExtra(EXTRA_FROM_SYMBOL,fromSymbol)
            return intent
        }
    }
}
