package github.magnilex.eltk.fixtureimporter.gui

import github.magnilex.eltk.fixtureimporter.service.Fixtures
import github.magnilex.eltk.fixtureimporter.service.exportToCsv
import github.magnilex.eltk.fixtureimporter.service.getFixtures
import github.magnilex.eltk.fixtureimporter.service.getGroups
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation.HORIZONTAL
import javafx.scene.control.Alert.AlertType.INFORMATION
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.Stage
import tornadofx.*
import tornadofx.FileChooserMode.Save

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

class EltkFixtureExporter : App(MainView::class) {
    override fun start(stage: Stage) {
        stage.minHeight = 200.0
        stage.minWidth = 400.0
        stage.isResizable = false
        super.start(stage)
    }
}