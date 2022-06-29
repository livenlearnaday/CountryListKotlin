package io.github.livenlearnaday.countrylistkotlin.ui.base


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import timber.log.Timber


abstract class BaseActivity: AppCompatActivity(){

    private val PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                Timber.e("Permission already granted.")
            } else {

//If the app doesn’t have the CALL_PHONE permission, request it//

                requestPermission()
            }
        }



    }


    open fun checkPermission(): Boolean {
        val CallPermissionResult =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CALL_PHONE)
        return CallPermissionResult == PackageManager.PERMISSION_GRANTED
    }

     fun requestPermission() {
         ActivityCompat.requestPermissions(
             this@BaseActivity, arrayOf(
                 Manifest.permission.CALL_PHONE
             ), PERMISSION_REQUEST_CODE
         )
    }



}