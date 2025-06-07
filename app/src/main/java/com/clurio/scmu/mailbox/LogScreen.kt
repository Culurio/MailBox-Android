package com.clurio.scmu.mailbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.clurio.scmu.mailbox.data.PackageData
import com.clurio.scmu.mailbox.ui.ToggleButton
import com.clurio.scmu.mailbox.util.formatDateForUser
import com.clurio.scmu.mailbox.viewModel.MailBoxViewModel


@Composable
fun LogScreen() {
    val viewModel: MailBoxViewModel = viewModel()
    val status = viewModel.status.value
    val packages = status.packages

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(0.8f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(packages.entries.toList()) { (id, packageData) ->
                PackageCard(packageData) {
                    viewModel.markPackagePickedUp(id)
                }
            }
        }
    }
}


@Composable
fun PackageCard(pkg: PackageData, onPickup: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Arrival: ${formatDateForUser(pkg.arrival_time)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (pkg.packagePresent) "Status: Present" else "Status: Not Present",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (pkg.packagePresent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
                Text(
                    text = "Pickup: ${formatDateForUser(pkg.pickup_time)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (pkg.packagePresent && pkg.pickup_time.isBlank()) {
                ToggleButton(
                    isActive = false,
                    onToggle = onPickup,
                    textOn = "Picked Up",
                    textOff = "Mark as Picked Up",
                    modifier = Modifier
                        .align(Alignment.End)
                        .height(48.dp)
                        .width(180.dp)
                )
            }
        }
    }
}

