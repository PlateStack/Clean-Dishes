package com.example.platestack

import org.platestack.api.plugin.PlatePlugin
import org.platestack.api.plugin.annotation.Plate
import org.platestack.api.plugin.annotation.Version

@Plate("kotlin_mvn", "Kotlin Hello Maven", Version(0,1,0,"SNAPSHOT"))
object HelloMaven : PlatePlugin() {
    override fun onEnable() {
        logger.info("Hello, I'm Kotlin from Maven!")
    }
}
