package com.withpet.mobile.utils

import android.content.Context
import androidx.core.content.ContextCompat
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.withpet.mobile.ui.activity.splash.SplashActivity
import android.content.ContextWrapper
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission

object PermissionUtils {

    private const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    private const val LOCATION_PERMISSION_REQUEST_CODE = 1001

    fun initPermissions(context: Context,activity:Activity ,onSuccess:()->Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionCamera = checkSelfPermission(context,Manifest.permission.CAMERA)
            val permissionLocation = checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION)

            val permissionWriteStorage = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                PackageManager.PERMISSION_GRANTED // Android 13 이상에서는 무시
            }

            val permissionReadStorage = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                PackageManager.PERMISSION_GRANTED // Android 13 이상에서는 무시
            }

            if (permissionCamera == PackageManager.PERMISSION_DENIED ||
                permissionWriteStorage == PackageManager.PERMISSION_DENIED ||
                permissionReadStorage == PackageManager.PERMISSION_DENIED ||
                permissionLocation == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf(
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.READ_MEDIA_AUDIO,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                } else {
                    arrayOf(
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                }
                requestPermissions(activity ,permissions, SplashActivity.PERMISSION_REQUEST_ID)
            } else {
                onSuccess.invoke()
            }
        } else {
            onSuccess.invoke()
        }
    }

    // 위치 권한이 허용되었는지 확인합니다.
    fun isLocationPermissionGranted(context: Context): Boolean {
        return isPermissionGranted(context, LOCATION_PERMISSION)
    }


    // 주어진 권한이 허용되었는지 확인하는 유틸리티 메서드.
    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        val permissionState = ContextCompat.checkSelfPermission(context, permission)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission(activity: Activity){
        requestPermissions(activity , arrayOf(LOCATION_PERMISSION), LOCATION_PERMISSION_REQUEST_CODE)
    }
}
