package com.example.platestack

import org.platestack.api.plugin.PlatePlugin
import org.platestack.api.plugin.annotation.{Plate, Version}

@Plate(id = "scala_plugin", name = "My Scala Plugin", version = new Version("0.1.0-SNAPSHOT"))
object ScalaPlugin extends PlatePlugin {

  getLogger.info("Hello PlateStack, I'm Scala")

}
