package net.ehvazend.builder.performance

import javafx.animation.Interpolator
import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.shape.Rectangle
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.Stage
import net.ehvazend.builder.filesystem.getRoot
import net.ehvazend.builder.performance.AnimationHandler.Effect.toggleDisable
import net.ehvazend.builder.performance.ContentHandler.slideStep

object Data {
    object Config {
        val duration = .75
        val interpolator = Interpolator.SPLINE(1.0, 0.2, 0.2, 1.0)!!
    }

    // Stage from Main
    lateinit var stage: Stage

    // Root
    lateinit var root: Pane
    lateinit var rootRectangle: Rectangle
    lateinit var content: AnchorPane

    // Header
    lateinit var header: Pane
    lateinit var backButton: Button
    lateinit var nextButton: Button

    // Slide's container
    lateinit var body: Pane

    object Init {
        val header: HBox by lazy {
            getRoot<HBox>("/assets/FXML/init/Header.fxml").also {
                it.children.forEach {
                    when {
                        it.id == "createButton" -> createButton = it as Button
                        it.id == "loadButton" -> loadButton = it as Button
                    }
                }

                fun toggleButton(duration: Double) {
                    createButton.toggleDisable(duration)
                    loadButton.toggleDisable(duration)
                }

                createButton.setOnAction {
                    toggleButton(Config.duration)
                    slideStep(create to load, ContentHandler.Side.BOTTOM)
                }

                loadButton.setOnAction {
                    toggleButton(Config.duration)
                    slideStep(load to create, ContentHandler.Side.TOP)
                }
            }
        }

        // Header button
        private lateinit var createButton: Button
        private lateinit var loadButton: Button

        val body: Pane by lazy {
            Pane().also {
                it.children += create
                it.children += load
            }
        }

        val create: Pane by lazy {
            getRoot<VBox>("/assets/FXML/init/Create.fxml").also {
                (it.children.first() as HBox).children.forEach {
                    if (it.id == "chooseDirectory") (it as Button).setOnAction {
                        DirectoryChooser().showDialog(Stage())
                    }
                }
            }
        }

        private val load: VBox by lazy {
            getRoot<VBox>("/assets/FXML/init/Load.fxml").also {
                it.opacity = 0.0
                it.isMouseTransparent = true

                (it.children.first() as HBox).children.forEach {
                    if (it.id == "chooseFile") (it as Button).setOnAction {
                        FileChooser().showOpenDialog(Stage())
                    }
                }
            }

        }
    }
}