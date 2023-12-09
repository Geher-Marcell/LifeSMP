package Listeners;

import l10.dev.main.Seaseon2;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class OnKill implements Listener {
    @EventHandler
    void onKill(EntityDeathEvent event){
        if(event.getEntity().getType() == null) return;
        if(event.getEntity().getKiller() == null) return;

        if(event.getEntity().getKiller().getType() == EntityType.PLAYER){
            if(event.getEntity().getType() == EntityType.PLAYER){
                Player p = (Player) event.getEntity().getKiller();
                Player target = (Player) event.getEntity();

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "addhealth " + p.getName() + " 30");
            }
        }
    }
}
