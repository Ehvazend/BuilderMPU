package net.ehvazend.builder.filesystem

import javafx.fxml.FXMLLoader
import net.ehvazend.builder.Main

fun <T> getRoot(address: String): T = FXMLLoader(Main::class.java.getResource(address)).let {
    it.load<T>()
    it.getRoot<T>()
}