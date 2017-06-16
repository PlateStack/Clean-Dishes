package com.example.platestack;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import org.platestack.api.plugin.PlatePlugin;
import org.platestack.api.plugin.annotation.Plate;
import org.platestack.api.plugin.annotation.Version;

import java.lang.reflect.Field;

/**
 * This example plugin creates a custom creeper which is afraid of players.
 * It will send messages when a player collides, hurt or kill it.
 * It will also drop an yellow flower after death.
 * It will not despawn when away from players.
 * It will spawn clones when hit by a player.
 * It will revert to an normal creeper after a server restart but the custom name tag will still be there.
 */
@Plate(id = "java_nms_plugin", name = "Java NMS Plugin", version = @Version("0.1.0-SNAPSHOT"))
public class SrgPluginExample extends PlatePlugin
{
    @Override
    protected void onEnable()
    {
        // Hack into DedicatedServer
        DedicatedServer server;
        try
        {
            Field target = Thread.class.getDeclaredField("target");
            target.setAccessible(true);

            server = (DedicatedServer) target.get(Thread.currentThread());
        } catch(ReflectiveOperationException e)
        {
            throw new RuntimeException(e);
        }

        // Unfortunately we need to register all our custom entity classes, otherwise it will be invisible to the players.
        // This will use the same registration data as a normal creeper.
        EntityList.REGISTRY.register(
                EntityList.REGISTRY.getIDForObject(EntityCreeper.class),
                new ResourceLocation("creeper"),
                CustomCreeper.class
        );

        // Find a safe spawn position in the over world
        WorldServer overWorld = server.getWorld(0);
        BlockPos worldSpawn = overWorld.getHeight(overWorld.getSpawnPoint()).add(0,2,0);

        // Creates the creeper
        EntityCreeper creeper = new CustomCreeper(overWorld);
        creeper.setPosition(worldSpawn.getX(), worldSpawn.getY(), worldSpawn.getZ());

        // Add it to the world
        getLogger().info("Spawning creeper at: "+worldSpawn);
        overWorld.spawnEntity(creeper);
        getLogger().info("Creeper: "+creeper);
    }
}
