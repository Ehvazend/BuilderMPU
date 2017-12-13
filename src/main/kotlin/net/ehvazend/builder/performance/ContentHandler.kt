package net.ehvazend.builder.performance

import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import net.ehvazend.builder.performance.AnimationHandler.Effect.appearance
import net.ehvazend.builder.performance.AnimationHandler.Effect.disappearance
import net.ehvazend.builder.performance.AnimationHandler.timeline

object ContentHandler {
    fun initContent() {
        Data.body.children += Data.Init.body
        resizeWindow(Data.Init.create)
    }

    fun resizeWindow(region: Region) {
        val sizeHeader = 25.0
        val padding = 14.0

        Data.rootRectangle.height = (sizeHeader + region.prefHeight + (padding * 2)) + 22
    }


    // Slide zone ------------------------------
    enum class Side(val x: Double = 0.0, val y: Double = 0.0) {
        TOP(y = -Data.stage.scene.height),
        RIGHT(x = Data.stage.scene.width),
        BOTTOM(y = Data.stage.scene.height),
        LEFT(x = -Data.stage.scene.width)
    }

    fun slideStep(slides: Pair<Pane, Pane>, side: Side) {
        val (newSlide, oldSlide) = slides

        oldSlide.also {
            it.disappearance(Data.Config.duration)
            it.layoutXProperty().timeline(0.0 to side.x, Data.Config.duration, AnimationHandler.Add(Data.Config.interpolator))
            it.layoutYProperty().timeline(0.0 to side.y, Data.Config.duration, AnimationHandler.Add(Data.Config.interpolator))
        }

        newSlide.also {
            it.appearance(Data.Config.duration)
            it.layoutXProperty().timeline(-side.x to 0.0, Data.Config.duration, AnimationHandler.Add(Data.Config.interpolator))
            it.layoutYProperty().timeline(-side.y to 0.0, Data.Config.duration, AnimationHandler.Add(Data.Config.interpolator))
        }
    }

    fun panelStep(slides: Pair<Pane, Pane>, side: Side) {
        val (newSlide, oldSlide) = slides

        oldSlide.also {
            it.disappearance(Data.Config.duration)
            it.translateXProperty().timeline(0.0 to side.x, Data.Config.duration, AnimationHandler.Add(Data.Config.interpolator))
            it.translateYProperty().timeline(0.0 to side.y, Data.Config.duration, AnimationHandler.Add(Data.Config.interpolator))
        }

        newSlide.also {
            it.appearance(Data.Config.duration)
            it.translateXProperty().timeline(-side.x to 0.0, Data.Config.duration, AnimationHandler.Add(Data.Config.interpolator))
            it.translateYProperty().timeline(-side.y to 0.0, Data.Config.duration, AnimationHandler.Add(Data.Config.interpolator))
        }
    }
}
