package com.example.parkingapp.data


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Zone (
        @SerializedName("zone_name") val zoneName:String,
        @SerializedName("phone_number") val phoneNumber:String,
        @SerializedName("zone_polygon_coordinates") val zonePolygonCoordinates: List<String>,
        @SerializedName("neighborhoods") val neighborhoods:List<String>,
        @SerializedName("zone_color") val zoneColor:String,
        @SerializedName("zone_max_duration") val zoneMaxDuration:Int,
        @SerializedName("zone_price") val zonePrice:Float
        ): Parcelable