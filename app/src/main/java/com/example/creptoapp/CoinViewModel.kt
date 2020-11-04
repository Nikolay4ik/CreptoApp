package com.example.creptoapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.creptoapp.Api.ApiFactory
import com.example.creptoapp.Data.AppDatabase
import com.example.creptoapp.Pojo.CoinPriceInfo
import com.example.creptoapp.Pojo.CoinPriceInfoRawData
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CoinViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)

    val priceList = db.coinPriceInfoDao().getPriceList()//теперь если в активити подпишимя на него то он будет отображаться в обсервере

    private val compositeDisposable = CompositeDisposable()

    fun getDetailInfo(fSym:String):LiveData<CoinPriceInfo>{// метод который возвращает детальную информацию об одной валюте
        return db.coinPriceInfoDao().getPriceInfoAboutCoin(fSym)
    }
    init {
        loadData()
    }

   private fun loadData() {
        val disposable = ApiFactory.apiService.getTopCoinsInfo(limit = 50)//получаем валюты
            .map { it.data?.map { it.coinInfo?.name }?.joinToString(",") }//получаем из CoinInfoListOfData datum и работаем с одни полем name joinToString {  }
            // передаём коллекцию строк и он их соеденяет в одну
            .flatMap { ApiFactory.apiService.getFullPriceList(fSyms = it) }//возьмёт строку и передаст внутрь блока getFullPriceList , всю инфу о валюте
            .map { getPriceListFromRawData(it) }//переобразуем
            .delaySubscription(10,TimeUnit.SECONDS)//задержка в 10 секунд
            .repeat()//метод в rxJava который будет бесконечно выполнять загрузку будет повторять пока всё успешно
            .retry()//метод в rxJava который будет бесконечно выполнять загрузку будет повторять когда не успешно всё прошло
            .subscribeOn(Schedulers.io())
            .subscribe({
                db.coinPriceInfoDao().insertPriceList(it)//добавляем в базу данных
               // Log.d("TEST_OF_LOADING_DATA", "Success:  $it")
            }, {
              //  Log.d("TEST_OF_LOADING_DATA", "Failure: ${it.message}")
            })
        compositeDisposable.add(disposable)
    }//метод для загрузки данных из сети

    private fun getPriceListFromRawData(coinPriceInfoRawData: CoinPriceInfoRawData): List<CoinPriceInfo> {//метод для парсинга json объекта
        val result = ArrayList<CoinPriceInfo>()
        val jsonObject = coinPriceInfoRawData.CoinPriceInfoJsonObject ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(currencyJson.getAsJsonObject(currencyKey), CoinPriceInfo::class.java)
                result.add(priceInfo)
            }
        }
        return result
    }

    override fun onCleared() { //для метода работы с сеткой
        super.onCleared()
        compositeDisposable.dispose()
    }
}