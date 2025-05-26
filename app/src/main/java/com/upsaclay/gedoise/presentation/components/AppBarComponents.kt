package com.upsaclay.gedoise.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.upsaclay.common.presentation.TopLevelDestinationRoute
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.gedoise.R
import com.upsaclay.gedoise.presentation.navigation.TopLevelDestination
import kotlin.reflect.KClass

@Composable
fun MainBottomBar(
    onTopLevelDestinationClick: (TopLevelDestinationRoute) -> Unit,
    currentRoute: NavDestination?,
    topLevelDestinations: List<TopLevelDestination>
) {
    var previousDestination: TopLevelDestination? = null

    NavigationBar {
        topLevelDestinations.forEachIndexed { index, destination ->
            val selected = currentRoute
                .isRouteInHierarchy(destination.route)
                .also {
                    if (it) {
                        previousDestination = destination
                    }
                }

            val iconRes = if (selected) destination.filledIcon else destination.outlinedIcon

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (destination.route != previousDestination?.route) {
                        onTopLevelDestinationClick(TopLevelDestinationRoute.entries[index])
                    }
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if (destination.badges > 0) {
                                Badge { Text(text = destination.badges.toString()) }
                            } else if (destination.hasNews) {
                                Badge()
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = stringResource(id = destination.iconDescription)
                        )
                    }
                },
                label = { Text(text = stringResource(id = destination.label)) }
            )
        }
    }
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.parent?.any {
        it.hasRoute(route)
    } ?: false

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun MainTopBarPreview() {
    GedoiseTheme {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = com.upsaclay.common.R.string.app_name),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { },
                    enabled = false
                ) {
                    Image(
                        painter = painterResource(id = com.upsaclay.common.R.drawable.ged_logo),
                        contentDescription = stringResource(id = com.upsaclay.common.R.string.ged_logo_description),
                        contentScale = ContentScale.Fit
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = { },
                    modifier = Modifier.clip(shape = CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = com.upsaclay.common.R.drawable.default_profile_picture),
                        contentDescription = stringResource(id = R.string.profile_icon_description),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        )
    }
}

@Phones
@Composable
private fun MainBottomBarPreview() {
    val navController = rememberNavController()

    val itemList = listOf(
        TopLevelDestination.Home(),
        TopLevelDestination.Message(badges = 5)
    )

    GedoiseTheme {
        MainBottomBar(
            onTopLevelDestinationClick = { },
            currentRoute = navController.currentBackStackEntryAsState().value?.destination,
            topLevelDestinations = itemList
        )
    }
}