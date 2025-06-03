package com.clurio.scmu.mailbox.notifications

import android.content.Context
import com.clurio.scmu.mailbox.viewModel.MailBoxViewModel
import com.clurio.scmu.mailbox.util.formatDateForUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebasePackageListener(
    private val viewModel: MailBoxViewModel,
    private val context: Context
) {
    private val lockerRef: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("/lastPackageNumber")

    private val prefs = context.getSharedPreferences("package_prefs", Context.MODE_PRIVATE)
    private val PREF_KEY_LAST_COUNT = "last_package_number"

    private var lastCount: Int
        get() = prefs.getInt(PREF_KEY_LAST_COUNT, 0)
        set(value) = prefs.edit().putInt(PREF_KEY_LAST_COUNT, value).apply()

    fun startListening() {
        lockerRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val current = snapshot.getValue(Int::class.java) ?: return

                val lastPackage = viewModel.status.value.packages.values.lastOrNull()

                val dateStr = lastPackage?.arrival_time

                val dateTime = formatDateForUser(dateStr)
                val arrivalTimeText = dateTime.toString()

                if (current > lastCount) {
                    lastCount = current
                    viewModel.onNewPackage?.invoke(current)
                    NotificationHandler.showNotification(
                        context,
                        "New package received! Total: $current\nReceived at $arrivalTimeText"
                    )
                } else {
                    lastCount = current
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Log error if needed
            }
        })
    }
}

