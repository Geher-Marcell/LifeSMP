package Commands;

import l10.dev.main.Seaseon2;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartEndGame implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player)sender;
        if(player.isOp()){
            Bukkit.getServer().dispatchCommand(sender, "worldborder set 30 1800");
            Bukkit.getServer().dispatchCommand(sender, "gamerule doDaylightCycle false");
            Bukkit.getServer().dispatchCommand(sender, "time set midnight");

            for (Player p : Bukkit.getOnlinePlayers()){
                p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 0.75F, 0.5F);
                p.sendTitle(ChatColor.DARK_RED + "Megkezdődött a Végjáték!", "");
                p.sendMessage(ChatColor.RED + "30 percen át lecsökken a World Border 30x30 blokkra!");
            }
        }
        return false;
    }
}
