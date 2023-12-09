package Commands;

import l10.dev.main.Seaseon2;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StartLava implements CommandExecutor {
    public void stopScheduler(int id){
        Bukkit.getScheduler().cancelTask(id);
    }

    private int centerX = -1000;
    private int centerZ = 1000;

    private int taskID;
    private int time;
    private int startY;
    private int endY;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        Player player = (Player)sender;
        if(player.isOp()){
            if (args.length != 3){
                player.sendMessage("Hasznalat: /startlava [y ettol] [y eddig] [y/mp]");
                return true;
            }else {
                startY = Integer.parseInt(args[0]);
                endY = Integer.parseInt(args[1]);
                time = Integer.parseInt(args[2]);
                for (Player p : Bukkit.getOnlinePlayers()){
                    p.sendTitle(ChatColor.DARK_RED + "A láva elkezdett emelkedni!", "");
                    p.playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);
                }
            }

            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Seaseon2.getInstance(), new Runnable() {
                @Override
                public void run() {
                    for (int x = centerX-15; x <= centerX+15; x++) {
                        for (int z = centerZ-15; z <= centerZ+15; z++){
                            player.getWorld().getBlockAt(x, startY, z).setType(Material.LAVA);
                        }
                    }
                    player.getWorld().getBlockAt(centerX, startY, centerZ).setType(Material.LAVA);
                    for(Player p : Bukkit.getOnlinePlayers()){
                        if(p.getGameMode() != GameMode.SURVIVAL) return;
                    }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setworldspawn "+ centerX + " " + startY + " " + centerZ);
                    if(startY == endY){
                        stopScheduler(taskID);
                    }else {
                        startY++;
                    }
                }
            }, 0, time*20);
        }
        return true;
    }
}
