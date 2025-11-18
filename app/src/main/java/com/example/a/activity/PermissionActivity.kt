package com.example.a.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.a.R

class PermissionActivity : AppCompatActivity() {

    private lateinit var closeButton: Button

    // 필수 권한 리스트 (위치, 카메라, 사진)
    private val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    // 선택 권한 리스트 (알림 - Android 13 이상)
    private val optionalPermissions = arrayOf(
        Manifest.permission.POST_NOTIFICATIONS
    )

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
        private const val NOTIFICATION_PERMISSION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        closeButton = findViewById(R.id.close_button)

        // 확인 버튼 클릭 시 권한 요청
        closeButton.setOnClickListener {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        // Android 6.0 이상에서는 런타임 권한 요청 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 필수 권한 먼저 요청 (위치, 카메라, 사진)
            ActivityCompat.requestPermissions(
                this,
                requiredPermissions,
                PERMISSION_REQUEST_CODE
            )
        } else {
            moveToLanguageActivity()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // 필수 권한 이후 선택 권한 요청 (알림)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(
                        this,
                        optionalPermissions,
                        NOTIFICATION_PERMISSION_CODE
                    )
                } else {
                    moveToLanguageActivity()
                }
            }
            NOTIFICATION_PERMISSION_CODE -> {
                moveToLanguageActivity()
            }
        }
    }

    private fun moveToLanguageActivity() {
        val intent = Intent(this, LanguageActivity::class.java)
        startActivity(intent)
        finish()
    }
}