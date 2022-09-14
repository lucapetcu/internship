package com.example.locationapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.locationapp.R
import com.example.locationapp.databinding.ActivityShowUserLocationBinding
import com.example.locationapp.viewmodels.ShowUserLocationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowUserLocationActivity : AppCompatActivity(), OnMapReadyCallback {
    private var binding: ActivityShowUserLocationBinding? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    private lateinit var viewModel: ShowUserLocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowUserLocationBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel = ViewModelProvider(this)[ShowUserLocationViewModel::class.java]

        viewModel.liveCoordinates.observe(this) {
            if (it.size == 0) {
                AlertDialog.Builder(this@ShowUserLocationActivity)
                    .setMessage("No coordinates available for this user")
                    .setNeutralButton("Ok") {
                            dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                return@observe
            }

            latitude = it[0].latitude
            longitude = it[0].longitude

            val supportMapFragment: SupportMapFragment =
                        supportFragmentManager.findFragmentById(R.id.mapUser) as SupportMapFragment
                    supportMapFragment.getMapAsync(this@ShowUserLocationActivity)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        val position = LatLng(latitude!!, longitude!!)

        map.addMarker(MarkerOptions().position(position).title("Current location"))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 11f))
    }
}