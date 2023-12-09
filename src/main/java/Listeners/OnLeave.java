package Listeners;

import l10.dev.main.Seaseon2;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings("deprecation")
public class OnLeave implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();

        Seaseon2.getInstance().DeleteScoreboard(p);

        event.setQuitMessage( ChatColor.GRAY + "["+ ChatColor.DARK_RED +"-"+ChatColor.GRAY+"] " + p.getName());
    }
}
