package net.ehvazend.builder.main

import javafx.scene.Node
import javafx.scene.layout.VBox
import net.ehvazend.builder.filesystem.getRoot

open class Logic : Annotation() {
    // Set node in root
    fun Node.set(): Node {
        return this.also {
            it.layoutX = -600.0
            Data.root.children += it
        }
    }

    // Move Node in starting place
    fun Node.init(): Node {
        return this.also {
            it.layoutX = 0.0
        }
    }

    fun load() {
        Data.initPanel = getRoot<VBox>("/assets/main/Init.fxml").set()
    }
}