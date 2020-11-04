package com.example.creptoapp.Data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.creptoapp.Pojo.CoinPriceInfo

@Dao
interface CoinPriceInfoDao {

    @Query("SELECT * FROM full_price_list ORDER BY lastUpdate DESC")
    fun getPriceList(): LiveData<List<CoinPriceInfo>> //метод для выведения списка валют
    // метод для вывода информации отдельной валюты
    @Query("SELECT * FROM full_price_list WHERE fromSymbol == :fSym LIMIT 1")
    fun getPriceInfoAboutCoin(fSym: String): LiveData<CoinPriceInfo>
    //Метод который будет сохранять полученную информацию в базу
    @Insert(onConflict = OnConflictStrategy.REPLACE)// каждый раз когда будут приходить новые данные . старые будут заменяться
    fun insertPriceList(priceList: List<CoinPriceInfo>)
}