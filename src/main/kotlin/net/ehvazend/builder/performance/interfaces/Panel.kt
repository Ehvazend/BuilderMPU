package net.ehvazend.builder.performance.interfaces

import javafx.scene.Node
import javafx.scene.layout.Pane

interface Panel {
    val header: Node
    val body: Node

    fun fillBody(vararg slides: Slide) = Pane().also { pane ->
        slides.forEach { pane.children += it.slide }
    }
}