package com.example.platestack

import org.platestack.api.plugin.PlatePlugin
import org.platestack.api.plugin.annotation.Plate
import org.platestack.api.plugin.annotation.Version

@Plate(id = "simple_java_plugin", name = "Simple Groovy Plugin", version = @Version("0.1.0-SNAPSHOT"))
@Singleton(strict = false)
class SimpleHelloWorld extends PlatePlugin
{
    SimpleHelloWorld()
    {
        logger.info("Hello World from ${annotation.name()} version $version");
    }
}
