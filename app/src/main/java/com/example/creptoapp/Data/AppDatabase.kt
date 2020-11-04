package com.example.creptoapp.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.creptoapp.Pojo.CoinInfoListOfData
import com.example.creptoapp.Pojo.CoinPriceInfo

@Database(entities = [CoinPriceInfo::class] , version = 3 , exportSchema = false)//нужно передать массив классов которые помечены анотацией entitie
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private var db: AppDatabase? = null
        private const val DB_NAME = "main.db"
        private val LOCK=Any()
        fun getInstance(context: Context): AppDatabase {
            synchronized(LOCK){
            db?.let { return it }//проверка на нулабельность , если не нулл то вернёт значение db
            val instance: AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).build()// переменную ввели что бы не проверять на нуллабельность
            db = instance
            return instance
            }
        }
    }
    abstract fun coinPriceInfoDao(): CoinPriceInfoDao
}