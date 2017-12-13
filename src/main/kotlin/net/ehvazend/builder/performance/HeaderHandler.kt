package net.ehvazend.builder.performance

import javafx.scene.layout.Pane
import net.ehvazend.builder.performance.AnimationHandler.Effect.appearance
import net.ehvazend.builder.performance.AnimationHandler.Effect.disappearance
import net.ehvazend.builder.performance.AnimationHandler.Effect.toggleDisable
import net.ehvazend.builder.performance.ContentHandler.slideStep

object HeaderHandler {
    fun initHeader() {
        Data.header.children += Data.Init.header

        Data.backButton.setOnAction {
            Data.Init.header.appearance(Data.Config.duration)
            Data.backButton.toggleDisable(Data.Config.duration)
            Data.nextButton.toggleDisable(Data.Config.duration)

            slideStep(Data.Init.body to Pane(), ContentHandler.Side.RIGHT)
        }

        Data.nextButton.setOnAction {
            Data.Init.header.disappearance(Data.Config.duration)
            Data.backButton.toggleDisable(Data.Config.duration)
            Data.nextButton.toggleDisable(Data.Config.duration)

            slideStep(Pane() to Data.Init.body, ContentHandler.Side.LEFT)
        }
    }
}