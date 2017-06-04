import org.platestack.api.plugin {
    PlatePlugin
}
import org.platestack.api.plugin.annotation {
    plate,
    ver=version,
    library
}
import org.slf4j {
    Logger
}

plate {
    id = "simple_ceylon_plugin";
    name = "Simple Ceylon Plugin";
    version = ver("0.1.0-SNAPSHOT");
    requires = {library("org.ceylon-lang", "ceylon.language", "1.3.2")};
}
class SimpleHelloWorld() extends PlatePlugin() {

    Logger getLogger() => logger;

    shared actual void onEnable() {
        getLogger().info("Hello World from ``metadata.name`` version ``version``");
    }

}
