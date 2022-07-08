package com.geert.jsondialogue.helpers

import java.io.File
import java.nio.file.Paths


class FilesHelper {

    fun readFileAsText(filePath: String): String = File(filePath).readText(Charsets.UTF_8)

    fun writeToFile(filePath: String, text: String) {

        File(filePath).printWriter().use { out ->
            out.print(text)
        }
    }

    fun getFolderPath(fullPath: String?): String {
        if (fullPath == null || fullPath == "") return ""

        if (fullPath.isBlank() || fullPath == "null") {
            return ""
        }

        return Paths.get(fullPath).parent.toString()
    }

    fun getFileFromFullPath(fullPath: String?): String {
        if (fullPath.isNullOrBlank() || fullPath == "null") {
            return ""
        }

        return Paths.get(fullPath).fileName.toString()
    }
}