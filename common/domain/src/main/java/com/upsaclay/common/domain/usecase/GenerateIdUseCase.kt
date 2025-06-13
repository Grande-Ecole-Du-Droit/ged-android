package com.upsaclay.common.domain.usecase

import java.util.UUID
import kotlin.math.absoluteValue

object GenerateIdUseCase {
    val stringId: String
        get() = UUID.randomUUID().toString()
    val longId: Long
        get() = UUID.randomUUID().mostSignificantBits.absoluteValue
}
