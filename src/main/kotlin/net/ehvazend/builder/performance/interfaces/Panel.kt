package net.ehvazend.builder.performance.interfaces

import javafx.scene.Node
import javafx.scene.layout.Pane
import net.ehvazend.builder.performance.handlers.AnimationHandler.InstantEffect.instantDisappearance
import java.util.*

interface Panel {
    val header: Node
    val body: Node
        get() = fillBody()

    val slides: HashMap<String, Slide>
    val defaultSlide: Slide
    var currentSlide: Slide

    private fun fillBody() = Pane().also { pane ->
        slides.forEach { key, value ->
            pane.children += value.slide.also { it.id = key }
            if (value != defaultSlide) value.slide.instantDisappearance()
        }
    }
}