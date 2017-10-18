package net.ehvazend.builder.animation

import javafx.animation.Interpolator
import javafx.animation.Interpolator.LINEAR
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.beans.property.Property
import javafx.util.Duration

object Animation {
    // Extension Function for Property
    fun <T> Property<T>.timeline(toValue: T, duration: Double, interpolator: Interpolator = LINEAR, cycleCount: Int = 0): Timeline {
        val keyValue = KeyValue(this, toValue, interpolator)
        val keyFrame = KeyFrame(Duration.seconds(duration), keyValue)

        Timeline(keyFrame).let {
            it.cycleCount = cycleCount

            return it.also(Timeline::play)
        }
    }
}