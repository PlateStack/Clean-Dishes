package com.example.platestack // You don't need to have the same directory structure as your package
                               // But your IDE may yell at you with an ignorable warning

import org.platestack.api.plugin.PlatePlugin
import org.platestack.api.plugin.annotation.Plate
import org.platestack.api.plugin.annotation.Version

/**
 * Kotlin is more flexible then Java, no more then one object of your plugin may exists at the same time,
 * so you should use `object` instead of `class`. You can use `class` too only because Java doesn't
 * have this feature so Java plugins needs to use a normal `class` to work and since Kotlin is Java compatible
 * you can still do the Java-way unnecessarily. This may change in future.
 *
 * It's recommended to specify every part of your version separately in your [Version] annotations.
 * If you see hints like "major:", "minor:", "patch:", etc then you may want to disable it.
 * To disable just do these steps:
 * 1. Alt+Enter next to a hint
 * 2. click on "Do not show hints for current method"
 * 3. click on "Show Parameter Hints Settings"
 * 4. remove the parenthesis from Version to blacklist the entire annotation.
 */
@Plate("kotlin_obj_plugin", "Simple Kotlin Plugin", Version(0,1,0,"SNAPSHOT"), kotlin = "1.1.2-4")
object SimpleHelloWorld: PlatePlugin() {

    override fun onEnable() {
        logger.info { "Hello World from ${metadata.name} version $version" }
    }

}
