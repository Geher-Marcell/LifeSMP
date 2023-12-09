package l10.dev.main;

import com.destroystokyo.paper.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("ALL")
public class KocsogFilko implements Listener, CommandExecutor {

    private static KocsogFilko instance;
    public static KocsogFilko getInstance(){ return instance; }

    //BoogeyMan stuff
    private Player kocsogfilko;
    private boolean kocsogKilled = false;
    private int penaltyLife = 2;
    private int rewardLife = 1;

    private BukkitTask countdownTask;
    int countdown = 5;

    public KocsogFilko(){
        instance = this;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kocsogvagyok")){
            if(!(sender instanceof Player)) return false;
            if(kocsogfilko == null) return false;

            if(((Player) sender).getName().equals(kocsogfilko.getName()))
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "IGEN");
            else
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "NEM");
        }
        else if (cmd.getName().equalsIgnoreCase("boogeyman")){
            if(kocsogfilko != null)
            {
                sender.sendMessage("Már van köcsög");
                return false;
            }

            List<Player> players = (List<Player>)Bukkit.getOnlinePlayers();

            kocsogfilko = players.get(ThreadLocalRandom.current().nextInt(0, players.size()));
            kocsogKilled = false;

            countdownTask = Bukkit.getScheduler().runTaskTimer(Seaseon2.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if(countdown == 0) {
                        countdownTask.cancel();

                        for (Player p : players){
                            p.sendTitle(new Title(ChatColor.DARK_RED + "A köcsögfilkó..." , "", 10, 50, 0));
                        }
                    }else{
                        for (Player p : players){
                            p.playSound(p.getLocation(), "custom:click", 1, 1);
                            p.sendTitle(new Title( ChatColor.getByChar(Integer.toHexString(new Random().nextInt(4, 16))) + "" + countdown , "", 0, 50, 0));
                        }
                        countdown--;
                    }
                }
            }, 0, 20);

            //Send out titles
            Bukkit.getScheduler().runTaskLater(Seaseon2.getInstance(), new Runnable() {
                @Override
                public void run() {
                    for (Player p : players) {
                        if(p != kocsogfilko) {
                            p.sendTitle(new Title(ChatColor.GREEN + "Nem te vagy!", "", 10, 50, 10));
                            p.playSound(p.getLocation(), "custom:not.kocsog", 1, 1);
                        }
                    }

                    kocsogfilko.sendTitle(new Title(ChatColor.RED + "Te vagy.", ChatColor.RED +"Meg kell ölnöd valakit.", 10, 50, 10));
                    kocsogfilko.playSound(kocsogfilko.getLocation(), "custom:is.kocsog", 1, 0.5F);
                }
            }, (20 * countdown) + 50);
        }
        return false;
    }

    @EventHandler
    public void OnPlayerKill(EntityDeathEvent e){
        if (kocsogKilled) return;

        if(e.getEntity().getType() == null) return;
        if(e.getEntity().getKiller() == null) return;

        if (e.getEntity().getType() != EntityType.PLAYER) return;
        if (e.getEntity().getKiller().getType() != EntityType.PLAYER) return;

        Player killer = e.getEntity().getKiller();
        Player target = (Player)e.getEntity();

        if(killer == kocsogfilko){
            kocsogKilled = true;
            killer.playSound(killer.getLocation(), "custom:cured", 1, 1);
            killer.sendTitle(ChatColor.GREEN + "Leszállt rólad az átok!", ChatColor.GREEN + "+1 élet", 10, 50, 10);
            target.sendTitle("", ChatColor.RED + "-1 élet", 10, 50, 10);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "addlife " + killer.getName() + " " + rewardLife); //Kap reward életet
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "addlife " + target.getName() + " -" + rewardLife); //Kevesebb élettel kezd
        }
    }

    public boolean TryBuntetKocsog(){
        if (!kocsogKilled){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "addlife " + kocsogfilko.getName() + " -" + penaltyLife); //nem öl senkit a
        }

        return !kocsogKilled;
    }

    public Player KocsogFilko(){
        return kocsogfilko;
    }
}
