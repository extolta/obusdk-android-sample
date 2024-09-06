package com.example.obusdk.sampleapp

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.obusdk.sampleapp.util.PermissionUtil
import com.example.obusdk.sampleapp.util.PermissionUtil.BLUETOOTH_PERMISSION
import com.example.obusdk.sampleapp.util.PermissionUtil.OBU_DATA_PERMISSION
import com.example.obusdk.sampleapp.util.printLog
import sg.gov.lta.obu.sdk.data.services.OBUSDK


@Composable
fun MainScreen(
    state: String,
    modifier: Modifier = Modifier,
    onAction: (ApiAction) -> Unit
) {
    val context = LocalContext.current

    val obuDataPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                printLog("Pls allow to access OBU Data permission")
            } else {
                printLog("OBU Data Permission Granted!!")
            }
        }
    )

    val bluetoothPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            val isGranted = perms.all {
                it.value
            }
            if (!isGranted) {
                printLog(
                    "Before search/connect OBU, please allow Nearby Devices permission."
                )
            } else {
                printLog("Nearby Devices permission Granted!!")
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 32.dp)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = state,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
        )
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
        ) {
            ApiActionButton(label = "OBU Data Permission") {
                /*
                   Step 02
                   Request OBU Data Permission.
                   The data permission is required from the motorist. This step is necessary before
                   calling the other apis.
                */
                obuDataPermissionResultLauncher.launch(
                    OBU_DATA_PERMISSION
                )
            }

            ApiActionButton(label = "Enable Mock Manager") {
                if (!PermissionUtil.isOBUDataPermissionGranted(context)) {
                    obuDataPermissionResultLauncher.launch(
                        OBU_DATA_PERMISSION
                    )
                    return@ApiActionButton
                }
                onAction(ApiAction.EnableMockData)
            }

            ApiActionButton(label = "Search OBU") {
                if (!OBUSDK.isInitialized) {
                    onAction(ApiAction.InitialiseSDK(context))
                    printLog("Pls initialise SDK before making api calls.")
                    return@ApiActionButton
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                    && !PermissionUtil.isBluetoothPermissionGranted(context)
                ) {
                    bluetoothPermissionResultLauncher.launch(
                        BLUETOOTH_PERMISSION
                    )
                    return@ApiActionButton
                }
                onAction(ApiAction.SearchOBU)
            }

            ApiActionButton(label = "Connect") {
                if (!OBUSDK.isInitialized) {
                    onAction(ApiAction.InitialiseSDK(context))
                    printLog("Pls initialise SDK before making api calls.")
                    return@ApiActionButton
                }
                if (!PermissionUtil.isOBUDataPermissionGranted(context)) {
                    obuDataPermissionResultLauncher.launch(
                        OBU_DATA_PERMISSION
                    )
                    return@ApiActionButton
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                    && !PermissionUtil.isBluetoothPermissionGranted(context)
                ) {
                    bluetoothPermissionResultLauncher.launch(
                        BLUETOOTH_PERMISSION
                    )
                    return@ApiActionButton
                }
                onAction(ApiAction.ConnectOBU)
            }

            ApiActionButton(label = "Disconnect") {
                onAction(ApiAction.Disconnect)
            }
        }
    }
}

@Composable
fun ApiActionButton(
    label: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(4.dp),
            fontSize = 16.sp
        )
    }
}
