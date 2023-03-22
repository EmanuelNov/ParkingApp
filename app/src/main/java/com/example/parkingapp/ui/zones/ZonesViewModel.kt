package com.example.parkingapp.ui.zones

import android.content.res.AssetManager
import androidx.lifecycle.ViewModel
import com.example.parkingapp.data.ParkingZones
import com.example.parkingapp.data.Zone
import com.google.gson.Gson

class ZonesViewModel:ViewModel() {
    var parkingZonesList:ArrayList<Zone> = arrayListOf()
    fun init(zonesAssets:String){

        val gson = Gson()
        val parkingData: ParkingZones = gson.fromJson(zonesAssets, ParkingZones::class.java)
        parkingZonesList.addAll(parkingData.parkingZones)
    }
    fun getHoodsString(neighborhoods:List<String>):String{
        var hoods =""
        neighborhoods.forEach{
            hoods += "$it     "
        }
        return hoods
    }
}
