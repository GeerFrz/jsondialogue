package com.geert.jsondialogue

import com.geert.jsondialogue.controllers.MainController
import javafx.application.Application
import javafx.stage.Stage


class Main : Application() {
    companion object {
        var windowTitle = ""
        var icon = "icons/jsondialogue_icon_x48.png"
    }

    private lateinit var stageFactory: StageFactory

    override fun start(stage: Stage) {

        initialScene(stage)
        stage.show()
    }

    private fun initialScene(stage: Stage): Stage {

        stageFactory = StageFactory("main-view", stage)
        stageFactory.generate(900.0, 400.0)

        stage.title = windowTitle
        stage.scene = stageFactory.getScene()

        val controller: MainController = stageFactory.getController() as MainController
        controller.configure(stage)

        return stage
    }

}

fun main() {
    Application.launch(Main::class.java)
}