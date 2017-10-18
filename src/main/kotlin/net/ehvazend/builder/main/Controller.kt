package net.ehvazend.builder.main

import javafx.fxml.Initializable
import javafx.scene.effect.ColorAdjust
import javafx.scene.layout.VBox
import net.ehvazend.builder.animation.Animation.timeline
import net.ehvazend.builder.animation.getRoot
import java.net.URL
import java.util.*

class Controller : Logic(), Initializable {
    override fun initialize(location: URL, resources: ResourceBundle?) {
        // Fill data
        init()

        // Load Init.fxml
        Data.initPanel = getRoot<VBox>("/assets/main/Init.fxml")

        // Set Init.fxml
        Data.root.children += Data.initPanel
        Data.currentPanel = Data.initPanel

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