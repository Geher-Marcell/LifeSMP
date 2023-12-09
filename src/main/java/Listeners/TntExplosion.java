package Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class TntExplosion implements Listener {
    @EventHandler
    public void TntExplode(EntityExplodeEvent event){
        if (event.getEntity().getType() == EntityType.PRIMED_TNT || event.getEntity().getType() == EntityType.MINECART_TNT){
            event.setCancelled(true);
            event.getEntity().remove();
            event.getEntity().getWorld().createExplosion(event.getLocation(), 4, true, false);
        }
    }
}
