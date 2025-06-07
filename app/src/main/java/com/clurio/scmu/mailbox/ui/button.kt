package com.clurio.scmu.mailbox.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clurio.scmu.mailbox.ui.theme.ActiveButtonColor
import com.clurio.scmu.mailbox.ui.theme.ActiveButtonPressedColor

@Composable
fun ToggleButton(
    isActive: Boolean,
    onToggle: () -> Unit,
    textOn: String,
    textOff: String,
    modifier: Modifier = Modifier,
    activeColor: Color = ActiveButtonPressedColor,
    inactiveColor: Color = ActiveButtonColor
) {
    Button(
        onClick = onToggle,
        modifier = modifier
            .fillMaxWidth(0.7f)
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) activeColor else inactiveColor,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(50.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
    ) {
        Text(
            text = if (isActive) textOn else textOff,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}