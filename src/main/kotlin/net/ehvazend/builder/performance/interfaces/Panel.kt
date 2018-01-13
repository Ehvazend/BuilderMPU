package net.ehvazend.builder.performance.interfaces

import javafx.scene.Node
import javafx.scene.layout.Pane
import net.ehvazend.builder.performance.Data
import net.ehvazend.builder.performance.handlers.AnimationHandler.InstantEffect.instantDisappearance
import java.util.*

interface Panel {
    val id: String

    val header: Node
    val body: Node
        get() = fillBody()

    val slides: HashMap<String, Slide>
    val defaultSlide: Slide
    var currentSlide: Slide

    val backPanel: Panel
        get() = autoBackPanel()

    val nextPanel: Panel
        get() = autoNextPanel()

    private fun fillBody() = Pane().also { pane ->
        slides.forEach { key, value ->
            pane.children += value.slide.also { it.id = key }
            if (value != defaultSlide) value.slide.instantDisappearance() else currentSlide = defaultSlide
        }
    }

    fun currentSlide(value: Slide) {
        currentSlide = value
    }

    private fun autoBackPanel() = Data.panels.let { panels ->
        panels.indexOf(this).let {
            if (it != 0) panels[it - 1] else throw NullPointerException()
        }
    }

    private fun autoNextPanel() = Data.panels.let { panels ->
        panels.indexOf(this).let {
            if (it != panels.lastIndex) panels[it + 1] else throw NullPointerException()
        }
    }
}