package Listeners;

import l10.dev.main.Seaseon2;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerHealthUpdate implements Listener {
    Seaseon2 main = Seaseon2.getInstance();
    @EventHandler
    public void onPlayerHealthUpdate(EntityDamageEvent e) {
        if(e.getEntity().getType() != EntityType.PLAYER) return;
        Player p = (Player) e.getEntity();

        Bukkit.getServer().getScheduler().runTaskLater(main, new Runnable() {
            public void run() {
                int health = (int) p.getMaxHealth();
                int damage = (int) e.getFinalDamage();

                main.UpdatePlayerHealth(p, health - damage);
            }
        }, 0);
    }
}
