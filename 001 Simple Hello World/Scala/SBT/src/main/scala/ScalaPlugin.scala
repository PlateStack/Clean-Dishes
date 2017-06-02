package com.example.platestack

import org.platestack.api.plugin.PlatePlugin
import org.platestack.api.plugin.annotation.{Plate, Version, Requires, Library}

@Plate(id = "scala_sbt_plugin", name = "My Scala SBT Plugin", version = new Version("0.1.0-SNAPSHOT"))
@Requires(Array(new Library("org.scala-lang", "scala-library", "2.12.2")))
object ScalaPlugin extends PlatePlugin {

  override def onEnable() {
    getLogger.info { s"Hello World from ${getMetadata.getName} version $getVersion" }
  }

}
