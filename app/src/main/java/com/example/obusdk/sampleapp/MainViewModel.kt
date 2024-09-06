package com.example.obusdk.sampleapp

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.obusdk.sampleapp.util.MockManager
import com.example.obusdk.sampleapp.util.printLog
import sg.gov.lta.obu.sdk.conn.OBU
import sg.gov.lta.obu.sdk.conn.OBUBluetoothListener
import sg.gov.lta.obu.sdk.conn.OBUSearchListener
import sg.gov.lta.obu.sdk.core.enums.BluetoothState
import sg.gov.lta.obu.sdk.core.enums.OBUCardStatus
import sg.gov.lta.obu.sdk.core.enums.OBUChargingPaymentMode
import sg.gov.lta.obu.sdk.core.enums.OBUPaymentMode
import sg.gov.lta.obu.sdk.core.models.OBUAcceleration
import sg.gov.lta.obu.sdk.core.models.OBUChargingInformation
import sg.gov.lta.obu.sdk.core.models.OBUPaymentHistory
import sg.gov.lta.obu.sdk.core.models.OBUTotalTripCharged
import sg.gov.lta.obu.sdk.core.models.OBUTrafficInfo
import sg.gov.lta.obu.sdk.core.models.OBUTravelSummary
import sg.gov.lta.obu.sdk.core.types.OBUError
import sg.gov.lta.obu.sdk.data.interfaces.OBUConnectionStatusListener
import sg.gov.lta.obu.sdk.data.interfaces.OBUDataListener
import sg.gov.lta.obu.sdk.data.interfaces.OBUInitListener
import sg.gov.lta.obu.sdk.data.services.OBUSDK

class MainViewModel : ViewModel() {

    private var obu: OBU? = null

    var state by mutableStateOf("")
        private set

    private val connectionListener = object : OBUConnectionStatusListener {
        override fun onOBUConnected(device: OBU) {
            state = "Connected ${obu?.name}"
            printLog("[onOBUConnected] device=$device")
        }

        override fun onOBUConnectionFailure(error: OBUError) {
            state = "Connection Failed!"
            printLog("[onOBUConnectionFailure] error=$error")
        }

        override fun onOBUDisconnected(device: OBU?, error: OBUError?) {
            state = "Disconnected"
            printLog("[onOBUDisconnected] device=$device error=$error")
        }
    }

    fun onAction(apiAction: ApiAction) {
        when (apiAction) {
            is ApiAction.InitialiseSDK -> initialiseOBUSdk(apiAction.context)
            is ApiAction.EnableMockData -> enableMockData()
            is ApiAction.SearchOBU -> searchOBU()
            is ApiAction.ConnectOBU -> connectOBU()
            is ApiAction.Disconnect -> disconnect()
        }
    }

    /*
       Step 01
       Initialise SDK
       The developer needs to register at https://datamall.lta.gov.sg/content/datamall/en/request-for-api.html to receive the SDK account key of the OBU SDK.
       Initialise the SDK is mandatory before calling any other method.
    */
    private fun initialiseOBUSdk(context: Context) {
        val initListener = object : OBUInitListener {
            override fun onSuccess() {
                printLog("OBUSDK initialise success!!")
                // Uncomment this to listen Bluetooth State change.
                // subscribeBluetoothListener()
                // Uncomment this to listen data from OBU.
                // subscribeObuDataListener()
            }

            override fun onError(error: OBUError) {
                printLog("OBUSDK initialise failed. error=$error")
            }
        }
        // Add your SDK account key
        val sdkKey = "<SDK Account Key>"
        OBUSDK.init(context, sdkKey, initListener)
    }

    /*
       Enable Mock Data
       To use the Mock Manager call the method or else skip it.
    */
    private fun enableMockData() {
        OBUSDK.enableMockData(MockManager.defaultMockManager)
        printLog("Mock data enabled.")
    }

    /*
       Initiates a search for nearby OBUs

       Call `startSearch()` to search for OBUs. This method requires [OBUSearchListener] to listen
       search result.

       The `onObuSelected` callback is triggered when an OBU is selected from the popup displayed
       during the search process. The selected OBU is returned as a parameter. In the case of the
       Mock Manager, onObuSelected will return a mock OBU automatically.
    */
    @SuppressLint("MissingPermission")
    private fun searchOBU() {
        val searchListener = object : OBUSearchListener {
            override fun onObuSelected(obu: OBU) {
                this@MainViewModel.obu = obu
                state = "OBU Selected ${obu.name}"
                printLog("[onObuSelected] obu=$obu")
            }

            override fun onPairing() {
                printLog("[onPairing]")
            }

            override fun onSearchFailure(error: OBUError?) {
                state = "Search Failed!"
                printLog("[onSearchFailure] error=$error")
            }
        }

        printLog("start searching....")
        state = "Searching..."
        OBUSDK.startSearch(searchListener)
    }

    /*
       Connect to OBU
       Once OBU is found, next step is to make connection, SDK provides two connect() methods to
       establish a connection with The OBU.
       a) connect(obu, listener) : Takes OBU which you will get from `onObuSelected` callback.
       b) connect(vehicleNumber, listener) : Uses vehicle number to establish connection. However,
                                            this method requires the OBU to be previously paired
                                            with the device.
    */
    private fun connectOBU() {
        state = "Connecting to ${obu?.name}"
        printLog("request connection")
        obu?.let { obu -> OBUSDK.connect(obu = obu, listener = connectionListener) }
            ?: printLog("Please select obu device.")
    }

    // Disconnect the connection.
    private fun disconnect() {
        OBUSDK.disconnect()
        printLog("Disconnect called.")
    }

    // Subscribe Bluetooth Listener: Listen state change of Bluetooth.
    fun subscribeBluetoothListener() {
        val bluetoothListener = object : OBUBluetoothListener {
            override fun onBluetoothStateUpdated(state: BluetoothState) {
                when (state) {
                    BluetoothState.STATE_ON -> {
                        printLog("Bluetooth Sate On")
                    }

                    BluetoothState.STATE_OFF -> {
                        printLog("Bluetooth Sate Off")
                    }

                    BluetoothState.STATE_TURNING_ON -> {
                        printLog("Bluetooth Sate Turning On")
                    }

                    BluetoothState.STATE_TURNING_OFF -> {
                        printLog("Bluetooth Sate Turning Off")
                    }
                }
            }
        }
        OBUSDK.setBluetoothListener(bluetoothListener)
        printLog("Subscribed to Bluetooth Listener.")
    }

    /*
       Subscribe OBU Data Listener.
       Once connection has been established, [OBUDataListener] receive the data from the OBU.
    */
    fun subscribeObuDataListener() {
        val dataListener = object : OBUDataListener {

            override fun onChargingInformation(chargingInfo: List<OBUChargingInformation>) {
                printLog("[onChargingInformation] chargingInfo=$chargingInfo")
            }

            override fun onVelocityInformation(velocity: Double) {
                printLog("[onVelocityInformation] velocity=$velocity")
            }

            override fun onAccelerationInformation(acceleration: OBUAcceleration) {
                printLog("[onAccelerationInformation] acceleration=$acceleration")
            }

            override fun onCardInformation(
                status: OBUCardStatus?,
                paymentMode: OBUPaymentMode?,
                chargingPaymentMode: OBUChargingPaymentMode?,
                balance: Int?
            ) {
                printLog(
                    "[onCardInformation] status=$status paymentMode=$paymentMode " +
                            "chargingPaymentMode=$chargingPaymentMode balance=$balance"
                )
            }

            override fun onPaymentHistories(histories: List<OBUPaymentHistory>) {
                printLog("[onPaymentHistories] histories=$histories")
            }

            override fun onTrafficInformation(trafficInfo: OBUTrafficInfo<out Any>) {
                printLog("[onTrafficInformation] trafficInfo=$trafficInfo")
            }

            override fun onError(error: OBUError) {
                printLog("[onError] error=$error")
            }

            override fun onErpChargingAndTrafficInfo(
                trafficInfo: OBUTrafficInfo<out Any>?,
                chargingInfo: List<OBUChargingInformation>?
            ) {
                printLog(
                    "[onErpChargingAndTrafficInfo] trafficInfo=$trafficInfo " +
                            "chargingInfo=$chargingInfo"
                )
            }

            override fun onTotalTripCharged(totalCharged: OBUTotalTripCharged) {
                printLog("[onTotalTripCharged] totalCharged=$totalCharged")
            }

            override fun onTravelSummary(travelSummary: OBUTravelSummary) {
                printLog("[onTravelSummary] travelSummary=$travelSummary")
            }
        }
        OBUSDK.setDataListener(dataListener)
        printLog("Subscribed to OBU Data Listener.")
    }

    ////////////////////////////////  Helper Functions  //////////////////////////////////
    // Call this method to get the last card balance.
    fun getLastCardBalance() {
        val lastCardBalance = OBUSDK.getCardBalance()
        printLog("lastCardBalance=$lastCardBalance")
    }

    // Call this method to get the payment mode.
    fun getPaymentMode() {
        val paymentMode = OBUSDK.getPaymentMode()
        printLog("paymentMode=$paymentMode")
    }

    // Call this method to get the card status.
    fun getCardStatus() {
        val cardStatus = OBUSDK.getCardStatus()
        printLog("cardStatus=$cardStatus")
    }

    // Call this method to get the payment histories.
    fun getPaymentHistories() {
        val paymentHistories = OBUSDK.getPaymentHistories()
        printLog("paymentHistories=$paymentHistories")
    }

    // Call this method to get the last travel summary.
    fun getLastTravelSummary() {
        val lastTravelSummary = OBUSDK.getLastTravelSummary()
        printLog("lastTravelSummary=$lastTravelSummary")
    }

    // Call this method to get the last traffic info data.
    fun getLastTrafficInfo() {
        val lastTrafficInfo = OBUSDK.getLastTrafficInfo()
        printLog("lastTrafficInfo: $lastTrafficInfo")
        printLog("lastTrafficInfo: dataList=${lastTrafficInfo?.dataList}")
        printLog("lastTrafficInfo: priority=${lastTrafficInfo?.priority}")
        printLog("lastTrafficInfo: templateId=${lastTrafficInfo?.templateId}")
    }

    // Call this method to get the last OBU data.
    fun getLastObuData() {
        val lastObuData = OBUSDK.getLastObuData()
        printLog("lastObuData=$lastObuData")
    }

}