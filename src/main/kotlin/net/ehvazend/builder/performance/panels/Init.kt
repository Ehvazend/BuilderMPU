package net.ehvazend.builder.performance.panels

import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.Stage
import net.ehvazend.builder.filesystem.getRoot
import net.ehvazend.builder.performance.Data
import net.ehvazend.builder.performance.handlers.AnimationHandler.Effect.toggleDisable
import net.ehvazend.builder.performance.handlers.ContentHandler
import net.ehvazend.builder.performance.interfaces.Panel
import net.ehvazend.builder.performance.interfaces.Slide

object Init : Panel {
    override val header: HBox by lazy {
        getRoot<HBox>("/assets/FXML/init/Header.fxml").also {
            it.id = "headerInit"

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
                toggleButton(Data.Config.duration)
                ContentHandler.slideNext(create to load)
            }

            loadButton.setOnAction {
                toggleButton(Data.Config.duration)
                ContentHandler.slideBack(load to create)
            }
        }
    }

    // Header button
    private lateinit var createButton: Button
    private lateinit var loadButton: Button

    override val body: Pane by lazy {
        Pane().also {
            it.id = "bodyInit"

            it.children += create.body
            it.children += load.body
        }
    }

    override lateinit var currentSlide: Slide

    private val create: Slide by lazy {
        object : Slide {
            override val body = getRoot<VBox>("/assets/FXML/init/Create.fxml").also {
                (it.children.first() as HBox).children.forEach {
                    if (it.id == "chooseDirectory") (it as Button).setOnAction {
                        DirectoryChooser().showDialog(Stage())
                    }
                }
            }
        }
    }

    private val load: Slide by lazy {
        object : Slide {
            override val body = getRoot<VBox>("/assets/FXML/init/Load.fxml").also {
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