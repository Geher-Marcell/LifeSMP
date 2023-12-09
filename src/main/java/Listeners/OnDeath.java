package Listeners;

import l10.dev.main.Seaseon2;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class OnDeath implements Listener {

    @EventHandler
    public void PlayerDeathEvent (PlayerDeathEvent event) {
        Seaseon2 main = Seaseon2.getInstance();
        FileConfiguration config = main.getConfig();
        Player p = event.getPlayer();

        if (config.contains("players."+p.getName())) {
            int lives = config.getInt("players."+p.getName()+".lives");

            main.UpdatePlayerTeam(p);

            lives--;
            config.set("players."+p.getName()+".lives", lives);
            main.saveConfig();

            Bukkit.getServer().getScheduler().runTaskLater(main, new Runnable() {
                public void run() {
                    main.UpdatePlayerHealth(p, main.maxHealth);
                }
            }, 1);

            if (lives <= 0) {
                p.setGameMode(GameMode.SPECTATOR);
                Bukkit.broadcastMessage(p.getName() + " kifogyott az életekből!");
                p.getWorld().strikeLightningEffect(p.getLocation());
                main.UpdatePlayerTeam(p);
            }
        }
    }
}
