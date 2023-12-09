package Listeners;

import fr.mrmicky.fastboard.FastBoard;
import l10.dev.main.Seaseon2;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


@SuppressWarnings("deprecation")
public class OnJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Seaseon2 main = Seaseon2.getInstance();

        FileConfiguration config = main.getConfig();
        Player p = event.getPlayer();
        p.setNoDamageTicks(0);

        event.setJoinMessage(ChatColor.GRAY + "[" + ChatColor.DARK_GREEN + "+" + ChatColor.GRAY + "] " + p.getName());

        Bukkit.getServer().getScheduler().runTaskLater(main, new Runnable() {
            public void run() {
                main.CreateAndUpdateScoreboard(p);
            }
        }, 1);

        if (!config.contains("players." + p.getName())) {
            config.set("players." + p.getName() + ".lives", 6);
            p.setMaxHealth(main.maxHealth);
            p.setHealth(main.maxHealth);
            Seaseon2.getInstance().saveConfig();
        }

        main.UpdatePlayerTeam(p);
    }
}
