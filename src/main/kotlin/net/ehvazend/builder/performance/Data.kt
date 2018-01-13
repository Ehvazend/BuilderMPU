package net.ehvazend.builder.performance

import javafx.animation.Interpolator
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

    var currentPanel: Panel? = null

    val panels = arrayListOf<Panel>(Init)
}