package com.anson.darkmodeplus.view.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anson.darkmodeplus.R
import com.anson.darkmodeplus.view.ui.Color
import com.anson.darkmodeplus.view.ui.typo
import com.anson.darkmodeplus.view.viewmodel.MainViewModel
import kotlinx.coroutines.delay

@Composable
fun Content(viewModel: MainViewModel, contentPadding: PaddingValues) {
    val overlayEnabled by viewModel.overlayEnabled.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .background(Color().background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(
                text = if (!overlayEnabled) stringResource(id = R.string.push) else stringResource(id = R.string.adjust),
                fontSize = 48.sp,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(56.dp))
            Button(
                modifier = Modifier
                    .height(80.dp)
                    .width(230.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (overlayEnabled) Color().red else Color().primary,
                    contentColor = Color().onPrimary
                ),
                onClick = { viewModel.toggleOverlay() }
            ) {
                Text(
                    text = if (overlayEnabled) stringResource(id = R.string.off) else stringResource(id = R.string.on),
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typo.bodyLarge
                )
            }
            AnimatedVisibility(overlayEnabled) {
                OverlayControls(viewModel = viewModel)
            }
            AnimatedVisibility(overlayEnabled) {
                MemorySection(viewModel = viewModel)
            }
        }
        Box(
            contentAlignment = Alignment.BottomEnd, modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            Text(
                stringResource(id = R.string.warning),
                color = Color().warning,
                style = MaterialTheme.typo.bodyLarge
            )
        }
    }
}

@Composable
fun OverlayControls(viewModel: MainViewModel) {
    val overlayLv by viewModel.overlayLv.collectAsState()
    val enabled by viewModel.overlayEnabled.collectAsState()

    Column(modifier = Modifier.padding(top = 42.dp)) {
        Slider(
            value = overlayLv.toFloat(),
            onValueChange = { viewModel.updateOverlayLevel(it.toInt()) },
            valueRange = 0f..230f,
            steps = 230,
            enabled = enabled,
            modifier = Modifier.width(400.dp),
            colors = SliderDefaults.colors(
                thumbColor = Color().warning,
                activeTrackColor = Color().tertiary,
                inactiveTrackColor = Color().tertiary.copy(alpha = 0.5f),
                activeTickColor = Color().onPrimary,
                disabledThumbColor = Color().background,
                disabledActiveTrackColor = Color().background,
                disabledActiveTickColor = Color().background,
                disabledInactiveTickColor = Color().background,
                disabledInactiveTrackColor = Color().background
            ),
            interactionSource = remember { MutableInteractionSource() }
        )
    }
}

@Composable
fun MemorySection(viewModel: MainViewModel) {
    val enabled by viewModel.overlayEnabled.collectAsState()
    Column(modifier = Modifier.padding(top = 42.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.save_configurations),
            style = MaterialTheme.typo.titleLarge,
            color = if (enabled) Color().onBackground else Color().background
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            MemoryButton(
                enabled = enabled,
                text = stringResource(id = R.string.memory, 1),
                onClick = { viewModel.selectMemory(1) },
                onLongClick = { viewModel.saveMemory(1) }
            )
            Spacer(modifier = Modifier.width(16.dp))
            MemoryButton(
                enabled = enabled,
                text = stringResource(id = R.string.memory, 2),
                onClick = { viewModel.selectMemory(2) },
                onLongClick = { viewModel.saveMemory(2) }
            )
            Spacer(modifier = Modifier.width(16.dp))
            MemoryButton(
                enabled = enabled,
                text = stringResource(id = R.string.memory, 3),
                onClick = { viewModel.selectMemory(3) },
                onLongClick = { viewModel.saveMemory(3) }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.long_click),
            style = MaterialTheme.typo.bodyMedium,
            color = if (enabled) Color().onBackground else Color().background
        )
    }
}

@Composable
fun MemoryButton(enabled: Boolean, text: String, onClick: () -> Unit, onLongClick: () -> Unit) {
    var buttonPushed by remember { mutableStateOf(false) }
    val backgroundColor = if (buttonPushed) Color().primaryPushed else Color().primary
    if (buttonPushed) {
        LaunchedEffect(Unit) {
            delay(3000)
            buttonPushed = false
        }
    }
    Box(
        modifier = Modifier
            .height(60.dp)
            .width(180.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onClick()
                    },
                    onLongPress = {
                        onLongClick()
                    },
                    onPress = {
                        buttonPushed = true
                        this.awaitRelease()
                        buttonPushed = false
                    },
                )
            }
            .border(
                width = 2.dp,
                color = if (enabled) Color().tertiary else Color().background,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = if (enabled) backgroundColor else Color().background,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typo.bodyMedium,
            color = if (enabled) Color().onPrimary else Color().background
        )
    }
}
