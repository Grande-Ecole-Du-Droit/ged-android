package com.upsaclay.common.domain.usecase

import java.util.UUID
import kotlin.math.absoluteValue

object GenerateRandomIdUseCase {
    val stringId: String
        get() = UUID.randomUUID().toString()
    val intId: Int
        get() = UUID.randomUUID().mostSignificantBits.toInt().absoluteValue
}
