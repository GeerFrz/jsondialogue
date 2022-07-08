package com.geert.jsondialogue.modelviews

import javafx.beans.property.SimpleStringProperty

class TableDialogue(
    id: String,
    characterId: String,
    expression: String,
    text: String,
    goTo: String,
    options: String,
    values: String,
    setCurrentDialogue: String,
    signal: String,
    function: String
) {

    var id: SimpleStringProperty = SimpleStringProperty(id)
        get() = field
        set(value) {
            field = value
        }

    fun idProperty(): SimpleStringProperty {
        return id
    }

    var characterId: SimpleStringProperty = SimpleStringProperty(characterId)

    fun characterIdProperty(): SimpleStringProperty {
        return characterId
    }

    var expression: SimpleStringProperty = SimpleStringProperty(expression)
        get() = field
        set(value) {
            field = value
        }

    fun expressionProperty(): SimpleStringProperty {
        return expression
    }

    var text: SimpleStringProperty = SimpleStringProperty(text)
        get() = field
        set(value) {
            field = value
        }

    fun textProperty(): SimpleStringProperty {
        return text
    }

    var goTo: SimpleStringProperty = SimpleStringProperty(goTo)
        get() = field
        set(value) {
            field = value
        }

    fun goToProperty(): SimpleStringProperty {
        return goTo
    }

    var setCurrentDialogue: SimpleStringProperty = SimpleStringProperty(setCurrentDialogue)
        get() = field
        set(value) {
            field = value
        }

    fun setCurrentDialogueProperty(): SimpleStringProperty {
        return setCurrentDialogue
    }

    var options: SimpleStringProperty = SimpleStringProperty(options)
        get() = field
        set(value) {
            field = value
        }

    fun optionsProperty(): SimpleStringProperty {
        return options
    }

    var values: SimpleStringProperty = SimpleStringProperty(values)
        get() = field
        set(value) {
            field = value
        }

    fun valuesProperty(): SimpleStringProperty {
        return values
    }

    var signal: SimpleStringProperty = SimpleStringProperty(signal)
        get() = field
        set(value) {
            field = value
        }

    fun signalProperty(): SimpleStringProperty {
        return signal
    }

    var function: SimpleStringProperty = SimpleStringProperty(function)
        get() = field
        set(value) {
            field = value
        }

    fun functionProperty(): SimpleStringProperty {
        return function
    }

}