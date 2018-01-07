package net.ehvazend.builder.performance.handlers

import net.ehvazend.builder.performance.Data
import net.ehvazend.builder.performance.handlers.AnimationHandler.Effect.toggleDisable
import kotlin.properties.Delegates

object MoveBoxHandler {
    var backButtonEnable: Boolean by Delegates.observable(true) { _, oldValue, newValue ->
        when (!oldValue) {
            newValue -> return@observable
            else -> Data.backButton.toggleDisable(Data.Config.duration)
        }
    }

    var nextButtonEnable: Boolean by Delegates.observable(true) { _, oldValue, newValue ->
        when (!oldValue) {
            newValue -> return@observable
            else -> Data.nextButton.toggleDisable(Data.Config.duration)
        }
    }
}