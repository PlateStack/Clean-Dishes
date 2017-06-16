import net.minecraft.entity.EntityList
import net.minecraft.entity.monster.EntityCreeper
import net.minecraft.server.MinecraftServer
import net.minecraft.util.ResourceLocation
import org.platestack.api.minecraft.Minecraft
import org.platestack.api.plugin.PlatePlugin
import org.platestack.api.plugin.annotation.Plate
import org.platestack.api.plugin.annotation.Version

/**
 * Created by José Roberto on 16/06/2017.
 */


/**
 * This example plugin creates a custom creeper which is afraid of players.
 * It will send messages when a player collides, hurt or kill it.
 * It will also drop an yellow flower after death.
 * It will not despawn when away from players.
 * It will spawn clones when hit by a player.
 * It will revert to an normal creeper after a server restart but the custom name tag will still be there.
 */
@Plate(id = "kotlin_nms_plugin", name = "Java NMS Plugin", version = Version(0,1,0,"SNAPSHOT"), kotlin = "1.1.2-4")
class SrgPluginExample : PlatePlugin()
{
    override fun onEnable() {
        // Gets the MinecraftServer instance using the PlateStack API.
        val server = Minecraft.instance as MinecraftServer

        // Unfortunately we need to register all our custom entity classes, otherwise it will be invisible to the players.
        // This will use the same registration data as a normal creeper.
        EntityList.REGISTRY.register(
                EntityList.REGISTRY.getIDForObject(EntityCreeper::class.java),
                ResourceLocation("creeper"),
                CustomCreeper::class.java
        )

        // Find a safe spawn position in the over world
        val overWorld = server.getWorld(0)
        val worldSpawn = overWorld.getHeight(overWorld.getSpawnPoint()).add(0,2,0)

        // Creates the creeper
        val creeper = CustomCreeper(overWorld)
        creeper.setPosition(worldSpawn.x.toDouble(), worldSpawn.y.toDouble(), worldSpawn.z.toDouble())

        // Add it to the world
        logger.info { "Spawning creeper at: $worldSpawn" }
        overWorld.spawnEntity(creeper)
        logger.info { "Creeper: $creeper" }
    }
}
