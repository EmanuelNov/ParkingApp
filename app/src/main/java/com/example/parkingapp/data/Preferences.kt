package com.example.parkingapp.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object Preferences {
    private const val SHARED_PREFERENCES_KEY = "parking_app_preferences"
    private const val LAST_USED_PLATES_KEY = "last_used_car_plates"
    private const val LAST_USED_ZONE_KEY = "last_used_zone"
    private const val PLATES_KEY = "car_plates"
    lateinit var prefs:SharedPreferences

    fun init(ctx:Context){
        prefs = ctx.getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE)
    }
    fun saveCarPlates(plates:String){
        val platesSet = mutableSetOf<String>()
        platesSet.addAll(getCarPlates()!!)
        platesSet.add(plates)
        prefs.edit().putStringSet(PLATES_KEY, platesSet).apply()
    }
    fun getCarPlates() = prefs.getStringSet(PLATES_KEY, emptySet())

    fun removeCarPlate(plate: String){
        val platesSet = mutableSetOf<String>()
        platesSet.addAll(getCarPlates()!!)
        platesSet.remove(plate)
        prefs.edit().putStringSet(PLATES_KEY, platesSet).apply()
    }


    fun saveLastUsedCarPlate(carPlates:String){
        prefs.edit().putString(LAST_USED_PLATES_KEY, carPlates).apply()
    }
    fun getLastUsedPlate() = prefs.getString(LAST_USED_PLATES_KEY,"")

    fun saveLastUsedZone(zone:String){
        prefs.edit().putString(LAST_USED_ZONE_KEY, zone).apply()
    }
    fun getLastUsedZone() = prefs.getString(LAST_USED_ZONE_KEY,"")
}