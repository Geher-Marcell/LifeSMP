package Listeners;

import l10.dev.main.Seaseon2;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class OnChatMessage implements Listener {
    @EventHandler
    public void PlayerChatEvent (PlayerChatEvent event) {
        event.setCancelled(true);
        Player p = event.getPlayer();
        String message = event.getMessage();

        Bukkit.broadcastMessage(p.getDisplayName() + ChatColor.WHITE + ": " + message);
    }
}
