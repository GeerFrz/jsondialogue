package com.geert.jsondialogue.helpers

import javafx.application.Platform
import javafx.event.Event
import javafx.event.EventDispatchChain
import javafx.event.EventDispatcher
import javafx.scene.control.ContentDisplay
import javafx.scene.control.TableCell
import javafx.scene.control.TableView.TableViewFocusModel
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode


class CustomCellFactoryOld<T> : TableCell<T, String?>() {
    private var textField: TextField = TextField()
    private var hackIsEditing: Boolean = false
    private var currentValue: String? = null

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
            
            Platform.runLater {
                contentDisplay = ContentDisplay.TEXT_ONLY
            }

            hackIsEditing = false
        }

        textField.setOnKeyPressed { keyEvent ->

            if(keyEvent.code == KeyCode.TAB){
                keyEvent.consume()
                val value = textField.text ?: ""
                //val model: TableViewFocusModel<T> = tableView.focusModel
                commitEdit(value)
                tableView.focusModel.focusRightCell()
            }

        }


    }

    override fun commitEdit(newValue: String?) {
        super.commitEdit(newValue)
        currentValue = newValue

        graphic = textField
        textField.text = newValue
        text = newValue
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        hackIsEditing = false

        //tableView.focusModel.focusRightCell()

        println("commitEdit -> item: $item")
        println("commitEdit -> newValue: $newValue")
    }

    override fun startEdit() {
        hackIsEditing = true

        super.startEdit()
        var originalText = item

        /*
        println("startEdit -> isEmpty: $isEmpty")
        println("originalText 1::: $originalText")
        println("item::: $item")
        println("text::: $text")
        println("textField.text::: ${textField.text}")
        */

        if (!isEmpty) {
            text = textField.text
            graphic = textField
            contentDisplay = ContentDisplay.GRAPHIC_ONLY
            Platform.runLater {
                item = originalText
                text = originalText
                textField.text = originalText

                textField.requestFocus()
            }
        }
    }

    override fun cancelEdit() {
        super.cancelEdit()
        contentDisplay = ContentDisplay.TEXT_ONLY
    }

    override fun updateItem(item: String?, empty: Boolean) {
        super.updateItem(item, empty)

        if (empty) {
            //text = null
            //setGraphic(null)
        } else {

            if (hackIsEditing) {

                println("isEditing: $isEditing")
                println("item: $item")
                println("text: $text")
                println("textField.text: ${textField.text}")
                println("hackIsEditing: $hackIsEditing")

                //setGraphic(textField)
                graphic = textField
                textField.text = textField.text
                text = text

                contentDisplay = ContentDisplay.GRAPHIC_ONLY

            } else {
                text = item
                textField.text = text
                contentDisplay = ContentDisplay.TEXT_ONLY
            }
        }
    }

}