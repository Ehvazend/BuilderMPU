package net.ehvazend.builder.performance.handlers

import javafx.application.Platform
import javafx.scene.Node
import net.ehvazend.builder.performance.Data
import net.ehvazend.builder.performance.handlers.AnimationHandler.Effect.appearance
import net.ehvazend.builder.performance.handlers.AnimationHandler.Effect.disable
import net.ehvazend.builder.performance.handlers.AnimationHandler.Effect.disappearance
import net.ehvazend.builder.performance.handlers.AnimationHandler.Effect.enable
import net.ehvazend.builder.performance.handlers.AnimationHandler.timeline
import net.ehvazend.builder.performance.interfaces.Slide

object ContentHandler {
    data class Content(val header: Node, val body: Node) {}

    fun loadContent(panel: Data.Panels): Content {
        fun loadHeader(panel: Data.Panels) = panel.header.also {
            Data.headerContainer.children += it
        }

        fun loadBody(panel: Data.Panels) = panel.body.also {
            Data.bodyContainer.children += it
        }

        return Content(loadHeader(panel), loadBody(panel)).also { Data.currentPanel = panel }
    }

    fun initPanel(panel: Data.Panels) {
        loadContent(panel)

        resizeBackground()
        initMoveBox()
    }

    private fun resizeBackground() {
        when {
            Data.root.height != 0.0 -> Data.background.height = Data.root.height
            Data.root.height == 0.0 -> {
                Platform.runLater {
                    Data.background.height = Data.root.height
                }
            }
        }
    }

    private fun initMoveBox() {
        fun updateStatus() {
            when {
                Data.currentPanel == null || Data.Panels.values().size == 1 -> {
                    Data.backButton.disable(Data.Config.duration)
                    Data.nextButton.disable(Data.Config.duration)
                }

                Data.Panels.values().indexOf(Data.currentPanel) == 0 -> {
                    Data.backButton.disable(Data.Config.duration)
                    Data.nextButton.enable(Data.Config.duration)
                }

                Data.Panels.values().indexOf(Data.currentPanel) != 0 && Data.Panels.values().indexOf(Data.currentPanel) != Data.Panels.values().size - 1 -> {
                    Data.backButton.enable(Data.Config.duration)
                    Data.nextButton.enable(Data.Config.duration)
                }

                Data.Panels.values().indexOf(Data.currentPanel) == Data.Panels.values().size - 1 -> {
                    Data.backButton.enable(Data.Config.duration)
                    Data.nextButton.disable(Data.Config.duration)
                }
            }
        }

        updateStatus()

        Data.backButton.setOnAction {
            ContentHandler.panelBack()
            updateStatus()
        }

        Data.nextButton.setOnAction {
            ContentHandler.panelNext()
            updateStatus()
        }
    }

    // Slide zone ------------------------------
    enum class Direction(val x: Double = 0.0, val y: Double = 0.0) {
        TOP(y = -Data.stage.scene.height),
        RIGHT(x = Data.stage.scene.width),
        BOTTOM(y = Data.stage.scene.height),
        LEFT(x = -Data.stage.scene.width)
    }

    private fun slideStep(slides: Pair<Slide, Slide>, direction: Direction) {
        val (newSlide, oldSlide) = slides

        oldSlide.body.also {
            it.disappearance(Data.Config.duration)
            it.layoutYProperty().timeline(0.0 to direction.y, Data.Config.duration, AnimationHandler.Add(Data.Config.interpolator))
        }

        newSlide.body.also {
            it.appearance(Data.Config.duration)
            it.layoutYProperty().timeline(-direction.y to 0.0, Data.Config.duration, AnimationHandler.Add(Data.Config.interpolator))
        }
    }

    fun slideNext(slides: Pair<Slide, Slide>) = slideStep(slides, ContentHandler.Direction.BOTTOM)
    fun slideBack(slides: Pair<Slide, Slide>) = slideStep(slides, ContentHandler.Direction.TOP)


    private fun panelStep(panels: Pair<Data.Panels, Data.Panels>, direction: Direction) {
        val (newPanel, oldPanel) = panels

        // Load new objects
        loadContent(newPanel).also {
            it.header.opacity = 0.0
            it.body.opacity = 0.0
        }

        // Header
        oldPanel.header.disappearance(Data.Config.duration / 2.0).setOnFinished {
            newPanel.header.opacity = 0.0
            newPanel.header.appearance(Data.Config.duration / 2.0)
        }

        // Body
        oldPanel.body.disappearance(Data.Config.duration)
        oldPanel.body.layoutXProperty().timeline(0.0 to direction.x, Data.Config.duration, AnimationHandler.Add(Data.Config.interpolator)).setOnFinished {
            // Delete old objects when they are behind Data.root scene
            Data.bodyContainer.children.remove(oldPanel.body)
            Data.headerContainer.children.remove(oldPanel.header)
        }

        newPanel.body.appearance(Data.Config.duration)
        newPanel.body.layoutXProperty().timeline(-direction.x to 0.0, Data.Config.duration, AnimationHandler.Add(Data.Config.interpolator))
    }

    fun panelNext() {
        Data.Panels.values().also {
            if (it.indexOf(Data.currentPanel) != it.lastIndex) {
                panelStep(it[it.indexOf(Data.currentPanel) + 1] to Data.currentPanel!!, Direction.LEFT)
            }
        }
    }

    fun panelBack() {
        Data.Panels.values().also {
            if (it.indexOf(Data.currentPanel) != 0) {
                panelStep(it[it.indexOf(Data.currentPanel) - 1] to Data.currentPanel!!, Direction.RIGHT)
            }
        }
    }
}
