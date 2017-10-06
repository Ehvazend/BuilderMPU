package net.ehvazend.builder

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class Main : Application() {
    override fun start(mainStage: Stage) {
        // Constants
        val root = FXMLLoader()
        val title = "MPU: Builder"

        // Load resource
        root.location = javaClass.getResource("/assets/Main.fxml")

        // Set scene
        mainStage.scene = Scene(root.load(), 590.0, 240.0)

        // Scene parameters
        mainStage.isResizable = false
        mainStage.title = title

        mainStage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }
}