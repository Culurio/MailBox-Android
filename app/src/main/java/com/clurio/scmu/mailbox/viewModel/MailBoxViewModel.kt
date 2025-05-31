package com.clurio.scmu.mailbox.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.clurio.scmu.mailbox.data.DeviceStatus
import com.google.firebase.Firebase
import com.google.firebase.database.database

class MailBoxViewModel : ViewModel() {

    private val database = Firebase.database(
        url = "https://mailbox-movel-default-rtdb.europe-west1.firebasedatabase.app"
    )

    var status = mutableStateOf(DeviceStatus())
        private set

    init {
        getDeviceStatus()
    }

    private fun getDeviceStatus() {
        database.getReference("/")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.getValue(DeviceStatus::class.java)?.let {
                        status.value = it
                    }
                } else {
                    Log.e("Firebase", "Failed to load data", task.exception)
                }
            }
    }
}
