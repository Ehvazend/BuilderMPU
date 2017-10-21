package net.ehvazend.builder.animation

import javafx.scene.Node
import net.ehvazend.builder.animation.Animation.translate

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