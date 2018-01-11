package net.ehvazend.builder.performance

import javafx.animation.Interpolator
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.shape.Rectangle
import javafx.stage.Stage
import net.ehvazend.builder.performance.interfaces.Panel
import net.ehvazend.builder.performance.panels.Init

object Data {
    object Config {
        val duration = .75
        val interpolator = Interpolator.SPLINE(1.0, .2, .2, 1.0)!!
    }

    // Stage
    lateinit var stage: Stage

    // Root
    lateinit var root: Pane
    lateinit var background: Rectangle

    //-----------------------------------
    // Header
    lateinit var headerContainer: Pane

    // Move box container
    lateinit var moveBox: HBox
    lateinit var backButton: Button
    lateinit var nextButton: Button

    //-----------------------------------
    // Panels's container
    lateinit var bodyContainer: Pane

    // Panels
    enum class Panels(val panel: Panel) {
        INIT(Init);
//        TEST(O());
//
//        class O : Panel {
//            override val header: Node = getRoot<HBox>("/assets/FXML/init/Header.fxml").also { it.id = "headerTest" }
//            override val body: Node
//                get() = super.body
//
//            override val slides: HashMap<String, Slide>
//                get() = HashMap<String, Slide>().also {
//                    it["create"] = create
//                }
//
//            override val defaultSlide: Slide by lazy { create }
//            override lateinit var currentSlide: Slide
//
//            private val create: Slide by lazy {
//                object : Slide {
//                    override val source: Panel
//                        get() = this@O
//                    override val slide = getRoot<javafx.scene.layout.VBox>("/assets/FXML/init/Create.fxml")
//                }
//            }
//        }

        val header: Node = panel.header
        val body: Node = panel.body
    }

    var currentPanel: Panels? = null
}