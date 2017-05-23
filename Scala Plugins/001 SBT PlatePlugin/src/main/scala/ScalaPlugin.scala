package com.example.platestack

@Plate(id = "scala_plugin", name = "My Scala Plugin", version = new Version("0.1.0-SNAPSHOT"))
object ScalaPlugin extends PlatePlugin {

  getLogger.info("Hello PlateStack, I'm Scala")

}
