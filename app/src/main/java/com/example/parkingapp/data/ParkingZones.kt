package com.example.parkingapp.data

import com.google.gson.annotations.SerializedName

data class ParkingZones (
    @SerializedName("parking_zones") val parkingZones:List<Zone>
        )