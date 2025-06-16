package com.upsaclay.gedoise.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.upsaclay.common.R
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.message.presentation.conversation.ConversationRoute
import com.upsaclay.news.presentation.NewsRoute

sealed class TopLevelDestination(
    open val badges: Int,
    open val hasNews: Boolean
) {
    abstract val route: Route
    abstract val label: Int
    abstract val filledIcon: Int
    abstract val outlinedIcon: Int
    abstract val iconDescription: Int

    data class Home(
        override val badges: Int = 0,
        override val hasNews: Boolean = false
    ): TopLevelDestination(badges, hasNews) {
        override val route = NewsRoute
        @StringRes override val label: Int = R.string.home
        @DrawableRes override val filledIcon: Int = R.drawable.ic_fill_home
        @DrawableRes override val outlinedIcon: Int = R.drawable.ic_outline_home
        @StringRes override val iconDescription: Int = R.string.home_icon_description
    }

    data class Message(
        override val badges: Int = 0,
        override val hasNews: Boolean = false
    ): TopLevelDestination(badges, hasNews) {
        override val route = ConversationRoute
        @StringRes override val label: Int = R.string.messages
        @DrawableRes override val filledIcon: Int = R.drawable.ic_fill_message
        @DrawableRes override val outlinedIcon: Int = R.drawable.ic_outline_message
        @StringRes override val iconDescription: Int = R.string.message_icon_description
    }
}

enum class TopLevelDestinationRoute {
    HOME,
    MESSAGE
}