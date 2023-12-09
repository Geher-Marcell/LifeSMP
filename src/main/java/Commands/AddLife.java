package Commands;

import l10.dev.main.Seaseon2;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class AddLife implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender,  Command cmd,  String str, String[] args) {
        FileConfiguration config = Seaseon2.getInstance().getConfig();

        if(!sender.isOp()) return false;

        if(args.length != 2){
            sender.sendMessage("§cHelyes használat: /addlife <játékos> <élet>");
            return false;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if(target == null){
            sender.sendMessage("§cNem található ilyen játékos!");
            return false;
        }

        int life = Integer.parseInt(args[1]);
        int currentLifes = config.getInt("players." + target.getName() + ".lives");

        config.set("players." + target.getName() + ".lives", currentLifes + life);

        Seaseon2.getInstance().saveConfig();
        Seaseon2.getInstance().UpdatePlayerTeam(target);

        sender.sendMessage("§a" + target.getName() + " élete sikeresen beállítva " + (currentLifes+life) + "-ra!");

        return false;
    }
}
