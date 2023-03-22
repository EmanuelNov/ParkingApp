package com.example.parkingapp.ui.home

import android.content.res.AssetManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkingapp.data.ParkingZones
import com.example.parkingapp.data.Zone
import com.google.gson.Gson

class HomeViewModel:ViewModel(){
    var parkingZonesList: MutableList<Zone> = mutableListOf()
    var payNowEnabled :MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var plates:String? = null
        set(value) {
            field = value
            enablePayNowButton()
        }

    var zone:Zone? = null
        set(value) {
            field = value
            enablePayNowButton()
        }

    fun init(zonesAssets:String){

        val gson = Gson()
        val parkingData: ParkingZones= gson.fromJson(zonesAssets, ParkingZones::class.java)
        parkingZonesList.addAll(parkingData.parkingZones)
    }

    fun enablePayNowButton(){
        payNowEnabled.value = zone != null && plates !=null
    }

}
fun AssetManager.readAssetsFile(fileName:String):String = open(fileName).bufferedReader().use { it.readText() }