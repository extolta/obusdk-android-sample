package com.example.obusdk.sampleapp.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

object PermissionUtil {
    //OBU Data Permission = {applicationId}.permission.OBU_DATA_ACCESS
    const val OBU_DATA_PERMISSION = "com.example.obusdk.sampleapp.permission.OBU_DATA_ACCESS"

    @RequiresApi(Build.VERSION_CODES.S)
    val BLUETOOTH_PERMISSION = arrayOf(
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_SCAN
    )

    /**
     * Checks if [Manifest.permission.BLUETOOTH_SCAN] and [Manifest.permission.BLUETOOTH_SCAN]
     * permissions are granted.
     * @param context
     * @return true if granted else false
     */
    @RequiresApi(Build.VERSION_CODES.S)
    fun isBluetoothPermissionGranted(context: Context): Boolean {
        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN
        )
        return isPermissionGranted(context, permissions)
    }

    /**
     * Checks if [OBU_DATA_PERMISSION] permission is granted.
     * @param context
     * @return true if granted else false
     */
    fun isOBUDataPermissionGranted(context: Context): Boolean {
        val permission = arrayOf(
            OBU_DATA_PERMISSION
        )
        return isPermissionGranted(context, permission)
    }

    private fun isPermissionGranted(context: Context, permission: Array<String>): Boolean {
        return permission.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}