package github.magnilex.eltk.schemaexport.gui

import javafx.stage.Stage
import tornadofx.App
import tornadofx.launch

class EltkFixtureExporter : App(MainView::class) {
    override fun start(stage: Stage) {
        stage.minHeight = 200.0
        stage.minWidth = 400.0
        stage.isResizable = false
        super.start(stage)
    }
}

fun main(args: Array<String>) {
    launch<EltkFixtureExporter>(args)
}