package com.upsaclay.gedoise.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.upsaclay.common.R
import com.upsaclay.message.presentation.conversation.ConversationRoute
import com.upsaclay.news.presentation.NewsRoute
import kotlin.reflect.KClass

sealed class TopLevelDestination(
    open val badges: Int,
    open val hasNews: Boolean
) {
    abstract val route: KClass<*>
    abstract val label: Int
    abstract val filledIcon: Int
    abstract val outlinedIcon: Int
    abstract val iconDescription: Int

    data class Home(
        override val badges: Int = 0,
        override val hasNews: Boolean = false
    ): TopLevelDestination(badges, hasNews) {
        override val route = NewsRoute::class
        @StringRes override val label: Int = R.string.home
        @DrawableRes override val filledIcon: Int = R.drawable.ic_fill_home
        @DrawableRes override val outlinedIcon: Int = R.drawable.ic_outline_home
        @StringRes override val iconDescription: Int = R.string.home_icon_description
    }

    data class Message(
        override val badges: Int = 0,
        override val hasNews: Boolean = false
    ): TopLevelDestination(badges, hasNews) {
        override val route = ConversationRoute::class
        @StringRes override val label: Int = R.string.messages
        @DrawableRes override val filledIcon: Int = R.drawable.ic_fill_message
        @DrawableRes override val outlinedIcon: Int = R.drawable.ic_outline_message
        @StringRes override val iconDescription: Int = R.string.message_icon_description
    }

    data class Calendar(
        override val badges: Int = 0,
        override val hasNews: Boolean = false
    ): TopLevelDestination(badges, hasNews) {
        override val route = ConversationRoute::class
        @StringRes override val label: Int = R.string.calendar
        @DrawableRes override val filledIcon: Int = R.drawable.ic_fill_calendar
        @DrawableRes override val outlinedIcon: Int = R.drawable.ic_outline_calendar
        @StringRes override val iconDescription: Int = R.string.calendar_icon_description
    }

    data class Forum(
        override val badges: Int = 0,
        override val hasNews: Boolean = false
    ): TopLevelDestination(badges, hasNews) {
        override val route = ConversationRoute::class
        @StringRes override val label: Int = R.string.forum
        @DrawableRes override val filledIcon: Int = R.drawable.ic_fill_forum
        @DrawableRes override val outlinedIcon: Int = R.drawable.ic_outline_forum
        @StringRes override val iconDescription: Int = R.string.forum_icon_description
    }
}
