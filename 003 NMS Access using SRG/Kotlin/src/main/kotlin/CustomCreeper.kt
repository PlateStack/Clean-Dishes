import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.*
import net.minecraft.entity.boss.EntityWither
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.monster.EntityCreeper
import net.minecraft.entity.passive.EntityOcelot
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.util.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.World
import java.util.*

class CustomCreeper(worldIn: World) : EntityCreeper(worldIn) {

    init {
        customNameTag = "Please don't hurt me!"
        alwaysRenderNameTag = true
        enablePersistence()
    }

    /**
     * Custom AI which makes the creeper afraid from players. It will still use explosion but in a defensive way.
     *
     *
     * It will also hunt withers.
     */
    override fun initEntityAI() {
        this.tasks.addTask(1, EntityAISwimming(this))
        this.tasks.addTask(2, EntityAICreeperSwell(this))
        this.tasks.addTask(3, EntityAIAvoidEntity(this, EntityPlayer::class.java, 10.0f, 1.0, 1.2))
        this.tasks.addTask(4, EntityAIAvoidEntity(this, EntityOcelot::class.java, 8.0f, 1.0, 1.2))
        this.tasks.addTask(5, EntityAIAttackMelee(this, 1.0, false))
        this.tasks.addTask(6, EntityAIWanderAvoidWater(this, 0.8))
        this.tasks.addTask(7, EntityAIWatchClosest(this, EntityWither::class.java, 8.0f))
        this.tasks.addTask(7, EntityAILookIdle(this))
        this.targetTasks.addTask(1, EntityAINearestAttackableTarget(this, EntityWither::class.java, true))
        this.targetTasks.addTask(2, EntityAIHurtByTarget(this, false))
    }

    /**
     * Sends a message and spawns a clone when hit by a player, unless it is already dead.
     */
    override fun attackEntityFrom(damageSrc: DamageSource, damageAmount: Float): Boolean {
        if (!super.attackEntityFrom(damageSrc, damageAmount))
            return false

        val entity = damageSrc.entity
        if (!isDead && health > 0 && entity is EntityPlayer) {
            entity.sendMessage(TextComponentString("Please! Don't..."))

            val rand = Random()
            val otherX = posX + rand.nextDouble() * 2
            val otherZ = posZ + rand.nextDouble() * 2
            val height = world.getHeight(otherX.toInt(), otherZ.toInt())
            if (BlockPos(otherX, height.toDouble(), otherZ).getDistance(posX.toInt(), posY.toInt(), posZ.toInt()) <= 4.0) {
                val other = CustomCreeper(world)
                other.setPosition(otherX, height + 0.75, otherZ)
                world.spawnEntity(other)
            }
        }

        return true
    }

    /**
     * Sends a sad face to the killer and drops an yellow flower
     */
    override fun onDeath(cause: DamageSource) {
        super.onDeath(cause)
        cause.entity?.sendMessage(TextComponentString(":("))

        forceDrop {
            dropItem(Item.getItemFromBlock(Blocks.YELLOW_FLOWER), 1)
        }?.setEntityInvulnerable(true)
    }

    /**
     * Sends a message to the player
     */
    override fun onCollideWithPlayer(player: EntityPlayer) {
        player.sendMessage(TextComponentString("Ouch!"))
        super.onCollideWithPlayer(player)
    }

    /**
     * This will toggle the forceDrops field during the execution of the supplier parameter.
     * The field is declared by a CraftBukkit patch on CraftBukkit and Spigot servers, so it's not obfuscated
     * and is not present on non-craftbukkit servers.
     */
    private inline fun forceDrop(task: () -> EntityItem?): EntityItem? {
        val (field, original) =
                try {
                    EntityLivingBase::class.java.getDeclaredField("forceDrops").let {
                        it.isAccessible = true
                        it to it.getBoolean(this)
                    }
                } catch (ignored: ReflectiveOperationException) {
                    return task()
                }

        try {
            return task()
        }
        finally {
            try {
                field.setBoolean(this, original)
            } catch (e: ReflectiveOperationException) {
                e.printStackTrace()
            }
        }
    }
}
