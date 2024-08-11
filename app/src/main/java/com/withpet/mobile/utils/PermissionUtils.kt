package com.withpet.mobile.utils

import android.content.Context
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager

object PermissionUtils {

    private const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION

    // 위치 권한이 허용되었는지 확인합니다.
    fun isLocationPermissionGranted(context: Context): Boolean {
        return isPermissionGranted(context, LOCATION_PERMISSION)
    }

    // 주어진 권한이 허용되었는지 확인하는 유틸리티 메서드.
    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        val permissionState = ContextCompat.checkSelfPermission(context, permission)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }
}
