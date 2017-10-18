package net.ehvazend.builder.main

import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.scene.shape.Rectangle

open class Annotation {
    // Root content
    @FXML private var root = Pane()
    @FXML private var rootRectangle = Rectangle()

    //------------------------------

    // Initialize Data
    fun init() {
        Data.root = root
        Data.rootRectangle = rootRectangle
    }

    // For access in MicroController
    object Data {
        // Root content
        lateinit var root: Pane
        lateinit var rootRectangle: Rectangle

        //------------------------------

        // Panels
        lateinit var currentPanel: Node
        lateinit var initPanel: Node
    }
}