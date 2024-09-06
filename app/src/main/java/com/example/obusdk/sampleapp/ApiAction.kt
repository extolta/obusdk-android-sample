package com.example.obusdk.sampleapp

import android.content.Context

sealed class ApiAction {
    data class InitialiseSDK(val context: Context) : ApiAction()
    object EnableMockData : ApiAction()
    object SearchOBU : ApiAction()
    object ConnectOBU : ApiAction()
    object Disconnect : ApiAction()
}
