package net.ehvazend.builder.performance.root

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.shape.Rectangle
import net.ehvazend.builder.performance.Data
import java.net.URL
import java.util.*

open class Annotation : Initializable {
    // Early init
    override fun initialize(location: URL, resources: ResourceBundle?) {
        Data.root = this.root
        Data.rootRectangle = this.rootRectangle
        Data.content = this.content
        Data.header = this.header
        Data.backButton = this.backButton
        Data.nextButton = this.nextButton
        Data.body = this.body
    }

    // Root content
    @FXML private var root = Pane()
    @FXML private var rootRectangle = Rectangle()
    @FXML private var content = AnchorPane()
    @FXML private var header = Pane()
    @FXML private var backButton = Button()
    @FXML private var nextButton = Button()
    @FXML private var body = Pane()
}