package net.ehvazend.builder.animation

import javafx.animation.*
import javafx.animation.Interpolator.*
import javafx.beans.property.Property
import javafx.scene.Node
import javafx.util.Duration

object Effect {
    object Slider {
        enum class Side(val y: Double, val x: Double) {
            TOP(x = 0.0, y = -250.0),
            RIGHT(x = 600.0, y = 0.0),
            BOTTOM(x = 0.0, y = 250.0),
            LEFT(x = -600.0, y = 0.0)
        }

        fun stepPanel(currentNode: Node, newNode: Node, side: Side) {
            // Start moving currentNode
            currentNode.translate(side)

            // Set coordinate
            newNode.relocate(side.x, side.y)

            // Start moving newNode
            newNode.translate(side)
        }
    }

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