package com.example.obusdk.sampleapp.util

import sg.gov.lta.obu.sdk.conn.MockedConnectionHandler
import sg.gov.lta.obu.sdk.conn.mock.ERPEvent
import sg.gov.lta.obu.sdk.conn.mock.MockEvent
import sg.gov.lta.obu.sdk.conn.mock.TdcidEvent

object MockManager {
    val defaultMockManager =
        MockedConnectionHandler.Builder()
            .setSequence(
                // ERP Point Based
                mutableListOf(
                    MockEvent(
                        listOf(
                            ERPEvent.AlertPointMessage.Mock.erpPointBased(chargingAmount = 100)
                        ), TdcidEvent.Template110A()
                    ),
                    MockEvent(
                        listOf(
                            ERPEvent.ChargingMessage.Mock.erpPointBased(chargingAmount = 100)
                        ), TdcidEvent.Template110A()
                    ),
                    MockEvent(
                        listOf(
                            ERPEvent.DeductionSuccessfulMessage.Mock.erpPointBased(chargingAmount = 100)
                        ), TdcidEvent.Template110A()
                    ),
                    // EPS charging and deduction
                    MockEvent(
                        listOf(
                            ERPEvent.ChargingMessage.Mock.eps()
                        ), TdcidEvent.Template110A()
                    ),
                    MockEvent(
                        listOf(
                            ERPEvent.DeductionFailureMessage.Mock.eps()
                        ), TdcidEvent.Template110A()
                    ),
                    // OPC alert and charging
                    MockEvent(
                        listOf(
                            ERPEvent.AlertPointMessage.Mock.opcSingleArea()
                        ), null
                    ),
                    MockEvent(
                        listOf(
                            ERPEvent.ChargingMessage.Mock.opc()
                        ), TdcidEvent.Template110A()
                    ),
                    MockEvent(
                        listOf(
                            ERPEvent.CommonAlertMessage.Mock.erp()
                        ), null
                    ),
                    MockEvent(
                        listOf(
                            ERPEvent.CarParkMessage.Mock.eepInformation()
                        ), null
                    ),
                    MockEvent(
                        listOf(
                            ERPEvent.CarParkMessage.Mock.ownerMessage()
                        ), null
                    ),
                    MockEvent(
                        listOf(
                            ERPEvent.SchemeUpdateMessage.Mock.opc()
                        ), null
                    ),
                    MockEvent(
                        listOf(
                            ERPEvent.MigrationMessage.Mock.cpt()
                        ), null
                    ),
                    MockEvent(
                        listOf(
                            ERPEvent.SeasonParkingTicketMessage.Mock.rep()
                        ), null
                    ),
                    MockEvent(
                        listOf(
                            ERPEvent.OffenceAlertMessage.Mock.offenceAlert()
                        ), null
                    ),
                    MockEvent(
                        listOf(
                            ERPEvent.ParkingStatusMessage.Mock.parkingStatus()
                        ), null
                    ),
                )
            )
            .setCyclicMode(false).setCardBalance(1000).setTimeInterval(3).build() // $10
}
