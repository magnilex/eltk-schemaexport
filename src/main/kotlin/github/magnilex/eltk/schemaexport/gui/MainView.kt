package github.magnilex.eltk.schemaexport.gui

import github.magnilex.eltk.schemaexport.service.Fixtures
import github.magnilex.eltk.schemaexport.service.exportToCsv
import github.magnilex.eltk.schemaexport.service.getFixtures
import github.magnilex.eltk.schemaexport.service.getGroups
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation.HORIZONTAL
import javafx.scene.control.Alert.AlertType.INFORMATION
import javafx.stage.FileChooser.ExtensionFilter
import tornadofx.FileChooserMode.Save
import tornadofx.View
import tornadofx.action
import tornadofx.alert
import tornadofx.button
import tornadofx.chooseFile
import tornadofx.combobox
import tornadofx.disableWhen
import tornadofx.fieldset
import tornadofx.form
import tornadofx.group
import tornadofx.hbox
import tornadofx.hboxConstraints
import tornadofx.observableList
import tornadofx.onChange
import tornadofx.paddingHorizontal
import tornadofx.paddingVertical
import tornadofx.textfield
import tornadofx.vbox

class MainView : View() {
    val groups = getGroups()
    var fixtures: Fixtures = Fixtures()
    val players = observableList<String>()
    val selectedPlayer = SimpleStringProperty()
    val filename = SimpleStringProperty()

    override val root = group {
        hbox(100) {
            hboxConstraints {
                paddingHorizontal = 20
                paddingVertical = 20
            }
            fieldset("Välj Grupp") {
                combobox(values = groups) {
                    selectionModel.selectedItemProperty().onChange {
                        fixtures = getFixtures(it!!)
                        players.clear()
                        players.addAll(fixtures.getPlayers())
                    }
                }
            }
            vbox(30) {
                fieldset("Välj Spelare") {
                    combobox(selectedPlayer, players)
                }
                form {
                    disableWhen { selectedPlayer.isEmpty }
                    fieldset(labelPosition = HORIZONTAL) {
                        textfield(filename)
                        button("Välj Fil") {
                            setOnMouseClicked {
                                filename.set(chooseFile(filters = arrayOf(ExtensionFilter("csv", "*.csv")), mode = Save).getOrNull(0)?.absolutePath)
                            }
                        }
                    }
                    button("Exportera Matcher") {
                        disableWhen { filename.isEmpty }
                        action {
                            val numberOfExportedFixtures = exportToCsv(fixtures, selectedPlayer.get(), filename.get())
                            alert(INFORMATION, "Exporterat $numberOfExportedFixtures matcher för spelare ${selectedPlayer.get()} till ${filename.get()}")
                        }
                    }
                }
            }
        }
    }
}