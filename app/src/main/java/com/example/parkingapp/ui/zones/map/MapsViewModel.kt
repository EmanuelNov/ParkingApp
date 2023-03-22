package com.example.parkingapp.ui.zones.map

import androidx.lifecycle.ViewModel
import com.example.parkingapp.data.ParkingZones
import com.example.parkingapp.data.Zone
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

class MapsViewModel: ViewModel() {

    var parkingZonesList:ArrayList<Zone> = arrayListOf()

    fun init(zonesAssets:String){

        val gson = Gson()
        val parkingData: ParkingZones = gson.fromJson(zonesAssets, ParkingZones::class.java)
        parkingZonesList.addAll(parkingData.parkingZones)
    }

    fun getPolygonPoints(points:List<String>):List<LatLng>{
        return points.map{
            val lat = it.substring(0,it.indexOf(",")).toDouble()
            val lng = it.substring(it.indexOf(",") + 1, it.length).toDouble()

            LatLng(lng,lat)
        }
    }
}