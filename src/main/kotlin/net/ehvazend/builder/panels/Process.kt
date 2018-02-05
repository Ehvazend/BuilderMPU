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
                        "cleaning" -> cleaningProgressBar = progressBar
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
    lateinit var cleaningProgressBar: ProgressBar

    private val data: Slide by lazy {
        object : Slide {
            override val body: Pane = getRoot<VBox>("/assets/FXML/process/Data.fxml")

            override val source: Panel = this@Process
        }
    }

    init {
        ProgressBar()
    }

    override val setOnLoadPanel = {
        when (Init.currentSlide) {
            Init.slides["create"] -> {
//                Slide.unload(currentSlide!!)
//                Slide.load(build)
            }

            Init.slides["load"] -> {
                thread {
                    if (afterLoad()) PBHandler.nextPB() else PBHandler.setError()

//                    WebUtils.getMods(startConnection("https://cursemeta.dries007.net/mods.json"))
                }
                Unit
//                Slide.unload(currentSlide!!)
//                Slide.load(defaultSlide)
            }
        }
    }

    fun afterLoad(): Boolean {
        PBHandler.setFirst()

        try {
            Loader.getPacks(File(Init.loadTextField.text).toURI())
        } catch (e: Exception) {
            return false
        }

        return true
    }

    private object PBHandler {
        private var currentPB = startingProgressBar
            set(value) = Platform.runLater { field = value }

        private val listPB: ArrayList<ProgressBar> = arrayListOf(
            startingProgressBar,
            loadingProgressBar,
            processingProgressBar,
            cleaningProgressBar
        )

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

        fun setFirst() {
            listPB.forEach {
                Platform.runLater {
                    it.isDisable = true
                    it.progress = 0.0
                }
            }

            currentPB = listPB.first()
            setWait()
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