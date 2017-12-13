package net.ehvazend.builder.performance.root

import net.ehvazend.builder.performance.AnimationHandler
import net.ehvazend.builder.performance.ContentHandler
import net.ehvazend.builder.performance.Data
import net.ehvazend.builder.performance.HeaderHandler
import java.net.URL
import java.util.*

class Controller : Logic() {
    // Late init
    override fun initialize(location: URL, resources: ResourceBundle?) {
        // Invocation override early init
        super.initialize(location, resources)

        // Run
        HeaderHandler.initHeader()
        ContentHandler.initContent()
        AnimationHandler.Effect.contentEffect(Data.content, 2.5)
        AnimationHandler.Effect.backgroundEffect(40.0)
    }
}