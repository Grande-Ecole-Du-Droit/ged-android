package com.upsaclay.common.domain.repository

import com.upsaclay.common.domain.entity.Route

interface RouteRepository {
    val currentRoute: Route?

    fun setCurrentRoute(route: Route?)
}