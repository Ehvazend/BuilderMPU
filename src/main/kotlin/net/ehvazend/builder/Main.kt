package net.ehvazend.builder

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import net.ehvazend.builder.Main.DataApplication.*

class Main : Application() {
    enum class DataApplication(val get: String) {
        STYLE("/assets/Main.css"),
        LOGO("/assets/Logo.png"),
        TITLE("MPU: Builder")
    }

    override fun start(mainStage: Stage) {
        // Main.fxml load
        FXMLLoader().also {
            // Load resource
            it.location = javaClass.getResource("/assets/Main.fxml")

            // Set scene
            mainStage.scene = Scene(it.load())

            // Scene parameters
            mainStage.isResizable = false
            mainStage.scene.stylesheets += javaClass.getResource(STYLE.get).toExternalForm()
            mainStage.icons += Image(javaClass.getResourceAsStream(LOGO.get))
            mainStage.title = TITLE.get

            // Run
            mainStage.show()
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }
}