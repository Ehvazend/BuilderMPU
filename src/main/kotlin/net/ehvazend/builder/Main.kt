package net.ehvazend.builder

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import net.ehvazend.builder.Main.DataApplication.*
import net.ehvazend.builder.performance.Data

class Main : Application() {
    enum class DataApplication(val get: String) {
        STYLE("/assets/Main.css"),
        LOGO("/assets/Logo.png"),
        TITLE("MPU: Builder")
    }

    fun initStage(stage: Stage) {
        Data.stage = stage
    }

    override fun start(mainStage: Stage) {
        // Save stage in Data
        initStage(mainStage)

        // Main.fxml load
        FXMLLoader().also {
            // Load resource
            it.location = javaClass.getResource("/assets/FXML/main/Main.fxml")

            // Set scene
            mainStage.scene = Scene(it.load())

            // Scene parameters
            mainStage.also {
                it.isResizable = false
                it.scene.stylesheets += javaClass.getResource(STYLE.get).toExternalForm()
                it.icons += Image(javaClass.getResourceAsStream(LOGO.get))
                it.title = TITLE.get
            }

            // Launching
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