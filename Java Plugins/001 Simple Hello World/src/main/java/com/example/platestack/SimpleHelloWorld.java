package com.example.platestack;

import org.platestack.api.plugin.PlatePlugin;
import org.platestack.api.plugin.annotation.Plate;
import org.platestack.api.plugin.annotation.Version;

@Plate(id = "simple_java_plugin", name = "Simple Java Plugin", version = @Version("0.1.0-SNAPSHOT"))
public class SimpleHelloWorld extends PlatePlugin
{
    /**
     * IMPORTANT: Your plugin is not enabled yet at the construction stage but it's the only way
     * to show a hello world right now.
     */
    public SimpleHelloWorld()
    {
        getLogger().info("Hello World from "+getAnnotation().name()+" version "+getVersion());
    }
}
