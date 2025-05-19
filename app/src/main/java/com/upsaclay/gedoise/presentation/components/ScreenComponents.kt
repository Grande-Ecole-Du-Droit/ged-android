package com.upsaclay.gedoise.presentation.components

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing

@Composable
fun SplashScreen() {
    val localConfiguration = LocalConfiguration.current
    val screenWith = localConfiguration.screenWidthDp.dp

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val animatedValue = infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.spacing.medium),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .width(screenWith)
                .height(160.dp)
                .scale(animatedValue.value),
            painter = painterResource(id = com.upsaclay.common.R.drawable.ged_logo),
            contentDescription = stringResource(id = com.upsaclay.common.R.string.app_name)
        )
    }
}


/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview
@Composable
private fun SplashScreenPreview() {
    GedoiseTheme {
        SplashScreen()
    }
}