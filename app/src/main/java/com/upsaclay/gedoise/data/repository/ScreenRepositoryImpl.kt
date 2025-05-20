package com.upsaclay.gedoise.data.repository

import com.upsaclay.gedoise.domain.repository.ScreenRepository

internal class ScreenRepositoryImpl: ScreenRepository {
    private var _currentRoute: Any? = null
    override val currentRoute: Any? get() = _currentRoute

    override fun setCurrentScreenRoute(route: Any?) {
        _currentRoute = route
    }
}