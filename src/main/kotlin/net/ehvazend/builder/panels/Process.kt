package net.ehvazend.builder.panels

import javafx.application.Platform
import javafx.scene.Node
import javafx.scene.control.ProgressBar
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import net.ehvazend.builder.Loader
import net.ehvazend.builder.WebUtils.PrimaryModData
import net.ehvazend.builder.WebUtils.filterMods
import net.ehvazend.builder.WebUtils.getPrimaryModData
import net.ehvazend.builder.WebUtils.startConnection
import net.ehvazend.builder.panels.handlers.PBHandler
import net.ehvazend.builder.panels.handlers.PBHandler.State.ERROR
import net.ehvazend.builder.panels.handlers.PBHandler.State.READY
import net.ehvazend.graphics.getRoot
import net.ehvazend.graphics.interfaces.Panel
import net.ehvazend.graphics.interfaces.Slide
import java.io.File
import java.nio.file.Files
import kotlin.concurrent.thread

object Process : Panel {
    override val id = "Process"

    override val header: HBox by lazy {
        getRoot<HBox>("/assets/FXML/process/Header.fxml")
    }

    override val body: Pane by lazy { loadDefaultSlide() }

    override val slides: HashMap<String, Slide>
        get() = HashMap<String, Slide>().also {
            it["load"] = load
            it["data"] = data
        }

    override val defaultSlide: Slide by lazy { load }
    override var currentSlide: Slide? = null

    private val load: Slide = object : Slide {
        override val body: Pane = getRoot<AnchorPane>("/assets/FXML/process/Load.fxml").apply {
            Platform.runLater {
                fun setProgressBar(value: Node) {
                    value as AnchorPane
                    val progressBar = value.children[0] as ProgressBar

                    when (progressBar.id) {
                        "starting" -> PBHandler.addPBList(progressBar)
                        "loading" -> PBHandler.addPBList(progressBar)
                        "processing" -> PBHandler.addPBList(progressBar)
                        "filtering" -> PBHandler.addPBList(progressBar)
                    }
                }

                (children[0] as HBox).children.forEach { setProgressBar(it) }
                PBText = (children[1] as Text)
            }
        }

        override val source: Panel = this@Process
    }

    lateinit var PBText: Text

    private val data: Slide by lazy {
        object : Slide {
            override val body: Pane = getRoot<VBox>("/assets/FXML/process/Data.fxml")

            override val source: Panel = this@Process
        }
    }

    override val setOnLoadPanel: () -> Unit = {
        startLoad()
    }

    fun startLoad() {
        // Wrapper for set Progress Bar text
        fun setText(value: String) {
            PBText.text = value
        }

        // Create or load configuration file, if successfully return true
        fun afterCreate(): Boolean {
            val directory = File(Init.createTextField.text)
            val file = File(directory, Init.nameTextField.text + ".conf")

            return try {
                when {
                    directory.isDirectory && !file.exists() -> {
                        try {
                            Files.createFile(file.toPath())
                        } catch (e: Exception) {
                            throw RuntimeException()
                        }
                        true
                    }
                    else -> false
                }
            } catch (e: Exception) {
                false
            }
        }

        fun afterLoad(): Boolean {
            try {
                Loader.getPacks(File(Init.loadTextField.text).toURI())
            } catch (e: Exception) {
                return false
            }

            return true
        }

        fun loadBD() {
            fun connectionBD(adress: String): Pair<Boolean, String?> {
                lateinit var connection: String
                return try {
                    connection = startConnection(adress)
                    if (connection.isEmpty()) return Pair(false, null) else Pair(true, connection)
                } catch (e: Exception) {
                    return Pair(false, null)
                }
            }

            fun parsingDB(connection: String): Pair<Boolean, ArrayList<PrimaryModData>?> {
                lateinit var parsingDB: ArrayList<PrimaryModData>
                return try {
                    parsingDB = getPrimaryModData(connection)
                    if (parsingDB.isEmpty()) return Pair(false, null) else Pair(true, parsingDB)
                } catch (e: Exception) {
                    return Pair(false, null)
                }
            }

            fun filteringDB(primaryModData: ArrayList<PrimaryModData>): Pair<Boolean, List<PrimaryModData>?> {
                lateinit var filteringDB: List<PrimaryModData>
                return try {
                    filteringDB = filterMods(primaryModData)
                    if (filteringDB.isEmpty()) return Pair(false, null) else Pair(true, filteringDB)
                } catch (e: Exception) {
                    return Pair(false, null)
                }
            }

            // Logic
            // Step 2, 3 and 4
            setText("Connecting to BD")
            val connectionBD = connectionBD("https://cursemeta.dries007.net/mods.json")
            if (!connectionBD.first) {
                PBHandler.updateState(ERROR)
                setText("Error while connection to DB")
            } else {
                PBHandler.updateState(READY)
                setText("Parsing BD")
                val parsingDB = parsingDB(connectionBD.second!!)
                if (!parsingDB.first) {
                    PBHandler.updateState(ERROR)
                    setText("Error while parsing DB")
                } else {
                    PBHandler.updateState(READY)
                    setText("Filtering BD")
                    val filteringDB = filteringDB(parsingDB.second!!)
                    if (!filteringDB.first) {
                        PBHandler.updateState(ERROR)
                        setText("Error while filtering DB")
                    } else {
                        PBHandler.updateState(READY)
                        setText("Successful")
                    }
                }
            }
        }

        // Logic
        // Refresh state
        PBHandler.refresh()
        setText("")

        // Get current slide in Init
        when (Init.currentSlide) {
            Init.slides["create"] -> {
                thread {
                    setText("Creating configuration file")

                    if (!afterCreate()) {
                        PBHandler.updateState(ERROR)
                        setText("Error while creating file")
                    } else {
                        PBHandler.updateState(READY)
                        loadBD()
                    }
                }
            }

            Init.slides["load"] -> {
                thread {
                    setText("Reading configuration file")

                    if (!afterLoad()) {
                        PBHandler.updateState(ERROR)
                        setText("Error while reading file")
                    } else {
                        PBHandler.updateState(READY)
                        loadBD()
                    }
                }
            }
        }
    }
}