package com.example.p_3

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.p_3.ui.theme.P_3Theme

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private val accelerationThreshold = 30f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        setContent {
            P_3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AccelerometerScreen()
                }
            }
        }
    }

    @Composable
    fun AccelerometerScreen() {
        var xValue by remember { mutableStateOf("0.0") }
        var yValue by remember { mutableStateOf("0.0") }
        var zValue by remember { mutableStateOf("0.0") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("X: $xValue")
            Text("Y: $yValue")
            Text("Z: $zValue")
        }

        LaunchedEffect(Unit) {
            sensorManager.registerListener(this@MainActivity, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }

        DisposableEffect(Unit) {
            onDispose {
                sensorManager.unregisterListener(this@MainActivity)
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // Check for significant acceleration
            if (x > accelerationThreshold || y > accelerationThreshold || z > accelerationThreshold) {
                startActivity(Intent(this, AccelerationActivity::class.java))
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used in this example
    }
}
