package Commands;

import l10.dev.main.Seaseon2;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddHealth implements CommandExecutor {
    @Override
    public boolean onCommand( CommandSender sender,  Command cmd,  String str, String[] args) {
        if(!sender.isOp()) return false;
        Seaseon2 main = Seaseon2.getInstance();

        if(args.length != 2){
            sender.sendMessage("§cHelyes használat: /addhealth <játékos> <élet>");
            return false;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if(target == null){
            sender.sendMessage("§cNem található ilyen játékos!");
            return false;
        }

        int health = Integer.parseInt(args[1]);
        int playerHealth = (int)target.getHealth();

        int sumHealth = health + playerHealth;

        if(sumHealth > main.maxHealth)
            main.UpdatePlayerHealth(target, main.maxHealth);
        else if(sumHealth < 0)
            main.UpdatePlayerHealth(target, 0);
        else
            main.UpdatePlayerHealth(target, sumHealth);

        main.UpdatePlayerTeam(target);

        sender.sendMessage("§a" + target.getName() + " élete sikeresen beállítva " + sumHealth + "-ra!");

        return false;
    }
}
