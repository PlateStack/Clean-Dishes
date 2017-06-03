package com.example.platestack

import org.platestack.api.plugin.PlatePlugin
import org.platestack.api.plugin.annotation.{Plate, Version}

@Plate(id = "scala_mvn", name = "Scala Hello Maven", version = new Version("0.1.0-SNAPSHOT"), scala = "2.12.2")
object ScalaPlugin extends PlatePlugin {

  override def onEnable() {
    getLogger.info("Hello, I'm Scala from Maven!")
  }

}
