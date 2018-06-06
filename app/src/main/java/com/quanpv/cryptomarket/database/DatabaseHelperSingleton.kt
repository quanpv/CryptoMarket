package com.quanpv.cryptomarket.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quanpv.cryptomarket.model.CoinFavoritesStructures

import java.lang.reflect.Type
import java.util.ArrayList
import java.util.Arrays
import java.util.HashMap

/**
 * Created by QuanPham on 6/5/2018.
 */

class DatabaseHelperSingleton private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Grab the user's list of favorite coins out of the DB and save them into an object that
    // represents the coins as a list and as a hashtable. The hashtable can be used for fast lookups
    // The list will maintain the order of the user's favorite coins
    // Get a connection to the DB
    // Pull the favorites out of the DB
    // Move the cursor to the first element returned by the query
    // Get the list represented as a string out of the cursor
    // Instantiate a new serializer so that we can convert the string from the DB into a real list object
    // Tell Gson what type we want to convert the serialized string to
    // Load the string of the user's favorite coins into a list of strings!
    // Instantiate a new hashmap and put all of the items in the list above into the hashmap
    // Return a wrapper object that holds both the list and the hashmap
    val favorites: CoinFavoritesStructures
        get() {
            val db = this.readableDatabase
            val cursor = db.rawQuery("select FAVORITES from " + DATABASE_TABLE, null)
            cursor.moveToPosition(0)
            val favoritesListString = cursor.getString(0)
            cursor.close()
            val gson = Gson()
            val type = object : TypeToken<ArrayList<String>>() {

            }.type
            val favoritesList = gson.fromJson<ArrayList<String>>(favoritesListString, type)
            val favoritesMap = HashMap<String, String>()
            for (i in favoritesList.indices) {
                favoritesMap.put(favoritesList[i], favoritesList[i])
            }
            return CoinFavoritesStructures(favoritesList, favoritesMap)
        }

    // Run this method when the DB Schema is upgraded
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }

    // Run this method the first time the DB is ever created
    override fun onCreate(db: SQLiteDatabase) {
        // Drop the table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE)
        // Create the new table with 2 columns. One for ID and one for the coin list
        db.execSQL("CREATE TABLE $DATABASE_TABLE ($FAVORITE_COINS_COL_0 INTEGER PRIMARY KEY AUTOINCREMENT, $FAVORITE_COINS_COL_1 TEXT)")
        // Convert the defaultCoinsList string into a list of strings. Use comma as the delimeter
        val defaultCoinsList = ArrayList(Arrays.asList(*DEFAULT_FAVORITE_COINS.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
        // Instantiate a serializer so we can easily load/store the list in the DB
        val gson = Gson()
        // Serialize the list of strings into a JSON payload that we can store in the DB
        val favoritesListString = gson.toJson(defaultCoinsList)
        // Put the serialized paylout into a ContentValues object to prepare it for storage
        val defaultFavoriteCoins = ContentValues()
        defaultFavoriteCoins.put(FAVORITE_COINS_COL_1, favoritesListString)
        // Insert the list into the DB
        db.insert(DATABASE_TABLE, null, defaultFavoriteCoins)
    }

    // This will allow us to save the user's coin favorites any time by passing in a wrapper object
    // that contains the list of the user's favorite coins as well as the hashtable representation
    fun saveCoinFavorites(favs: CoinFavoritesStructures) {
        // Get a writeable connection to the db
        val db = this.writableDatabase
        // Instantiate a new serializer so that we can convert our list of coins back into a string
        // that is storable in the DB
        val gson = Gson()
        // Convert the list of the user's favorite coins into a string
        val favoritesListString = gson.toJson(favs.favoriteList)
        // Put the serialized paylout into a ContentValues object to prepare it for storage
        val newCoinFavorites = ContentValues()
        newCoinFavorites.put(FAVORITE_COINS_COL_1, favoritesListString)
        // Update the row in the DB with the new list of favorites!
        db.update(DATABASE_TABLE, newCoinFavorites, null, null)
    }

    companion object {

        private var sInstance: DatabaseHelperSingleton? = null

        private val DATABASE_NAME = "CryptoMarketQpv.db"
        private val DATABASE_TABLE = "favorites_list"
        private val DATABASE_VERSION = 1
        private val FAVORITE_COINS_COL_0 = "ID"
        private val FAVORITE_COINS_COL_1 = "FAVORITES"
        private val DEFAULT_FAVORITE_COINS = "BTC,ETH,XRP,LTC,BCH"

        // Use singleton design pattern so there is only ever one DB object floating around
        @Synchronized
        fun getInstance(context: Context): DatabaseHelperSingleton {

            if (sInstance == null) {
                sInstance = DatabaseHelperSingleton(context.applicationContext)
            }
            return sInstance as DatabaseHelperSingleton
        }
    }
}
