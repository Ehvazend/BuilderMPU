package net.ehvazend.builder.main

import javafx.animation.Interpolator
import javafx.fxml.Initializable
import javafx.scene.effect.ColorAdjust
import javafx.scene.layout.VBox
import net.ehvazend.builder.animation.Animation.timeline
import net.ehvazend.builder.animation.getRoot
import java.net.URL
import java.util.*
import kotlin.concurrent.thread

class Controller : Logic(), Initializable {
    override fun initialize(location: URL, resources: ResourceBundle?) {
        // Load Init.fxml
        root.children += getRoot<VBox>("/assets/main/Init.fxml").also { it ->
            it.opacityProperty().set(0.0)
            it.opacityProperty().timeline(1.0, 1.5, interpolator = Interpolator.LINEAR)
        }

        thread {
            // Start ColorAdjust effect on Rectangle in Root panel (Main.fxml)
            (rootRectangle.effect as ColorAdjust).hueProperty().timeline(1.0, 40.0, -1)
        }
    }
}