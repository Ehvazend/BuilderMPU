package net.ehvazend.builder.panels

import javafx.application.Platform
import javafx.scene.Node
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import net.ehvazend.graphics.getRoot
import net.ehvazend.graphics.interfaces.Panel
import net.ehvazend.graphics.interfaces.Slide
import java.util.*

object Process : Panel {
    override val id = "Process"

    override val header: Node = HBox()

    override val body: Node by lazy { fillBody() }

    override val slides: HashMap<String, Slide>
        get() = HashMap<String, Slide>().also {
            it["test"] = test
        }

    override val defaultSlide: Slide by lazy { test }
    override lateinit var currentSlide: Slide

    private val test: Slide by lazy {
        object : Slide {
            override val body: Node = getRoot<VBox>("/assets/FXML/init/Create.fxml")

            override val source: Panel = this@Process
        }
    }

    init {
        Platform.runLater {
            when (Init.currentSlide) {
                Init.create -> Unit
                Init.load -> Unit
            }
        }
    }
}