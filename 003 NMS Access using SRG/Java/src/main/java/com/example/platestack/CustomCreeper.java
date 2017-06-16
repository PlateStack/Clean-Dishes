package com.example.platestack;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.function.Supplier;

public class CustomCreeper extends EntityCreeper
{

    public CustomCreeper(World worldIn)
    {
        super(worldIn);
        setCustomNameTag("Please don't hurt me!");
        setAlwaysRenderNameTag(true);
        enablePersistence();
    }

    /**
     * Custom AI which makes the creeper afraid from players. It will still use explosion but in a defensive way.
     * <p>
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
            double otherX = posX + rand.nextDouble() * 2;
            double otherZ = posZ + rand.nextDouble() * 2;
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

        EntityItem item = forceDrop(() -> dropItem(Item.getItemFromBlock(Blocks.YELLOW_FLOWER), 1));
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
        } catch(ReflectiveOperationException e)
        {
            field = null;
        }

        try
        {
            return task.get();
        } finally
        {
            if(field != null)
            {
                try
                {
                    field.setBoolean(this, original);
                } catch(ReflectiveOperationException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
