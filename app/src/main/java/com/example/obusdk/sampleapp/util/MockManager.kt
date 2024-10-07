package com.example.obusdk.sampleapp.util

import sg.gov.lta.obu.sdk.conn.MockedConnectionHandler
import sg.gov.lta.obu.sdk.conn.mock.ERPEvent
import sg.gov.lta.obu.sdk.conn.mock.MockEvent
import sg.gov.lta.obu.sdk.conn.mock.TdcidEvent

object MockManager {
    val defaultMockManager =
        MockedConnectionHandler.Builder()
            .setSequence(
                mutableListOf(
                    MockEvent(
                        listOf(ERPEvent.PointBasedCharging()
                            .also {
                                // $0.5
                                it.chargeAmount = 50
                            }), TdcidEvent.Template110A()
                    ),
                    MockEvent(
                        listOf(ERPEvent.PointBasedDeductionSuccessful()
                            .also {
                                //$1.00
                                it.chargeAmount = 100
                            }), TdcidEvent.Template110A()
                    ),
                    MockEvent(
                        listOf(ERPEvent.PointBasedCharging()
                            .also {
                                // $0.53
                                it.chargeAmount = 53
                            }), TdcidEvent.Template110A()
                    ),
                    MockEvent(
                        listOf(ERPEvent.PointBasedDeductionFailure()
                            .also {
                                // $0.53
                                it.chargeAmount = 53
                            }), TdcidEvent.Template110A()
                    ),
                )
            )
            .setCyclicMode(false).setCardBalance(1000).setTimeInterval(3000).build() // $10
}
