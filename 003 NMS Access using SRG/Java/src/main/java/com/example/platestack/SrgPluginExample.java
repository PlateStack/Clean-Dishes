package com.example.platestack;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.jetbrains.annotations.NotNull;
import org.platestack.api.plugin.PlatePlugin;
import org.platestack.api.plugin.annotation.Plate;
import org.platestack.api.plugin.annotation.Version;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.function.Supplier;

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
    public static class CustomCreeper extends EntityCreeper {

        public CustomCreeper(World worldIn)
        {
            super(worldIn);
            setCustomNameTag("Please don't hurt me!");
            setAlwaysRenderNameTag(true);
            enablePersistence();
        }

        /**
         * Custom AI which makes the creeper afraid from players. It will still use explosion but in a defensive way.
         *
         * It will also hunt withers.
         */
        @Override
        protected void initEntityAI()
        {
            this.tasks.addTask(1, new EntityAISwimming(this));
            this.tasks.addTask(2, new EntityAICreeperSwell(this));
            this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityPlayer.class, 10.0F, 1.0D, 1.2D));
            this.tasks.addTask(4, new EntityAIAvoidEntity<>(this, EntityOcelot.class, 8.0F, 1.0D, 1.2D));
            this.tasks.addTask(5, new EntityAIAttackMelee(this, 1.0D, false));
            this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.8D));
            this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityWither.class, 8.0F));
            this.tasks.addTask(7, new EntityAILookIdle(this));
            this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityWither.class, true));
            this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
        }

        /**
         * Sends a message and spawns a clone when hit by a player, unless it is already dead.
         */
        @Override
        public boolean attackEntityFrom(DamageSource damageSrc, float damageAmount)
        {
            if(!super.attackEntityFrom(damageSrc, damageAmount))
                return false;

            Entity entity = damageSrc.getEntity();
            if(!isDead && getHealth() > 0 && entity instanceof EntityPlayer)
            {
                entity.sendMessage(new TextComponentString("Please! Don't..."));

                Random rand = new Random();
                double otherX = posX + rand.nextDouble()*2;
                double otherZ = posZ + rand.nextDouble()*2;
                int height = world.getHeight((int) otherX, (int) otherZ);
                if(new BlockPos(otherX, height, otherZ).getDistance((int) posX, (int) posY, (int) posZ) <= 4.0)
                {
                    CustomCreeper other = new CustomCreeper(world);
                    other.setPosition(otherX, height + 0.75, otherZ);
                    world.spawnEntity(other);
                }
            }

            return true;
        }

        /**
         * Sends a sad face to the killer and drops an yellow flower
         */
        @Override
        public void onDeath(@NotNull DamageSource cause)
        {
            super.onDeath(cause);
            Entity entity = cause.getEntity();
            if(entity != null)
                entity.sendMessage(new TextComponentString(":("));

            EntityItem item = forceDrop(()-> dropItem(Item.getItemFromBlock(Blocks.YELLOW_FLOWER), 1));
            if(item != null)
                item.setEntityInvulnerable(true);
        }

        /**
         * Sends a message to the player
         */
        @Override
        public void onCollideWithPlayer(EntityPlayer player)
        {
            player.sendMessage(new TextComponentString("Ouch!"));
            super.onCollideWithPlayer(player);
        }

        /**
         * This will toggle the forceDrops field during the execution of the supplier parameter.
         * The field is declared by a CraftBukkit patch on CraftBukkit and Spigot servers, so it's not obfuscated
         * and is not present on non-craftbukkit servers.
         */
        private EntityItem forceDrop(Supplier<EntityItem> task)
        {
            boolean original = false;
            Field field;
            try
            {
                field = EntityLivingBase.class.getDeclaredField("forceDrops");
                field.setAccessible(true);
                original = field.getBoolean(this);
                field.setBoolean(this, true);
            }
            catch(ReflectiveOperationException e)
            {
                field = null;
            }

            try
            {
                return task.get();
            }
            finally
            {
                if(field != null)
                {
                    try
                    {
                        field.setBoolean(this, original);
                    }
                    catch(ReflectiveOperationException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

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
