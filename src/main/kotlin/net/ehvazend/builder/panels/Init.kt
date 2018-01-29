package net.ehvazend.builder.panels

import javafx.application.Platform
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
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
import net.ehvazend.graphics.handlers.MoveBoxHandler
import net.ehvazend.graphics.interfaces.Panel
import net.ehvazend.graphics.interfaces.Slide
import java.util.*
import java.util.regex.Pattern

object Init : Panel {
    override val id = "init"

    override val header: HBox by lazy {
        getRoot<HBox>("/assets/FXML/init/Header.fxml").apply {
            id = "headerInit"

            lateinit var createButton: Button
            lateinit var loadButton: Button

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
                ContentHandler.slideNext(create to currentSlide!!)
                when (createHoldOn) {
                    true -> MoveBoxHandler.holdNextButtonOn = MoveBoxHandler.HoldValue(true, false)
                    false -> MoveBoxHandler.holdNextButtonOn = MoveBoxHandler.HoldValue(false, false)
                }
            }

            loadButton.setOnAction {
                toggleButton()
                ContentHandler.slideBack(load to currentSlide!!)
                when (loadTextField.text) {
                    "File not chosen" -> MoveBoxHandler.holdNextButtonOn = MoveBoxHandler.HoldValue(true, false)
                    else -> MoveBoxHandler.holdNextButtonOn = MoveBoxHandler.HoldValue(false, false)
                }
            }
        }
    }

    override val body: Pane by lazy { loadDefaultSlide() }

    override val slides: HashMap<String, Slide>
        get() = HashMap<String, Slide>().also {
            it["create"] = create
            it["load"] = load
        }

    override val defaultSlide: Slide by lazy { create }
    override var currentSlide: Slide? = null

    private val create: Slide by lazy {
        object : Slide {
            override val body = getRoot<VBox>("/assets/FXML/init/Create.fxml").apply {
                (children[0] as HBox).children.forEach {
                    when (it.id) {
                        "nameTextField" -> nameTextField = it as TextField
                        "nameDescriptionTextField" -> nameDescriptionTextField = it as TextField
                    }
                }

                (children[1] as HBox).children.forEach {
                    when (it.id) {
                        "createTextField" -> createTextField = it as TextField
                        "chooseDirectory" -> (it as Button).setOnAction {
                            DirectoryChooser().showDialog(Stage().apply {
                                initOwner(Data.stage)
                                initModality(Modality.WINDOW_MODAL)
                            }.owner).also {
                                if (it != null) {
                                    createTextField.text = it.path
                                    nameTextField.enable()
                                    nameTextField.text = ""
                                    nameDescriptionTextField.enable()
                                        .apply { nameDescriptionTextField.isMouseTransparent = true }
                                }
                            }
                        }
                    }
                }

                nameTextField.apply {
                    textProperty().addListener { _, oldValue, newValue ->
                        if (!Pattern.compile("(\\w+)?").matcher(newValue).matches() || newValue == " " || newValue.length >= 16) {
                            text = oldValue
                            createHoldOn = oldValue == ""
                        } else createHoldOn = newValue == ""
                    }
                }
            }

            lateinit var nameTextField: TextField
            lateinit var nameDescriptionTextField: TextField
            lateinit var createTextField: TextField

            override val source = this@Init
        }
    }

    private var createHoldOn = true
        set(value) {
            MoveBoxHandler.holdNextButtonOn = MoveBoxHandler.HoldValue(value, false)
            field = value
        }

    private val load: Slide by lazy {
        object : Slide {
            override val body = getRoot<HBox>("/assets/FXML/init/Load.fxml").apply {
                children.forEach {
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

                                        if (MoveBoxHandler.holdNextButtonOn.mode) MoveBoxHandler.holdNextButtonOn =
                                                MoveBoxHandler.HoldValue(false, false)
                                    }
                                }
                            }
                        }

                        "loadTextField" -> loadTextField = it as TextField
                    }
                }
            }

            override val source = this@Init
        }
    }

    lateinit var loadTextField: TextField

    init {
        Platform.runLater {
            MoveBoxHandler.holdNextButtonOn = MoveBoxHandler.HoldValue(true, false)
        }
    }
}