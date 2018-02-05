package net.ehvazend.builder.panels

import javafx.application.Platform
import javafx.scene.Node
import javafx.scene.control.ProgressBar
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import net.ehvazend.graphics.getRoot
import net.ehvazend.graphics.handlers.AnimationHandler.Effect.enable
import net.ehvazend.graphics.interfaces.Panel
import net.ehvazend.graphics.interfaces.Slide
import java.util.*

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

//    override val setOnLoadPanel = {
//        when (Init.currentSlide) {
//            Init.slides["create"] -> {
////                Slide.unload(currentSlide!!)
////                Slide.load(build)
//            }
//
//            Init.slides["load"] -> {
//                thread {
////                    println(Loader.getPacks(File(Init.loadTextField.text).toURI()))
////                    WebUtils.getMods(startConnection("https://cursemeta.dries007.net/mods.json"))
//                }
//                Unit
////                Slide.unload(currentSlide!!)
////                Slide.load(defaultSlide)
//            }
//        }
//    }

    private object PBHandler {
        private var currentPB = startingProgressBar
        private val listPB: ArrayList<ProgressBar> = arrayListOf(
            startingProgressBar,
            loadingProgressBar,
            processingProgressBar,
            cleaningProgressBar
        )

        fun nextPB() {
            setReady()

            val currentID = listPB.indexOf(currentPB)

            if (currentID < listPB.size -1) {
                currentPB = listPB[currentID + 1]
                setWait()
            } else println("End of progress bars")
        }

        fun setReady() {
            currentPB.progress = 1.0
        }

        fun setWait() {
            currentPB.apply {
                enable()
                progress = ProgressBar.INDETERMINATE_PROGRESS
            }
        }

        fun setError() {
            setReady()
            currentPB.styleClass += "errorGradient"
        }
    }
}