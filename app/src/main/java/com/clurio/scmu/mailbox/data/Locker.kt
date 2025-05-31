package com.clurio.scmu.mailbox.data

data class DeviceStatus(
    val led: Led? = null,
    val locked: Boolean = false,
    val packages: Int = 0
)

data class Led(
    val state: Int = 0
)

