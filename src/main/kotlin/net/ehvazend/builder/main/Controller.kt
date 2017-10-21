package net.ehvazend.builder.main

import javafx.fxml.Initializable
import javafx.scene.effect.ColorAdjust
import net.ehvazend.builder.animation.Animation.timeline
import java.net.URL
import java.util.*

class Controller : Logic(), Initializable {
    override fun initialize(location: URL, resources: ResourceBundle?) {
        // Fill Data
        init()
        load()

        // Set Init.fxml
        Data.currentPanel = Data.initPanel.init()

        // Set effect
        Data.initPanel.also {
            // Effect
            it.opacityProperty().set(0.0)
            it.opacityProperty().timeline(1.0, 1.5)
        }

        // Set ColorAdjust effect on Rectangle in Root panel (Main.fxml)
        (Data.rootRectangle.effect as ColorAdjust).hueProperty().timeline(1.0, 40.0, cycleCount = -1)
    }
}