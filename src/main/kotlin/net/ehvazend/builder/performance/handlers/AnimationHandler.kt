package net.ehvazend.builder.performance.handlers

import javafx.animation.Interpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.beans.value.WritableValue
import javafx.scene.Node
import javafx.scene.effect.ColorAdjust
import javafx.util.Duration
import net.ehvazend.builder.performance.Data

object AnimationHandler {
    // Allows smoothly change value
    data class Add(val interpolator: Interpolator? = null, val isAutoReverse: Boolean? = null, val cycleCount: Int? = null)

    fun <T> WritableValue<T>.timeline(values: Pair<T, T>, duration: Double, add: Add? = null) = Timeline().let {
        // Set up KeyFrame and adding in to Timeline
        it.keyFrames += KeyFrame(
                Duration.seconds(duration),
                // Set up KeyValue
                KeyValue(this.also { it.value = values.first }, values.second, add?.interpolator ?: Interpolator.EASE_BOTH)
        )

        // Set up Timeline
        it.isAutoReverse = add?.isAutoReverse ?: false
        it.cycleCount = add?.cycleCount ?: 0

        // Return and play
        it.also(Timeline::play)
    }

    object Effect {
        fun Node.appearance(duration: Double, add: Add? = null)
                = this.opacityProperty().timeline(this.opacity to 1.0, duration, add).also { this.isMouseTransparent = false }

        fun Node.disappearance(duration: Double, add: Add? = null)
                = this.opacityProperty().timeline(this.opacity to 0.0, duration, add).also { this.isMouseTransparent = true }

        fun Node.toggleDisable(duration: Double, add: Add? = null) = when (this.isDisable) {
            true -> this.enable(duration, add)
            false -> this.disable(duration, add)
        }

        fun Node.enable(duration: Double, add: Add? = null) {
            if (this.isDisable) {
                this.opacityProperty().timeline(0.5 to 1.0, duration, add).setOnFinished { this.isDisable = false }
            }
        }

        fun Node.disable(duration: Double, add: Add? = null) {
            if (!this.isDisable) {
                this.isMouseTransparent = true
                this.opacityProperty().timeline(1.0 to 0.5, duration, add).setOnFinished {
                    this.isMouseTransparent = false
                    this.isDisable = true
                }
            }
        }

        fun backgroundEffect(duration: Double): Timeline {
            return (Data.background.effect as ColorAdjust).hueProperty().timeline(0.0 to 1.0, duration, Add(isAutoReverse = true, cycleCount = -1))
        }

        fun contentAppear(duration: Double, add: Add? = null) {
            Data.background.appearance(duration / 1.5, add)
            Data.headerContainer.appearance(duration, add)
            Data.moveBox.appearance(duration, add)
            Data.bodyContainer.appearance(duration, add)
        }
    }

    object InstantEffect {
        fun Node.instantAppearance() = this.also {
            it.opacity = 1.0
            it.isMouseTransparent = false
        }

        fun Node.instantDisappearance() = this.also {
            it.opacity = .0
            it.isMouseTransparent = true
        }
    }
}