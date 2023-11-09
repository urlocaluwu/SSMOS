package SSM.Projectiles;

import SSM.Events.SmashDamageEvent;
import SSM.Utilities.ServerMessageType;
import SSM.Utilities.Utils;
import SSM.Utilities.VelocityUtil;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BileProjectile extends SmashProjectile {

    public BileProjectile(Player firer, String name) {
        super(firer, name);
        this.damage = 1;
        this.hitbox_size = 0.5;
        this.knockback_mult = 1;
        this.expiration_ticks = 40;
    }

    @Override
    protected Entity createProjectileEntity() {
        ItemStack flesh = new ItemStack(Material.ROTTEN_FLESH);
        return firer.getWorld().dropItem(firer.getEyeLocation().add(firer.getLocation().getDirection()).subtract(0, 0.5, 0), flesh);
    }

    @Override
    protected void doVelocity() {
        Vector random = new Vector((Math.random() - 0.5) * 0.525, (Math.random() - 0.5) * 0.525, (Math.random() - 0.5) * 0.525);
        VelocityUtil.setVelocity(projectile, firer.getLocation().getDirection().add(random),
                0.8, false, 0, 0.2, 10, false);
    }

    @Override
    protected void doEffect() {
        return;
    }

    @Override
    protected boolean onExpire() {
        return true;
    }

    @Override
    protected boolean onHitLivingEntity(LivingEntity hit) {
        SmashDamageEvent smashDamageEvent = new SmashDamageEvent(hit, firer, damage);
        smashDamageEvent.multiplyKnockback(knockback_mult);
        smashDamageEvent.setReason(name);
        smashDamageEvent.callEvent();
        return true;
    }

    @Override
    protected boolean onHitBlock(Block hit) {
        return true;
    }

    @Override
    protected boolean onIdle() {
        return false;
    }

}
