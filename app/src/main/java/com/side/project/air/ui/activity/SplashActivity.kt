package com.side.project.air.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.side.project.air.R
import com.side.project.air.utils.Contracts.PERMISSION_COARSE_LOCATION
import com.side.project.air.utils.Contracts.PERMISSION_CODE
import com.side.project.air.utils.Contracts.PERMISSION_FINE_LOCATION
import com.side.project.air.utils.displayShortToast
import com.side.project.air.utils.requestLocationPermission

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var splashScreen: SplashScreen

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                for (result in grantResults)
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        when {
                            permissions.any { it == PERMISSION_FINE_LOCATION || it == PERMISSION_COARSE_LOCATION } ->
                                displayShortToast(getString(R.string.toast_ask_location_permission))

                            else -> Unit
                        }
                        return
                    }
                doInitialize()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { true }
        super.onCreate(savedInstanceState)

        doInitialize()
    }

    private fun doInitialize() {
        if (!requestLocationPermission()) return
        startActivity(Intent(this, MainActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}