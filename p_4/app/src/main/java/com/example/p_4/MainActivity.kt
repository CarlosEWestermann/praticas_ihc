package com.example.p_4

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.p_4.ui.theme.P_4Theme

class MainActivity : ComponentActivity(), SensorEventListener, LocationListener {
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var temperatureSensor: Sensor? = null

    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this)
        }

        setContent {
            P_4Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SensorAndLocationScreen()
                }
            }
        }
    }

    @Composable
    fun SensorAndLocationScreen() {
        var lightValue by remember { mutableStateOf("Light: Waiting...") }
        var temperatureValue by remember { mutableStateOf("Temperature: Waiting...") }
        var locationValue by remember { mutableStateOf("Location: Waiting...") }

        LaunchedEffect(Unit) {
            sensorManager.registerListener(this@MainActivity, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(this@MainActivity, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        DisposableEffect(Unit) {
            onDispose {
                sensorManager.unregisterListener(this@MainActivity)
                locationManager.removeUpdates(this@MainActivity)
            }
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(text = lightValue)
            Text(text = temperatureValue)
            Text(text = locationValue)
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_LIGHT -> {
                val light = event.values[0]
                var lightValue = "Light: $light lx"
            }
            Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                val temperature = event.values[0]
                var temperatureValue = "Temperature: $temperature °C"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }

    override fun onLocationChanged(location: Location) {
        TODO("Not yet implemented")
    }

}
