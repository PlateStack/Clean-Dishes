package com.example.platestack

import org.platestack.api.plugin.PlatePlugin
import org.platestack.api.plugin.annotation.Plate
import org.platestack.api.plugin.annotation.Version

@Plate("simple_java_plugin", "Simple Java Plugin", Version(0,1,0,"SNAPSHOT"))
class SimpleHelloWorld: PlatePlugin() {

    /**
     * IMPORTANT: Your plugin is not enabled yet at the construction stage but it's the only way
     * to show a hello world right now.
     */
    init {
        logger.info("Hello World from ${annotation.name} version $version");
    }
}
