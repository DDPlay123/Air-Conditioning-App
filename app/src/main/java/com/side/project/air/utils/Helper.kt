package com.side.project.air.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.side.project.air.R
import com.side.project.air.utils.Contracts.location_permission

fun Activity.requestLocationPermission(): Boolean {
    if (!Method.requestPermission(this, *location_permission)) {
        displayShortToast(getString(R.string.toast_ask_location_permission))
        return false
    }
    return true
}

fun String.floatFormat(): String =
    "%.1f".format(this.toFloat())

fun Context.displayShortToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()