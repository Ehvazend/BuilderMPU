package net.ehvazend.builder.animation

import javafx.fxml.FXMLLoader
import net.ehvazend.builder.Main
import java.io.IOException

@Throws(IOException::class)
fun <T> getRoot(address: String): T = FXMLLoader(Main::class.java.getResource(address)).let {
    it.load<T>()
    it.getRoot<T>()
}
