package net.ehvazend.builder.animation

import javafx.animation.*
import javafx.animation.Interpolator.*
import javafx.beans.property.Property
import javafx.scene.Node
import javafx.util.Duration

object Effect {
    // Extension Function for Property
    fun <T> Property<T>.timeline(toValue: T, duration: Double, interpolator: Interpolator = LINEAR, cycleCount: Int = 0): Timeline {
        val keyValue = KeyValue(this, toValue, interpolator)
        val keyFrame = KeyFrame(Duration.seconds(duration), keyValue)

        Timeline(keyFrame).let {
            it.cycleCount = cycleCount

            return it.also(Timeline::play)
        }
    }

    fun Node.translate(side: Slider.Side, duration: Double = .75, interpolator: Interpolator = SPLINE(1.0, 0.2, 0.2, 1.0)) {
        TranslateTransition().also {
            it.node = this
            it.duration = Duration.seconds(duration)
            it.interpolator = interpolator

            it.toX = -side.x
            it.toY = -side.y

            it.play()
        }
    }
}