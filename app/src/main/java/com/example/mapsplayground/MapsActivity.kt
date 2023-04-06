package com.example.mapsplayground

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.graphics.BitmapFactory
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.example.mapsplayground.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import java.io.File
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback
{
    companion object
    {
        const val REQUEST_LOCATION_PERMISSION = 1
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap)
    {
        mMap = googleMap

        // Request permission to access the user's location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        )
        {
            // Enable the location layer on the map
            mMap.isMyLocationEnabled = true

            // Get the user's last known location
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val lastKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            // Center the map on the user's current location
            if (lastKnownLocation != null)
            {
                val currentLocation =
                    LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))

                // Add markers for bicycles around Viana do Castelo
                addMarkers()
            }
        }
        else
        {
            // Request permission to access the user's location
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun addMarkers()
    {
        val icon = BitmapDescriptorFactory.fromResource(R.drawable.baseline_directions_bike_24)

        // Add 10 random markers around Viana do Castelo
        val vianaDoCastelo = LatLng(41.6932300, -8.8328700)
        val random = Random()
        for (i in 1..10)
        {
            // Randomly offset the latitude and longitude by up to 0.05 degrees
            val offsetLat = random.nextDouble() * 0.1 - 0.05
            val offsetLng = random.nextDouble() * 0.1 - 0.05
            val markerPosition =
                LatLng(vianaDoCastelo.latitude + offsetLat, vianaDoCastelo.longitude + offsetLng)
            mMap.addMarker(
                MarkerOptions()
                    .position(markerPosition)
                    .title("Bicycle $i")
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_directions_bike_24))
            )
        }
    }
}

