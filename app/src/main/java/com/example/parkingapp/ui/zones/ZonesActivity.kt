package com.example.parkingapp.ui.zones

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.parkingapp.databinding.ActivityZonesBinding
import com.example.parkingapp.databinding.ItemZonesBinding
import com.example.parkingapp.ui.home.readAssetsFile
import com.example.parkingapp.ui.zones.map.MapsActivity

class ZonesActivity: AppCompatActivity(){
    companion object{
        val ZONE_KEY = "zone"
    }

    lateinit var binding: ActivityZonesBinding
    lateinit var viewModel:ZonesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityZonesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ZonesViewModel::class.java)
        viewModel.init(this.assets.readAssetsFile("parking_zones.json"))
        initZonesList()
        binding.backTextView.setOnClickListener { onBackPressed() }
    }

    fun initZonesList(){
        for(zone in viewModel.parkingZonesList){
            val itemBinding = ItemZonesBinding.inflate(layoutInflater)
            itemBinding.zoneName.text = zone.zoneName
            itemBinding.pricePerHour.text = "1 Euro/hour"
            zone.neighborhoods?.let{
                itemBinding.zoneNeighborhoods.text = viewModel.getHoodsString(it)
            }
            itemBinding.viewOnMapButton.setOnClickListener {
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra(ZONE_KEY, zone.zoneName)
                startActivity(intent)
            }
            binding.zonesHolder.addView(itemBinding.root)
        }
    }
}