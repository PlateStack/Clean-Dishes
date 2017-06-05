import org.platestack.api.plugin {
    PlatePlugin
}
import org.platestack.api.plugin.annotation {
    plate,
    ver=version
}

shared plate {
    id = "simple_ceylon_plugin";
    name = "Simple Ceylon Plugin";
    version = ver("0.1.0-SNAPSHOT");
}
class SimpleHelloWorld() extends PlatePlugin() {

    shared actual void onEnable() {
        logger.info("Hello World from ``metadata.name`` version ``version``");
    }

}
