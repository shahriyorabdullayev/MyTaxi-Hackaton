package me.shakhriyor.mytaxihackaton.presentation


import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import me.shakhriyor.mytaxihackaton.R
import me.shakhriyor.mytaxihackaton.common.Constants.DEVICE_THEME
import me.shakhriyor.mytaxihackaton.common.SharedPref
import me.shakhriyor.mytaxihackaton.common.service.LocationService


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var configuration: Configuration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar_color)
        configuration = Configuration(resources.configuration)
        requestPermission()
        checkDeviceTheme()
        initNavController()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            startService()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

        }
    }

    private fun startService() {
        Intent(this, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            startService(this)
        }
    }

    private fun initNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ), 0
        )
    }

    private fun checkDeviceTheme() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> SharedPref(this).saveBoolean(DEVICE_THEME, true)
            Configuration.UI_MODE_NIGHT_YES -> SharedPref(this).saveBoolean(DEVICE_THEME, false)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChanged(newConfig)
        configuration = Configuration(newConfig)

    }

    private fun configurationChanged(newConfig: Configuration) {
        if (isNightConfigChanged(newConfig)) {
            recreate()
        }
    }

    private fun isNightConfigChanged(newConfig: Configuration): Boolean {
        return newConfig.diff(configuration) and ActivityInfo.CONFIG_UI_MODE != 0 && isOnDarkMode(
            newConfig
        ) != isOnDarkMode(configuration)
    }

    private fun isOnDarkMode(configuration: Configuration): Boolean {
        return configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }


}