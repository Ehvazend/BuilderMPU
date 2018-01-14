package net.ehvazend.builder

import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import net.ehvazend.builder.Main.DataApplication.*
import net.ehvazend.builder.panels.Init
import net.ehvazend.graphics.Data
import net.ehvazend.graphics.MPU
import net.ehvazend.graphics.handlers.ContentHandler

class Main : MPU() {
    enum class DataApplication(val get: String) {
        STYLE("/assets/Main.css"),
        LOGO("/assets/Logo.png"),
        TITLE("MPU: Builder")
    }

    override fun start(mainStage: Stage) {
        // Save stage in Data
        initStage(mainStage)

        // Load all panels from project to Data.panels
        loadPanels(Init)

        // Set scene
        mainStage.scene = Scene(Data.root)

        // Scene parameters
        mainStage.also {
            it.isResizable = false
            it.scene.stylesheets += javaClass.getResource(STYLE.get).toExternalForm()
            it.icons += Image(javaClass.getResourceAsStream(LOGO.get))
            it.title = TITLE.get
        }

        ContentHandler.initContent(Data.panels.first { it.id == "init" })

        // Launching
        mainStage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }
}