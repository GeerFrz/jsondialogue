package com.geert.jsondialogue.controllers

import com.geert.jsondialogue.data.TableDialogueData
import com.geert.jsondialogue.helpers.CustomCellFactory
import com.geert.jsondialogue.helpers.FilesHelper
import com.geert.jsondialogue.modelviews.TableDialogue
import com.geert.jsondialogue.repositories.DialogueParser
import com.geert.jsondialogue.widgets.Messages
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.MenuItem
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File
import java.net.URL
import java.util.*
import kotlin.system.exitProcess


class MainController : Initializable {

    private lateinit var parentStage: Stage
    private var currentFilePath: String = ""
    private var currentFolderPath: String = ""
    private var currentFileName: String = ""
    private var originalListData: MutableList<TableDialogueData> = mutableListOf()

    @FXML
    private lateinit var tableData: ObservableList<TableDialogue>
    @FXML
    private lateinit var table: TableView<TableDialogue>

    @FXML
    private lateinit var id: TableColumn<TableDialogue, String>
    @FXML
    private lateinit var characterId: TableColumn<TableDialogue, String>
    @FXML
    private lateinit var expression: TableColumn<TableDialogue, String>
    @FXML
    private lateinit var text: TableColumn<TableDialogue, String>
    @FXML
    private lateinit var options: TableColumn<TableDialogue, String>
    @FXML
    private lateinit var values: TableColumn<TableDialogue, String>
    @FXML
    private lateinit var goTo: TableColumn<TableDialogue, String>
    @FXML
    private lateinit var setCurrentDialogue: TableColumn<TableDialogue, String>
    @FXML
    private lateinit var signal: TableColumn<TableDialogue, String>
    @FXML
    private lateinit var function: TableColumn<TableDialogue, String>

    @FXML
    private lateinit var btnAdd: Button
    @FXML
    private lateinit var btnClearAll: Button
    @FXML
    private lateinit var btnRemove: Button
    @FXML
    private lateinit var btnSave: Button

    @FXML
    private lateinit var miOpenJsonFile: MenuItem
    @FXML
    private lateinit var miCloseFile: MenuItem
    @FXML
    private lateinit var miExit: MenuItem
    @FXML
    private lateinit var miCopy: MenuItem
    @FXML
    private lateinit var miAbout: MenuItem
    @FXML
    private lateinit var miDebugTest: MenuItem

    fun configure(stage: Stage) {
        parentStage = stage

        setOnCloseStage(parentStage)
    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        configureTable()

        btnClearAll.setOnAction {
            clearAll()
        }

        btnAdd.setOnAction {
            addRow()
        }

        btnRemove.setOnAction {
            removeRow()
        }

        btnSave.setOnAction {
            saveAsJson()
        }

        miExit.setOnAction {
            Platform.exit()
            exitProcess(0)
        }

        miOpenJsonFile.setOnAction {
            openJsonFile()
        }

        miCloseFile.setOnAction {
            closeFile()
        }

        miDebugTest.setOnAction {
            compareCurrentAndOriginalData("miDebugTest")
        }
    }

    private fun closeFile() {
        clearAll(false)
        currentFilePath = ""
    }

    private fun openJsonFile() {
        val filesHelper = FilesHelper()

        val fileName = openFileChooser(parentStage, false) ?: ""

        if (fileName == "" || fileName == "null") {
            println("No file selected.")
            return
        }

        val fileContent = filesHelper.readFileAsText(fileName.toString())

        val dialogueParser = DialogueParser()

        val listData = dialogueParser.jsonStringToMap(fileContent)
        if (listData == null) {
            val msg = mutableListOf<String>()
            msg.add("Reading JSON file was not possible.")
            msg.add("Please, compare the file structure with the structure in Help -> JSON file structure.")
            Messages.alert(msg).show()
            return
        }

        updateTableData(listData)

        setOriginalData()
        compareCurrentAndOriginalData("updateTableData")
    }

    private fun copyToTableDialogueObject(tdDataObject: TableDialogueData): TableDialogue {

        return TableDialogue(
            tdDataObject.id,
            tdDataObject.characterId,
            tdDataObject.expression,
            tdDataObject.text,
            tdDataObject.goTo,
            tdDataObject.options,
            tdDataObject.values,
            tdDataObject.setCurrentDialogue,
            tdDataObject.signal,
            tdDataObject.function
        )
    }

    private fun updateTableData(listData: ArrayList<TableDialogueData>) {
        table.items.removeAll()
        tableData.clear()

        listData.forEach {
            val objTableDialogue = copyToTableDialogueObject(it)
            tableData.add(objTableDialogue)
        }

        table.parent.requestFocus()
    }

    private fun setOnCloseStage(stage: Stage) {
        stage.onCloseRequest = EventHandler {
            clearCurrentAndOriginalData()

            Platform.exit()
            exitProcess(0)
        }
    }

    private fun clearAll(showWarning: Boolean = true): Int {
        if (tableData.isEmpty()) {
            Messages.alert(mutableListOf("The table is already empty.")).show()
            return 0
        }

        if (showWarning) {
            val cfgConfirm: MutableMap<String, Any> = mutableMapOf("title" to "Confirm")

            val nochangesMade = noChangesMade()

            val msg: MutableList<String> = mutableListOf()

            if (!nochangesMade) {
                msg.add("All changes will be lost.")
            }

            msg.add("Do you really want to clear the whole table?")

            if (Messages.confirm(msg, cfgConfirm) == 0) {
                return 0
            }
        }


        clearCurrentAndOriginalData()

        return 0
    }

    private fun addRow() {

        val row = TableDialogue("", "", "", "", "", "", "", "", "", "")

        tableData.add(row)
    }

    private fun removeRow() {
        val row = table.selectionModel.selectedItem
        if (row == null) {
            Messages.alert(mutableListOf("You have to select a row."))
            return
        }

        val cfgConfirm: MutableMap<String, Any> = mutableMapOf("title" to "Confirm")
        val msg = mutableListOf("Do you really want to remove the selected row?")

        if (Messages.confirm(msg, cfgConfirm) == 0) {
            return
        }

        tableData.remove(row)
    }

    private fun openFileChooser(primaryStage: Stage, saveFile: Boolean = true): File? {

        val fileChooser = FileChooser()

        val extFilter = FileChooser.ExtensionFilter("JSON files (*.json)", "*.json")
        fileChooser.extensionFilters.add(extFilter)

        if (currentFolderPath != "") {

            var defaultDir = File(currentFolderPath)
            if (!defaultDir.canRead()) {
                println("Cant read directory $defaultDir")
                defaultDir = File("")
            }

            fileChooser.initialDirectory = defaultDir
        }

        return if (saveFile) {

            fileChooser.showSaveDialog(primaryStage)
        } else {
            fileChooser.showOpenDialog(primaryStage)
        }
    }

    private fun saveAsJson() {

        if (tableData.isEmpty()) {
            Messages.alert(mutableListOf("The table is empty.")).show()
            return
        }

        val rows: List<TableDialogue> = table.items

        val dialogueParser = DialogueParser()

        val jsonString = dialogueParser.process(rows)

        val fileName = openFileChooser(parentStage).toString()

        val filesHelper = FilesHelper()
        filesHelper.writeToFile(fileName, jsonString)

        currentFilePath = fileName
        currentFolderPath = filesHelper.getFolderPath(currentFilePath)
        currentFileName = filesHelper.getFileFromFullPath(currentFilePath)
    }

    private fun configureTable() {
        tableData = FXCollections.observableArrayList()

        id.cellValueFactory = PropertyValueFactory("id")
        characterId.cellValueFactory = PropertyValueFactory("characterId")
        expression.cellValueFactory = PropertyValueFactory("expression")
        text.cellValueFactory = PropertyValueFactory("text")
        options.cellValueFactory = PropertyValueFactory("options")
        values.cellValueFactory = PropertyValueFactory("values")
        goTo.cellValueFactory = PropertyValueFactory("goTo")
        setCurrentDialogue.cellValueFactory = PropertyValueFactory("setCurrentDialogue")
        signal.cellValueFactory = PropertyValueFactory("signal")
        function.cellValueFactory = PropertyValueFactory("function")


        table.selectionModel.cellSelectionEnabledProperty().set(true)
        table.items = tableData

        configureEditableTableCells()
    }

    private fun configureEditableTableCells() {

        id.setCellFactory { CustomCellFactory() }
        characterId.setCellFactory { CustomCellFactory() }

        expression.setCellFactory { CustomCellFactory() }
        text.setCellFactory { CustomCellFactory() }
        options.setCellFactory { CustomCellFactory() }
        values.setCellFactory { CustomCellFactory() }
        goTo.setCellFactory { CustomCellFactory() }
        setCurrentDialogue.setCellFactory { CustomCellFactory() }
        signal.setCellFactory { CustomCellFactory() }
        function.setCellFactory { CustomCellFactory() }
        characterId.setCellFactory { CustomCellFactory() }

    }

    private fun copyToTableDialogueDataObject(tdObject: TableDialogue): TableDialogueData {

        return TableDialogueData(
            tdObject.id.toString(),
            tdObject.characterId.toString(),
            tdObject.expression.toString(),
            tdObject.text.toString(),
            tdObject.goTo.toString(),
            tdObject.options.toString(),
            tdObject.values.toString(),
            tdObject.setCurrentDialogue.toString(),
            tdObject.signal.toString(),
            tdObject.function.toString()
        )
    }

    private fun noChangesMade(): Boolean {
        val tmpData = mutableListOf<TableDialogueData>()

        tableData.forEach {
            tmpData.add(copyToTableDialogueDataObject(it))
        }

        return originalListData == tmpData
    }

    private fun setOriginalData() {
        val tmpData = mutableListOf<TableDialogueData>()

        tableData.forEach {
            val obj = copyToTableDialogueDataObject(it)
            tmpData.add(obj)
        }

        originalListData = tmpData
    }

    private fun clearCurrentAndOriginalData() {
        tableData.clear()
        originalListData.clear()
    }

    private fun compareCurrentAndOriginalData(updateTableData: String = "") {
        println()
        print("($updateTableData) Current and original comparition: ")
        val isEqual = noChangesMade()
        print(isEqual)
        println()
        println()

        printOriginalData()
        printCurrentData()
    }

    private fun printOriginalData() {
        println("*** Original data:")
        originalListData.forEach {
            println(it.expression)
        }
    }

    private fun printCurrentData() {
        println("*** Current data:")
        tableData.forEach {
            println(it.expression)
        }
    }

}