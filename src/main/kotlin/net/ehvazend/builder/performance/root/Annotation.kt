package net.ehvazend.builder.performance.root

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.shape.Rectangle
import net.ehvazend.builder.performance.Data
import java.net.URL
import java.util.*

open class Annotation : Initializable {
    // Early init
    override fun initialize(location: URL, resources: ResourceBundle?) {
        Data.root = this.root
        Data.background = this.background
        Data.headerContainer = this.headerContainer
        Data.moveBox = this.moveBox
        Data.backButton = this.backButton
        Data.nextButton = this.nextButton
        Data.bodyContainer = this.bodyContainer
    }

    // Root content
    @FXML private var root = Pane()
    @FXML private var background = Rectangle()
    @FXML private var headerContainer = Pane()
    @FXML private var moveBox = HBox()
    @FXML private var backButton = Button()
    @FXML private var nextButton = Button()
    @FXML private var bodyContainer = Pane()
}