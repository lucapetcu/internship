package com.example.locationapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ShowLocationActivity : AppCompatActivity(), OnMapReadyCallback {


    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_location)



        val supportMapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

    }

    override fun onMapReady(map: GoogleMap) {
        val position = LatLng(latitude, longitude)

        map.addMarker(MarkerOptions().position(position).title("Current location"))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 11f))
    }

}