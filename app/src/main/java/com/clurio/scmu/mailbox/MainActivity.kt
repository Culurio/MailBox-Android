package com.clurio.scmu.mailbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.clurio.scmu.mailbox.ui.icons.getLockIcon
import com.clurio.scmu.mailbox.ui.theme.MailBoxTheme
import com.clurio.scmu.mailbox.viewModel.MailBoxViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MailBoxTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { MailBoxTopBar() },
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("Status")
                        }
                        Image(
                            modifier = Modifier
                                .size(256.dp)
                                .align(Alignment.CenterHorizontally),
                            imageVector = getLockIcon(),
                            contentDescription = null,
                        )

                        Button(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = {},
                        ) {
                            Text("Lock")
                        }

                        Button(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = {}
                        ) {
                            Text("Turn On the Buzzer")
                        }

                        DeviceScreen()

                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MailBoxTopBar() {
        CenterAlignedTopAppBar(
            title = {
                Text(text = "Mail Box") // TODO: Move to strings.xml
            },
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = "Menu"
                    )
                }
            },
            actions = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings"
                    )
                }
            }
        )
    }

    @Composable
    fun DeviceScreen(viewModel: MailBoxViewModel = viewModel()) {
        val status = viewModel.status.value

        Text(text = "LED: ${status.led?.state}")
        Text(text = "Locked: ${status.locked}")
        Text(text = "Packages: ${status.packages}")
    }


}