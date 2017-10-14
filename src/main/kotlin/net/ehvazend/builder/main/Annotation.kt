package net.ehvazend.builder.main

import javafx.fxml.FXML
import javafx.scene.layout.Pane
import javafx.scene.shape.Rectangle

open class Annotation {
    @FXML
    protected var root = Pane()

    @FXML
    protected var rootRectangle = Rectangle()
}