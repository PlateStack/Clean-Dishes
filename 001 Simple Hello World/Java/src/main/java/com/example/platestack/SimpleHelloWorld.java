package com.example.platestack;

import org.platestack.api.plugin.PlatePlugin;
import org.platestack.api.plugin.annotation.Plate;
import org.platestack.api.plugin.annotation.Version;

/**
 * The class must be public, must extends {@link PlatePlugin} and have a {@link Plate} annotation.
 *
 * It's recommended to specify every part of your version separately in your {@link Version} annotations
 * but it gets overcomplicated in pure java, so you can use {@link Version#value()} here.
 * <p>
 * Make sure to follow the <a href="http://semver.org/spec/v2.0.0.html">Semantic Versioning Specification 2.0.0</a>
 * or your plugin will fail to load.
 */
@Plate(id = "simple_java_plugin", name = "Simple Java Plugin", version = @Version("0.1.0-SNAPSHOT"))
public class SimpleHelloWorld extends PlatePlugin
{
    @Override
    protected void onEnable()
    {
        getLogger().info("Hello World from {} version {}", getMetadata().getName(), getVersion());
    }
}
