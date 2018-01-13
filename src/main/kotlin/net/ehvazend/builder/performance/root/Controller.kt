package net.ehvazend.builder.performance.root

import net.ehvazend.builder.performance.Data
import net.ehvazend.builder.performance.handlers.AnimationHandler
import net.ehvazend.builder.performance.handlers.AnimationHandler.Add
import net.ehvazend.builder.performance.handlers.ContentHandler
import java.net.URL
import java.util.*

class Controller : Annotation() {
    // Late init
    override fun initialize(location: URL, resources: ResourceBundle?) {
        // Invocation override early init
        super.initialize(location, resources)

        // Run
        ContentHandler.initContent(Data.panels.first { it.id == "init" })
        AnimationHandler.Effect.contentAppear(Add(2.5))
        AnimationHandler.Effect.backgroundEffect(40.0)
    }
}