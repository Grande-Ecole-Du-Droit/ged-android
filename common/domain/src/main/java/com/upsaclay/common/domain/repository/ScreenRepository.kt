package com.upsaclay.common.domain.repository

import com.upsaclay.common.domain.entity.Route

interface ScreenRepository {
    val currentRoute: Route?

    fun setCurrentRoute(route: Route?)
}