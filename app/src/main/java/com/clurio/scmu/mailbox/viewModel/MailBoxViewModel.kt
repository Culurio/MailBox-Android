package com.clurio.scmu.mailbox.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.clurio.scmu.mailbox.data.MailboxStatus
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class MailBoxViewModel : ViewModel() {

    private val database = Firebase.database(
        url = "https://mailbox-movel-default-rtdb.europe-west1.firebasedatabase.app"
    )

    private val mailboxRef = database.getReference("/")

    var onNewPackage: ((Int) -> Unit)? = null

    var status = mutableStateOf(MailboxStatus())
        private set

    init {
        signInWithEmail("","")
        getDeviceStatus()
    }

    private fun getDeviceStatus() {
        mailboxRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(MailboxStatus::class.java)?.let { newStatus ->
                    if (newStatus.lastPackageNumber > status.value.lastPackageNumber) {
                        onNewPackage?.invoke(newStatus.lastPackageNumber)
                    }
                    status.value = newStatus
                    Log.d("Firebase", "Mailbox status updated: $newStatus")
                } ?: run {
                    Log.w("Firebase", "Received null MailboxStatus")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to listen for mailbox status", error.toException())
            }
        })
    }

    fun toggleLocked() {
        val lockedRef = database.getReference("/led/locked")
        lockedRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val currentLocked = currentData.getValue(Boolean::class.java) == true
                currentData.value = !currentLocked
                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                if (error != null) {
                    Log.e("Firebase", "Failed to toggle locked state", error.toException())
                } else {
                    val updatedLocked = currentData?.getValue(Boolean::class.java) == true
                    status.value = status.value.copy(closed = updatedLocked)
                    Log.d("Firebase", "Locked state toggled to $updatedLocked")
                }
            }
        })
    }

    private fun signInWithEmail(email: String, password: String) {
        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("Auth", "Signed in as: ${user?.email}")
                } else {
                    Log.e("Auth", "Email sign-in failed", task.exception)
                }
            }
    }
}
