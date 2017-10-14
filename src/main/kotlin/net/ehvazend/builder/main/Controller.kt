package net.ehvazend.builder.main

import javafx.fxml.Initializable
import javafx.scene.effect.ColorAdjust
import net.ehvazend.builder.animation.Animation.timeline
import java.net.URL
import java.util.*
import kotlin.concurrent.thread

class Controller : Logic(), Initializable {
    override fun initialize(location: URL, resources: ResourceBundle?) {
        thread {
            (rootRectangle.effect as ColorAdjust).hueProperty().timeline<Number>(1.0, 40.0, -1)
        }
    }
}