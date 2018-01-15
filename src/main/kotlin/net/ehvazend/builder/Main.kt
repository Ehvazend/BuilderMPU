package net.ehvazend.builder

import javafx.scene.image.Image
import javafx.stage.Stage
import net.ehvazend.builder.Main.DataApplication.*
import net.ehvazend.builder.panels.Init
import net.ehvazend.graphics.Data
import net.ehvazend.graphics.InitializationMPU

class Main : InitializationMPU(2.5, 40.0) {
    enum class DataApplication(val get: String) {
        STYLE("/assets/Main.css"),
        LOGO("/assets/Logo.png"),
        TITLE("MPU: Builder")
    }

    override fun start(mainStage: Stage) {
        // Load all panels from project to Data.panels
        loadPanels(Init)

        // Start with Init panel
        initPanel(Data.panels.first { it.id == "init" })

        // Save stage in Data and set scene
        initStage(mainStage)

        // Scene parameters
        mainStage.apply {
            isResizable = false
            scene.stylesheets += Main::class.java.getResource(STYLE.get).toExternalForm()
            icons += Image(Main::class.java.getResourceAsStream(LOGO.get))
            title = TITLE.get
        }
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