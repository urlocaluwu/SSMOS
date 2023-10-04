package SSM.GameManagers;

import SSM.Abilities.Ability;
import SSM.Attributes.Attribute;
import SSM.GameManagers.OwnerEvents.*;
import SSM.SSM;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class EventManager implements Listener {

    private static EventManager ourInstance;
    private JavaPlugin plugin = SSM.getInstance();

    public EventManager() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        ourInstance = this;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Ability ability = KitManager.getCurrentAbility(player);
        if(ability == null) {
            return;
        }
        if (ability instanceof OwnerLeftClickEvent && (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)) {
            OwnerLeftClickEvent leftClick = (OwnerLeftClickEvent) ability;
            leftClick.onOwnerLeftClick(e);
        }
        if (ability instanceof OwnerRightClickEvent && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            OwnerRightClickEvent rightClick = (OwnerRightClickEvent) ability;
            rightClick.onOwnerRightClick(e);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if(KitManager.getPlayerKit(player) == null) {
                return;
            }
            List<Attribute> attributes = KitManager.getPlayerKit(player).getAttributes();
            for (Attribute attribute : attributes) {
                if (attribute instanceof OwnerTakeDamageEvent) {
                    OwnerTakeDamageEvent takeDamage = (OwnerTakeDamageEvent) attribute;
                    takeDamage.onOwnerTakeDamage(e);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        Entity damagee = e.getEntity();
        Entity damager = e.getDamager();
        if (damager instanceof Player) {
            Player player = (Player) damager;
            if(KitManager.getPlayerKit(player) == null) {
                return;
            }
            List<Attribute> attributes = KitManager.getPlayerKit(player).getAttributes();
            for (Attribute attribute : attributes) {
                if (attribute instanceof OwnerDamageEntityEvent) {
                    OwnerDamageEntityEvent damageEntityEvent = (OwnerDamageEntityEvent) attribute;
                    damageEntityEvent.onOwnerDamageEntity(e);
                }
            }
        }
        if(damagee instanceof Player) {
            Player player = (Player) damagee;
            if(KitManager.getPlayerKit(player) == null) {
                return;
            }
            List<Attribute> attributes = KitManager.getPlayerKit(player).getAttributes();
            for (Attribute attribute : attributes) {
                if (attribute instanceof EntityDamageOwnerEvent) {
                    EntityDamageOwnerEvent damageOwnerEvent = (EntityDamageOwnerEvent) attribute;
                    damageOwnerEvent.onEntityDamageOwner(e);
                }
            }
        }
    }

    public static EventManager getInstance() {
        return ourInstance;
    }

}
