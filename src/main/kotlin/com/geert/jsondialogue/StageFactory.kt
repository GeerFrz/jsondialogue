package com.geert.jsondialogue

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.Style


class StageFactory (private val fxmlFile: String, private val initialStage: Stage?){

    private var fxmlLoader: FXMLLoader = FXMLLoader(Main::class.java.getResource("fxml/$fxmlFile.fxml"))
    private val jMetro = JMetro(Style.DARK)

    private fun applyCssStyles(scene: Scene){

        jMetro.scene = scene
        scene.stylesheets.add(Main::class.java.getResource("css/custom.css").toExternalForm())

    }

    fun generateModal(width: Double = 0.0, height: Double = 0.0, title: String = ""): Stage{
        val owner: Window = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null)

        val stage = Stage()

        stage.scene = Scene(fxmlLoader.load())

        applyCssStyles(stage.scene)

        if("" == title){
            stage.title = Main.windowTitle
        } else {
            stage.title = title
        }

        if(width > 0.0) stage.width = width

        if(height > 0.0) stage.height = height

        stage.initOwner(owner)
        stage.initModality(Modality.WINDOW_MODAL);

        stage.show()
        stage.centerOnScreen()

        return stage
    }

    fun generate(width: Double = 0.0, height: Double = 0.0): Stage{

        if(initialStage!!.scene == null){
            val scene = Scene(fxmlLoader.load())
            initialStage.scene = scene
            applyCssStyles(scene)
        } else {
            initialStage.scene.root = fxmlLoader.load()
        }

        if(width > 0){
            initialStage.width = width
        }

        if(height > 0){
            initialStage.height = height
        }

        initialStage.icons.add(Image(Main::class.java.getResourceAsStream(Main.icon)))
        initialStage.centerOnScreen()

        return initialStage
    }

    fun getController(): Any? {
        return fxmlLoader.getController();
    }

    fun getScene(): Scene{
        return initialStage!!.scene
    }
}