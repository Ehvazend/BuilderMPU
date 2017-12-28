package net.ehvazend.builder.performance.interfaces

import javafx.scene.Node

interface Panel : Slide {
    val header: Node
    var currentSlide: Slide
}