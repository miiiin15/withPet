package com.withpet.mobile.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.withpet.mobile.R
import com.withpet.mobile.data.repository.CommonRepo
import com.withpet.mobile.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    companion object {
        private const val PERMISSION_REQUEST_ID = 11
        private var requestPermissionGranted = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initPermissions()
    }

    private fun checkVersionAndNavigate() {
        CommonRepo.getVersion(
            success = {
                if (it.result.code == 200) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    Toast.makeText(this, "${it.payload}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "${it.error?.message.toString()}", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            networkFail = {
                Toast.makeText(this, "네트웍실패", Toast.LENGTH_SHORT).show()
            },
            failure = {
                Toast.makeText(this, "그냥실패", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun initPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionCamera = checkSelfPermission(Manifest.permission.CAMERA)
            val permissionWriteStorage =
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val permissionLocation = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)

            if (permissionCamera == PackageManager.PERMISSION_DENIED ||
                permissionWriteStorage == PackageManager.PERMISSION_DENIED ||
                permissionLocation == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                requestPermissions(permissions, PERMISSION_REQUEST_ID)
            } else {
                requestPermissionGranted = true
                checkVersionAndNavigate()
            }
        } else {
            Log.e("권한", "PermissionGrant run with old way")
            requestPermissionGranted = true
            checkVersionAndNavigate()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ID) {
            requestPermissionGranted =
                grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (requestPermissionGranted) {
                checkVersionAndNavigate()
            } else {
                Toast.makeText(this, "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

