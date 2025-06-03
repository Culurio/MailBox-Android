package com.clurio.scmu.mailbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
            items(packages.values.toList()) { packageData ->
                PackageCard(packageData)
            }
        }
    }
}

@Composable
fun PackageCard(pkg: PackageData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Arrival: ${pkg.arrival_time}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = if (pkg.packagePresent) "Status: Present" else "Status: Not Present",
                style = MaterialTheme.typography.bodySmall,
                color = if (pkg.packagePresent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
            Text(text = "Pickup: ${pkg.pickup_time}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}