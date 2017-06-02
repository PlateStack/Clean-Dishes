package com.example.platestack;

import org.platestack.api.plugin.PlatePlugin;
import org.platestack.api.plugin.annotation.Plate;
import org.platestack.api.plugin.annotation.Version;

@Plate(id = "java_mvn", name = "Java Maven Plugin", version = @Version("0.1.0-SNAPSHOT"))
public class JavaMavenPlugin extends PlatePlugin
{
    @Override
    protected void onEnable()
    {
        getLogger().info("Hello, I'm Java from Maven!");
    }
}
