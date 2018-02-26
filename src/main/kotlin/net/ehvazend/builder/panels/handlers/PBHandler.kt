package net.ehvazend.builder.panels.handlers

import javafx.application.Platform
import javafx.scene.control.ProgressBar
import javafx.scene.control.ProgressIndicator.INDETERMINATE_PROGRESS
import net.ehvazend.builder.panels.handlers.PBHandler.State.*
import net.ehvazend.graphics.handlers.AnimationHandler.Effect.forceDisable
import net.ehvazend.graphics.handlers.AnimationHandler.Effect.forceEnable

object PBHandler {
    enum class State {
        INACTIVE,
        WAIT,
        READY,
        ERROR
    }

    private data class ProgressBarData(val progressBar: ProgressBar, private val primaryState: State = INACTIVE) {
        var state = primaryState
            set(value) {
                when (value) {
                    INACTIVE -> Platform.runLater {
                        progressBar.forceDisable()
                        progressBar.progress = 0.0
                        progressBar.styleClass -= "errorGradient"
                    }

                    WAIT -> Platform.runLater {
                        progressBar.forceEnable()
                        progressBar.progress = INDETERMINATE_PROGRESS
                    }

                    READY -> Platform.runLater {
                        progressBar.progress = 1.0
                    }

                    ERROR -> Platform.runLater {
                        progressBar.progress = 1.0
                        progressBar.styleClass += "errorGradient"
                    }
                }

                field = value
            }
    }


    fun addPBList(progressBar: ProgressBar) {
        listPB += ProgressBarData(progressBar)
    }

    private val listPB = ArrayList<ProgressBarData>()

    private var currentPB: ProgressBarData? = null

    fun refresh() {
        listPB.forEach {
            it.state = INACTIVE
        }

        currentPB = listPB[0].apply { state = WAIT }
    }

    fun updateState(state: State) {
        fun setState() {
            currentPB!!.state = state
        }

        when (state) {
            INACTIVE -> setState()
            WAIT -> setState()
            READY -> with(listPB.indexOf(currentPB)) {
                if (this <= listPB.size - 1) {
                    setState()

                    // Increment current Progress Bar
                    if (this != listPB.size - 1) {
                        currentPB = listPB[listPB.indexOf(currentPB!!) + 1]
                        updateState(WAIT)
                    }
                }
            }

            ERROR -> setState()
        }
    }
}
