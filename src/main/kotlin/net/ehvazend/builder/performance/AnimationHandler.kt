package net.ehvazend.builder.performance

import javafx.animation.Interpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.beans.value.WritableValue
import javafx.scene.Node
import javafx.scene.effect.ColorAdjust
import javafx.util.Duration

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
        fun Node.appearance(duration: Double, add: Add? = null) = this.opacityProperty().timeline(0.0 to 1.0, duration, add).also { this.isMouseTransparent = false }
        fun Node.disappearance(duration: Double, add: Add? = null) = this.opacityProperty().timeline(1.0 to 0.0, duration, add).also { this.isMouseTransparent = true }
        fun Node.toggleDisable(duration: Double, add: Add? = null) = when {
            this.isDisable -> this.opacityProperty().timeline(0.5 to 1.0, duration, add).setOnFinished { this.isDisable = false }
            else -> {
                this.isMouseTransparent = true
                this.opacityProperty().timeline(1.0 to 0.5, duration, add).setOnFinished {
                    this.isMouseTransparent = false
                    this.isDisable = true
                }
            }
        }

        fun backgroundEffect(duration: Double) = (Data.rootRectangle.effect as ColorAdjust).hueProperty().timeline(0.0 to 1.0, duration, Add(isAutoReverse = true, cycleCount = -1))
        fun contentEffect(node: Node, duration: Double, add: Add? = null) = node.appearance(duration, add)
    }
}