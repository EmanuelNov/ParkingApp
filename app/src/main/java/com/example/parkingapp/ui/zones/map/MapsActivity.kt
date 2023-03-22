package com.example.parkingapp.ui.zones.map

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.parkingapp.R
import com.example.parkingapp.ui.home.readAssetsFile
import com.example.parkingapp.ui.zones.ZonesActivity.Companion.ZONE_KEY
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var viewModel: MapsViewModel
    lateinit var backTextView: TextView
    private var zoneName: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        zoneName = intent.getStringExtra(ZONE_KEY)
        viewModel = ViewModelProvider(this).get(MapsViewModel::class.java)
        viewModel.init(this.assets.readAssetsFile("parking_zones.json"))
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        backTextView = findViewById(R.id.backTextView)
        backTextView.setOnClickListener { onBackPressed() }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val pr = LatLng(42.6629, 21.1655)

        mMap.moveCamera(CameraUpdateFactory.newLatLng(pr))
        mMap.setMinZoomPreference(14f)
        val zone = viewModel.parkingZonesList.find { it.zoneName == zoneName }
        zone?.let {
            val polygon: Polygon = mMap.addPolygon(
                PolygonOptions().clickable(true).addAll(
                    viewModel.getPolygonPoints(it.zonePolygonCoordinates)
                )
            )
            polygon.setFillColor(Color.parseColor("#40${it.zoneColor}"))
            polygon.strokeColor = Color.parseColor("#${it.zoneColor}")
        }

    }
}