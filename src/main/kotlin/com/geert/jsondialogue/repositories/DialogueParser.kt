package com.geert.jsondialogue.repositories

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.geert.jsondialogue.data.TableDialogueData
import com.geert.jsondialogue.modelviews.TableDialogue


class DialogueParser {

    private val mapper = jacksonObjectMapper()

    fun process(rows: List<TableDialogue>): String {

        val list = mutableListOf<TableDialogueData>()

        rows.forEach {

            val data = TableDialogueData(
                it.id.get(), it.characterId.get(), it.expression.get(), it.text.get(), it.goTo.get(),
                it.options.get(), it.values.get(), it.setCurrentDialogue.get(), it.signal.get(), it.function.get()
            )

            list.add(data)
        }

        mapper.propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE

        return mapper.writeValueAsString(list) ?: ""
    }

    fun jsonStringToMap(jsonString: String): ArrayList<TableDialogueData>? {
        mapper.propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE

        try {
            return mapper.readValue(jsonString)
        } catch (ex: Exception) {
            println("Error: couldn't read json file.")
        }

        return null

    }

}
