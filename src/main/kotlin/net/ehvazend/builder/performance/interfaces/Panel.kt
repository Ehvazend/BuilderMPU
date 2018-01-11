package net.ehvazend.builder.performance.interfaces

import javafx.scene.Node
import javafx.scene.layout.Pane
import net.ehvazend.builder.performance.Data
import net.ehvazend.builder.performance.handlers.AnimationHandler.InstantEffect.instantDisappearance
import java.util.*

interface Panel {
    val header: Node
    val body: Node
        get() = fillBody()

    val slides: HashMap<String, Slide>
    val defaultSlide: Slide
    var currentSlide: Slide

    val backPanel: Data.Panels?
        get() = autoBackPanel()

    val nextPanel: Data.Panels?
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

    private fun autoBackPanel(): Data.Panels? = Data.Panels.values().let {
        if (it.indexOf(Data.currentPanel) != 0) it[it.indexOf(Data.currentPanel) - 1] else null
    }

    private fun autoNextPanel(): Data.Panels? = Data.Panels.values().let {
        if (it.indexOf(Data.currentPanel) != it.lastIndex) it[it.indexOf(Data.currentPanel) + 1] else null
    }

}