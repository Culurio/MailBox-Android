package com.clurio.scmu.mailbox.data


data class MailboxStatus(
    val lastPackageNumber: Int = 0,
    val led: LedStatus = LedStatus(),
    val closed: Boolean = false,
    val packages: Map<String, PackageData> = emptyMap()
)

data class LedStatus(
    val locked: Boolean = false
)

data class PackageData(
    val arrival_time: String = "",
    val packagePresent: Boolean = false,
    val pickup_time: String = ""
)

