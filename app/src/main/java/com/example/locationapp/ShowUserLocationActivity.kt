package com.example.locationapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.locationapp.databinding.ActivityShowUserLocationBinding
import com.example.locationapp.models.LastCoordinatesModel
import com.example.locationapp.network.LocationService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class ShowUserLocationActivity : AppCompatActivity(), OnMapReadyCallback {
    private var binding: ActivityShowUserLocationBinding? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowUserLocationBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        getLastCoordinates()
    }

    private fun getLastCoordinates() {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val locationsService: LocationService = retrofit.create(LocationService::class.java)

        val call: Call<LastCoordinatesModel> = locationsService.getLastCoordinates(intent.getStringExtra("username")!!)

        call.enqueue(object : Callback<LastCoordinatesModel>{
            override fun onResponse(
                call: Call<LastCoordinatesModel>,
                response: Response<LastCoordinatesModel>
            ) {
                if (response.body()!!.locations.size == 0) {
                    AlertDialog.Builder(this@ShowUserLocationActivity)
                        .setMessage("No coordinates available for this user")
                        .setNeutralButton("Ok") {
                            dialog, _ ->
                            dialog.dismiss()
                            finish()
                        }
                        .show()
                } else {
                    latitude = response.body()!!.locations[0].latitude
                    longitude = response.body()!!.locations[0].longitude

                    val supportMapFragment: SupportMapFragment =
                        supportFragmentManager.findFragmentById(R.id.mapUser) as SupportMapFragment
                    supportMapFragment.getMapAsync(this@ShowUserLocationActivity)
                }
            }

            override fun onFailure(call: Call<LastCoordinatesModel>, t: Throwable) {
                Log.e("retrofit getLastCoord", "Error " + t.message)
            }

        })
    }

    override fun onMapReady(map: GoogleMap) {
        val position = LatLng(latitude!!, longitude!!)

        map.addMarker(MarkerOptions().position(position).title("Current location"))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 11f))
    }
}