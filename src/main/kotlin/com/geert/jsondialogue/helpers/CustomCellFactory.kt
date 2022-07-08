package com.geert.jsondialogue.helpers

import javafx.application.Platform
import javafx.scene.control.ContentDisplay
import javafx.scene.control.TableCell
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode

class CustomCellFactory<T> : TableCell<T, String?>() {
    private var textField: TextField = TextField()

    init {
        textField.setOnKeyPressed { keyEvent ->
            if (keyEvent.code === KeyCode.ENTER) {

                Platform.runLater {
                    commitEdit(textField.text)
                    contentDisplay = ContentDisplay.TEXT_ONLY
                }
            }
        }

        textField.focusedProperty().addListener { _, _, isFocused ->
            if(isFocused){
                return@addListener
            }

            contentDisplay = ContentDisplay.TEXT_ONLY
        }
    }

    override fun startEdit() {
        super.startEdit()

        if(isEmpty){
            return
        }

        graphic = textField
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        textField.requestFocus()
    }

    override fun updateItem(item: String?, empty: Boolean) {
        super.updateItem(item, empty)

        if(empty){
            text = null
            graphic = null
        } else {
            text = item
            textField.text = item
            contentDisplay = ContentDisplay.TEXT_ONLY
        }
    }
}
