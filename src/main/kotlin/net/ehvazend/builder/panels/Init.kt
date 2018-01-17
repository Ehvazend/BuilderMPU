package net.ehvazend.builder.panels

import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage
import net.ehvazend.graphics.Data
import net.ehvazend.graphics.getRoot
import net.ehvazend.graphics.handlers.AnimationHandler.Effect.enable
import net.ehvazend.graphics.handlers.AnimationHandler.Effect.toggleDisable
import net.ehvazend.graphics.handlers.ContentHandler
import net.ehvazend.graphics.interfaces.Panel
import net.ehvazend.graphics.interfaces.Slide
import java.util.*

object Init : Panel {
    override val id = "init"

    override val header: HBox by lazy {
        getRoot<HBox>("/assets/FXML/init/Header.fxml").apply {
            id = "headerInit"

            children.forEach {
                when (it.id) {
                    "createButton" -> createButton = it as Button
                    "loadButton" -> loadButton = it as Button
                }
            }

            fun toggleButton() {
                createButton.toggleDisable()
                loadButton.toggleDisable()
            }

            createButton.setOnAction {
                toggleButton()
                ContentHandler.slideNext(create to currentSlide)
            }

            loadButton.setOnAction {
                toggleButton()
                ContentHandler.slideBack(load to currentSlide)
            }
        }
    }

    // Header button
    private lateinit var createButton: Button
    private lateinit var loadButton: Button

    override val body: Node by lazy {
        fillBody()
    }

    override val slides: HashMap<String, Slide>
        get() = HashMap<String, Slide>().also {
            it["create"] = create
            it["load"] = load
        }

    override val defaultSlide: Slide by lazy { create }
    override lateinit var currentSlide: Slide

    private val create: Slide by lazy {
        object : Slide {
            override val body = getRoot<VBox>("/assets/FXML/init/Create.fxml").apply {
                (children.first() as HBox).children.forEach {
                    when (it.id) {
                        "chooseDirectory" -> (it as Button).setOnAction {
                            DirectoryChooser().showDialog(Stage().apply {
                                initOwner(Data.stage)
                                initModality(Modality.WINDOW_MODAL)
                            }.owner).also {
                                if (it != null) {
                                    createTextField.text = it.path
                                    createTextField.enable()
                                }
                            }
                        }

                        "createTextField" -> createTextField = it as TextField
                    }
                }
            }

            override val source = this@Init

            lateinit var createTextField: TextField
        }
    }

    private val load: Slide by lazy {
        object : Slide {
            override val body = getRoot<VBox>("/assets/FXML/init/Load.fxml").apply {
                (children.first() as HBox).children.forEach {
                    when (it.id) {
                        "chooseFile" -> (it as Button).setOnAction {
                            FileChooser().apply {
                                extensionFilters.add(FileChooser.ExtensionFilter("MPU config files", "*.conf"))
                                showOpenDialog(Stage().apply {
                                    initOwner(Data.stage)
                                    initModality(Modality.WINDOW_MODAL)
                                }.owner).also {
                                    if (it != null) {
                                        loadTextField.text = it.path
                                        loadTextField.enable()
                                    }
                                }
                            }
                        }
                        "loadTextField" -> loadTextField = it as TextField
                    }
                }
            }

            override val source = this@Init

            lateinit var loadTextField: TextField
        }
    }
}