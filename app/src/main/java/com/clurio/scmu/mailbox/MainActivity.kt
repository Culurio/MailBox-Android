package com.clurio.scmu.mailbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.clurio.scmu.mailbox.notifications.FirebasePackageListener
import com.clurio.scmu.mailbox.notifications.NotificationHandler
import com.clurio.scmu.mailbox.notifications.PackageCheckScheduler
import com.clurio.scmu.mailbox.ui.ToggleButton
import com.clurio.scmu.mailbox.ui.icons.getLockIcon
import com.clurio.scmu.mailbox.ui.icons.getUnlockIcon
import com.clurio.scmu.mailbox.ui.theme.MailBoxTheme
import com.clurio.scmu.mailbox.viewModel.MailBoxViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        NotificationHandler.createChannel(this)
        NotificationHandler.requestPermission(this)
        PackageCheckScheduler.schedule(this)

        setContent {
            MailBoxTheme {
                val navController = rememberNavController()
                MailBox(navController)
            }
        }
    }

    @Composable
    fun NavigationGraph(
        navController: NavHostController,
        padding: PaddingValues,
        onScreenChange: (String) -> Unit
    ) {
        NavHost(
            navController = navController,
            startDestination = "MainScreen",
            modifier = Modifier.padding(padding)
        ) {
            composable("MainScreen") {
                onScreenChange("MainScreen")
                MainScreen()
            }
            composable("LogScreen") {
                onScreenChange("LogScreen")
                LogScreen()
            }
        }
    }

    @Composable
    fun MainScreen() {
        val context = LocalContext.current
        val viewModel: MailBoxViewModel = viewModel()

        LaunchedEffect(Unit) {
            FirebasePackageListener(viewModel, context).startListening()
        }

        val status = viewModel.status.value
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(256.dp)
                    .align(Alignment.CenterHorizontally),
                imageVector = if (status.led.locked) getLockIcon() else getUnlockIcon(),
                contentDescription = null,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Status")
            }
            Text(
                text = "You have ${status.lastPackageNumber} packages",
                style = TextStyle(textAlign = TextAlign.Center),
                modifier = Modifier.fillMaxWidth()
            )

            ToggleButton(
                isActive = viewModel.status.value.led.locked,
                onToggle = { viewModel.toggleLocked() },
                textOn = "Unlock",
                textOff = "Lock",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            ToggleButton(
                isActive = viewModel.status.value.buzzer,
                onToggle = { viewModel.turnBuzzer() },
                textOn = "Turn Off the Buzzer",
                textOff = "Turn On the Buzzer",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

        }
    }

    @Composable
    fun MailBox(navController: NavHostController) {
        var topBarTitle by remember { mutableStateOf("Main Screen") }
        var showMenuIcon by remember { mutableStateOf(true) }
        var showSettingsIcon by remember { mutableStateOf(true) }
        var showBackButton by remember { mutableStateOf(false) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                MailBoxTopBar(
                    navController = navController,
                    title = topBarTitle,
                    showMenuIcon = showMenuIcon,
                    showSettingsIcon = showSettingsIcon,
                    showBackButton = showBackButton
                )
            },
        ) { padding ->
            NavigationGraph(
                navController = navController,
                padding = padding,
                onScreenChange = { screenName ->
                    when (screenName) {
                        "MainScreen" -> {
                            topBarTitle = "Main Screen"
                            showMenuIcon = true
                            showSettingsIcon = true
                            showBackButton = false
                        }

                        "LogScreen" -> {
                            topBarTitle = "Log Screen"
                            showMenuIcon = false
                            showSettingsIcon = false
                            showBackButton = true
                        }
                    }
                }
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MailBoxTopBar(
        navController: NavController,
        title: String,
        showMenuIcon: Boolean = true,
        showSettingsIcon: Boolean = true,
        showBackButton: Boolean = false,
    ) {
        CenterAlignedTopAppBar(
            title = { Text(title) },
            navigationIcon = {
                when {
                    showBackButton -> {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }

                    showMenuIcon -> {
                        IconButton(onClick = { navController.navigate("LogScreen") }) {
                            Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Menu")
                        }
                    }
                }
            },
            actions = {
                if (showSettingsIcon) {
                    IconButton(onClick = { /* TODO: Settings action */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            }
        )
    }
}
