
package com.sct.proximity

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.content.pm.PackageManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.location.LocationManager
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.*
import android.os.Build

class MainActivity : Activity() {

    private lateinit var info: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 35, 28, 25)
            setBackgroundColor(Color.rgb(240, 246, 255))
        }

        val title = TextView(this).apply {
            text = "SURYA COOL TEKNIK"
            textSize = 25f
            setTextColor(Color.rgb(20, 75, 160))
            gravity = Gravity.CENTER
        }

        val subtitle = TextView(this).apply {
            text = "PROXIMITY MARKETING 1.0"
            textSize = 16f
            gravity = Gravity.CENTER
            setTextColor(Color.DKGRAY)
        }

        info = TextView(this).apply {
            text = "Status GPS: Belum diperiksa\n" +
                    "Status Bluetooth: Belum diperiksa\n" +
                    "Jangkauan target: 50 meter"
            textSize = 16f
            setTextColor(Color.BLACK)
            setPadding(15, 35, 10, 35)
        }

        val gpsButton = Button(this).apply {
            text = "PERIKSA GPS"
            setOnClickListener { checkGPS() }
        }

        val bluetoothButton = Button(this).apply {
            text = "PERIKSA BLUETOOTH"
            setOnClickListener { checkBluetooth() }
        }

        val permissionButton = Button(this).apply {
            text = "IZINKAN AKSES"
            setOnClickListener { requestAccess() }
        }

        layout.addView(title)
        layout.addView(subtitle)
        layout.addView(info)
        layout.addView(permissionButton)
        layout.addView(gpsButton)
        layout.addView(bluetoothButton)

        setContentView(layout)
    }

    private fun requestAccess() {
        val permissions = mutableListOf<String>()

        if (checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            permissions.add(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }

        if (Build.VERSION.SDK_INT >= 31) {
            if (checkSelfPermission(
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(
                    Manifest.permission.BLUETOOTH_SCAN
                )
            }

            if (checkSelfPermission(
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            }
        }

        if (permissions.isNotEmpty()) {
            requestPermissions(permissions.toTypedArray(), 100)
        } else {
            info.text = "Izin GPS dan Bluetooth sudah diberikan."
        }
    }

    private fun checkGPS() {
        val manager = getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

        val enabled = manager.isProviderEnabled(
            LocationManager.GPS_PROVIDER
        )

        info.text = if (enabled) {
            "GPS: AKTIF\nJangkauan target: 50 meter"
        } else {
            "GPS: BELUM AKTIF\nSilakan aktifkan lokasi HP."
        }
    }

    private fun checkBluetooth() {
        val manager = getSystemService(
            Context.BLUETOOTH_SERVICE
        ) as BluetoothManager

        val adapter: BluetoothAdapter? = manager.adapter

        info.text = when {
            adapter == null -> "Bluetooth tidak tersedia."
            checkSelfPermission(
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED &&
                Build.VERSION.SDK_INT >= 31 ->
                "Izin Bluetooth belum diberikan."
            adapter.isEnabled -> "Bluetooth: AKTIF"
            else -> "Bluetooth: NONAKTIF"
        }
    }
}
