package net.ehvazend.builder.panels

import javafx.application.Platform
import javafx.scene.Node
import javafx.scene.control.ProgressBar
import javafx.scene.control.ProgressBar.INDETERMINATE_PROGRESS
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import net.ehvazend.builder.Loader
import net.ehvazend.builder.WebUtils
import net.ehvazend.builder.WebUtils.filterMods
import net.ehvazend.builder.WebUtils.getMods
import net.ehvazend.builder.WebUtils.startConnection
import net.ehvazend.graphics.getRoot
import net.ehvazend.graphics.handlers.AnimationHandler.Effect.enable
import net.ehvazend.graphics.interfaces.Panel
import net.ehvazend.graphics.interfaces.Slide
import java.io.File
import java.util.*
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
                        "starting" -> startingProgressBar = progressBar
                        "loading" -> loadingProgressBar = progressBar
                        "processing" -> processingProgressBar = progressBar
                        "filtering" -> filteringProgressBar = progressBar
                    }
                }

                (children[0] as HBox).children.forEach { setProgressBar(it) }
            }
        }

        override val source: Panel = this@Process
    }


    lateinit var startingProgressBar: ProgressBar
    lateinit var loadingProgressBar: ProgressBar
    lateinit var processingProgressBar: ProgressBar
    lateinit var filteringProgressBar: ProgressBar

    private val data: Slide by lazy {
        object : Slide {
            override val body: Pane = getRoot<VBox>("/assets/FXML/process/Data.fxml")

            override val source: Panel = this@Process
        }
    }

    init {
        ProgressBar()
    }

    override val setOnLoadPanel: () -> Unit = {
        when (Init.currentSlide) {
            Init.slides["create"] -> {
                thread {
                    if (!afterCreate()) PBHandler.setError() else {
                        PBHandler.nextPB()
                        loadBD()
                    }
                }
            }

            Init.slides["load"] -> {
                thread {
                    if (!afterLoad()) PBHandler.setError() else {
                        PBHandler.nextPB()
                        loadBD()
                    }
                }
            }
        }
    }

    fun afterCreate(): Boolean {
        PBHandler.refresh()
        return true
    }

    fun afterLoad(): Boolean {
        PBHandler.refresh()

        try {
            Loader.getPacks(File(Init.loadTextField.text).toURI())
        } catch (e: Exception) {
            return false
        }

        return true
    }

    fun loadBD() {
        val connection: String by lazy { startConnection("https://cursemeta.dries007.net/mods.json") }
        val getMods: ArrayList<WebUtils.PrimaryModData> by lazy { getMods(connection) }
        val listMods: List<WebUtils.PrimaryModData> by lazy { filterMods(getMods) }

        if (connection != "") PBHandler.nextPB() else PBHandler.setError()
        if (getMods.isNotEmpty()) PBHandler.nextPB() else PBHandler.setError()
        if (listMods.isNotEmpty()) PBHandler.nextPB() else PBHandler.setError()
    }

    private object PBHandler {
        private val listPB: ArrayList<ProgressBar> = arrayListOf(
            startingProgressBar,
            loadingProgressBar,
            processingProgressBar,
            filteringProgressBar
        )

        private var currentPB = listPB[0]
            set(value) = Platform.runLater { field = value }

        fun refresh() {
            listPB.forEach {
                Platform.runLater {
                    it.isDisable = true
                    it.progress = 0.0
                }
            }

            currentPB = listPB[0]
            setWait()
        }

        fun nextPB() {
            setReady()

            Platform.runLater {
                val currentID = listPB.indexOf(currentPB)

                if (currentID < listPB.size - 1) {
                    currentPB = listPB[currentID + 1]
                    setWait()
                }
            }
        }

        fun setReady() {
            Platform.runLater { currentPB.progress = 1.0 }
        }

        fun setWait() {
            Platform.runLater {
                currentPB.enable()
                currentPB.progress = INDETERMINATE_PROGRESS
            }
        }

        fun setError() {
            setReady()
            Platform.runLater { currentPB.styleClass += "error" }
        }
    }
}