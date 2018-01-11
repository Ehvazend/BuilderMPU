package net.ehvazend.builder.performance.handlers

import javafx.application.Platform
import javafx.scene.Node
import net.ehvazend.builder.performance.Data
import net.ehvazend.builder.performance.handlers.AnimationHandler.Add
import net.ehvazend.builder.performance.handlers.AnimationHandler.Effect.appearance
import net.ehvazend.builder.performance.handlers.AnimationHandler.Effect.disappearance
import net.ehvazend.builder.performance.handlers.AnimationHandler.timeline
import net.ehvazend.builder.performance.interfaces.Slide

object ContentHandler {
    data class Content(val header: Node, val body: Node)

    private fun loadContent(panel: Data.Panels): Content {
        fun loadHeader(panel: Data.Panels) = panel.header.also {
            Data.headerContainer.children += it
        }

        fun loadBody(panel: Data.Panels) = panel.body.also {
            Data.bodyContainer.children += it
        }

        return Content(loadHeader(panel), loadBody(panel)).also { Data.currentPanel = panel }
    }

    fun initContent(panel: Data.Panels) {
        loadContent(panel)
        initMoveBox()
        resizeBackground()
    }

    private fun initMoveBox() {
        fun updateStatus() {
            when {
                Data.currentPanel == null || Data.Panels.values().size == 1 -> {
                    MoveBoxHandler.backButtonEnable = false
                    MoveBoxHandler.nextButtonEnable = false
                }

                Data.Panels.values().indexOf(Data.currentPanel) == 0 -> {
                    MoveBoxHandler.backButtonEnable = false
                    MoveBoxHandler.nextButtonEnable = true
                }

                Data.Panels.values().indexOf(Data.currentPanel) != 0
                        && Data.Panels.values().indexOf(Data.currentPanel) != Data.Panels.values().size - 1 -> {
                    MoveBoxHandler.backButtonEnable = true
                    MoveBoxHandler.nextButtonEnable = true
                }

                Data.Panels.values().indexOf(Data.currentPanel) == Data.Panels.values().size - 1 -> {
                    MoveBoxHandler.backButtonEnable = true
                    MoveBoxHandler.nextButtonEnable = false
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

    // Slide zone -------------------------------
    sealed class Direction(val x: Double = 0.0, val y: Double = 0.0) {
        object TOP : Direction(y = -Data.stage.scene.height)
        object RIGHT : Direction(x = Data.stage.scene.width)
        object BOTTOM : Direction(y = Data.stage.scene.height)
        object LEFT : Direction(x = -Data.stage.scene.width)
    }

    private fun slideStep(slides: Pair<Slide, Slide>, direction: Direction) {
        val (newSlide, oldSlide) = slides

        oldSlide.slide.also {
            it.disappearance()
            it.layoutYProperty().timeline(0.0 to direction.y, Add(interpolator = Data.Config.interpolator))
        }

        newSlide.slide.also {
            it.appearance()
            it.layoutYProperty().timeline(-direction.y to 0.0, Add(interpolator = Data.Config.interpolator))
        }

        newSlide.source.currentSlide(newSlide)
    }

    fun slideNext(slides: Pair<Slide, Slide>) = slideStep(slides, Direction.BOTTOM)
    fun slideBack(slides: Pair<Slide, Slide>) = slideStep(slides, Direction.TOP)

    // Panel zone -------------------------------
    private fun panelStep(panels: Pair<Data.Panels, Data.Panels>, direction: Direction) {
        val (newPanel, oldPanel) = panels

        // Load new objects
        loadContent(newPanel).also {
            it.header.opacity = 0.0
            it.body.opacity = 0.0
        }

        // Header
        oldPanel.header.disappearance(Add(Data.Config.duration / 2.0)).setOnFinished {
            newPanel.header.opacity = 0.0
            newPanel.header.appearance(Add(Data.Config.duration / 2.0))
        }

        // Body
        oldPanel.body.disappearance()
        oldPanel.body.layoutXProperty().timeline(0.0 to direction.x, Add(interpolator = Data.Config.interpolator)).setOnFinished {
            // Delete old objects when they are behind Data.root scene
            Data.bodyContainer.children.remove(oldPanel.body)
            Data.headerContainer.children.remove(oldPanel.header)
        }

        newPanel.body.appearance()
        newPanel.body.layoutXProperty().timeline(-direction.x to 0.0, AnimationHandler.Add(interpolator = Data.Config.interpolator))
    }

    private fun panelBack() {
        panelStep(Data.currentPanel!!.panel.backPanel!! to Data.currentPanel!!, Direction.RIGHT)
    }

    private fun panelNext() {
        panelStep(Data.currentPanel!!.panel.nextPanel!! to Data.currentPanel!!, Direction.LEFT)
    }
}
