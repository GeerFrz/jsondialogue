package com.geert.jsondialogue.widgets

import com.geert.jsondialogue.Main
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.image.Image
import javafx.stage.Stage
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.Style

class Messages {

    companion object {
        private val jMetro = JMetro(Style.DARK)

        fun alert(mpMessages: MutableList<String>, cfg: MutableMap<String, Any> = mutableMapOf()): Alert {
            val title = if (cfg.isEmpty() || !cfg.contains("title")) "Alert" else cfg.getValue("title") as String

            val alert = Alert(Alert.AlertType.ERROR)

            alert.contentText = mpMessages.joinToString(System.getProperty("line.separator"))

            val stage = alert.dialogPane.scene.window as Stage
            stage.title = title

            applyCssStyleToScene(alert.dialogPane.scene)
            addIconToDialog(alert)

            return alert
        }

        fun confirm(mpMessages: MutableList<String>, cfg: MutableMap<String, Any> = mutableMapOf()): Int {
            val title = if (cfg.contains("title")) cfg.getValue("title").toString() else "Confirm"
            val headerText =  if (cfg.contains("headerText")) cfg.getValue("headerText").toString() else ""

            val alert = Alert(Alert.AlertType.CONFIRMATION)

            alert.title = title
            alert.headerText = headerText
            alert.contentText = mpMessages.joinToString(System.getProperty("line.separator"))

            val btnNo = ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE)
            val btnYes = ButtonType("Yes")

            alert.buttonTypes.setAll(btnNo, btnYes)

            applyCssStyleToScene(alert.dialogPane.scene)

            applyCssStyleToButtonTypes(alert, listOf("button"))

            addIconToDialog(alert)

            var res = 0
            val result = alert.showAndWait()
            if (result.get() == btnNo) {
                res = 0
            } else if (result.get() == btnYes) {
                res = 1
            }

            return res
        }

        private fun addIconToDialog(dialog: Alert) {
            val stage = dialog.dialogPane.scene.window as Stage
            stage.icons.add(Image(Main::class.java.getResourceAsStream(Main.icon)))
        }

        private fun applyCssStyleToScene(scene: Scene) {
            jMetro.scene = scene
            scene.stylesheets.add(Main::class.java.getResource("css/custom.css")?.toExternalForm() ?: "")
        }

        private fun applyCssStyleToButtonTypes(alert: Alert, styles: List<String>) {
            val buttonTypes = alert.dialogPane.buttonTypes

            styles.forEach {cssClass ->

                buttonTypes.forEach { type ->
                    val button = alert.dialogPane.lookupButton(type)

                    if (!button.styleClass.contains(cssClass)) {
                        button.styleClass.add(cssClass)
                    }
                }
            }
        }

    }

}