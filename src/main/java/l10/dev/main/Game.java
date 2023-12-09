package l10.dev.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("ALL")
public class Game implements Listener, CommandExecutor, TabCompleter {

    Seaseon2 main = Seaseon2.getInstance();

    public BukkitTask GameTimer = null;
    private final int sessionLength = 60*60*3; //óra

    public int getSessionLength() {return sessionLength; }
    public int getPauseLength() {
        //perc
        return 60 * 15;
    }

    public int RemainingSessionLength = 0;
    public int RemainingPauseLength = 0;
    public boolean isPaused = false;

    public void GameLogic(){
        for(Player p : Bukkit.getOnlinePlayers()) {
            main.CreateAndUpdateScoreboard(p);
        }

        if(RemainingSessionLength == 0) GameEnd();

        int halfLength = (int)(sessionLength * 0.5);

        if(halfLength == RemainingSessionLength && !isPaused){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "game pause");
            Bukkit.getOnlinePlayers().forEach(p -> p.kickPlayer("SZÜNET!!!!\n" + main.SecondsToClock(RemainingPauseLength)));
        }
        if(RemainingPauseLength == 0 && isPaused) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "game resume");
            RemainingPauseLength = -1;
        }

        if(RemainingSessionLength - halfLength <= 10 && RemainingSessionLength - halfLength > 0)
            Bukkit.broadcastMessage(main.prefix + "Szünet " + (RemainingSessionLength - halfLength) + " másodperc múlva!");
        if(RemainingSessionLength <= 10 && RemainingSessionLength > 0)
            Bukkit.broadcastMessage(main.prefix + "Játék vége " + (RemainingSessionLength) + " másodperc múlva!");

        if(!isPaused) RemainingSessionLength--;
        else RemainingPauseLength--;
    }

    private void GameEnd(){
        SetupEndMessages();

        GameTimer.cancel();
        GameTimer = null;
        isPaused = false;
        RemainingSessionLength = sessionLength;

        int index = ThreadLocalRandom.current().nextInt(0, AllStats.size()-1);

        Statistic stat = (Statistic) AllStats.keySet().toArray()[index];
        Player highestPlayer = GetHighestPlayerOfStat(stat);
        String message = AllStats.values().toArray()[index].toString();

        ChatColor clr = ChatColor.GREEN;

        Bukkit.broadcastMessage(clr+">>>>>>>> A játéknak vége! <<<<<<<<");
        Bukkit.broadcastMessage(clr+"");
        if(KocsogFilko.getInstance().TryBuntetKocsog()) Bukkit.broadcastMessage(clr+"A köcsögfilkó senkit nem tudott megölni!");
        else Bukkit.broadcastMessage(clr+"A köcsögfilkó: " + KocsogFilko.getInstance().KocsogFilko() + " volt");
        Bukkit.broadcastMessage(clr+"");
        Bukkit.broadcastMessage(clr+"Az event kezdete óta a(z) " + message + "");
        Bukkit.broadcastMessage(clr+ "" +highestPlayer.getName() + " (" + highestPlayer.getStatistic(stat)+")");
        Bukkit.broadcastMessage(clr+"");
        Bukkit.broadcastMessage(clr+"");
        Bukkit.broadcastMessage(clr+"");
        Bukkit.broadcastMessage(clr+">>> Várunk vissza legközelebb! <<<");

        /*
        Bukkit.getScheduler().runTaskLater(main, new Runnable() {
            @Override
            public void run() {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
            }
        }, 20*10);
         */
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        if(args.length == 0){
            sender.sendMessage("&cHibá használat bazdmeg");
        }
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("start")){
                if(GameTimer != null) return false;

                RemainingSessionLength = getSessionLength();

                GameTimer = new BukkitRunnable() {
                    @Override
                    public void run() {
                        GameLogic();
                    }
                }.runTaskTimer(main, 0, 20L);
            }
            else if(args[0].equalsIgnoreCase("stop")){
                if(GameTimer == null) return false;

                GameTimer.cancel();
                GameTimer = null;
                isPaused = false;
                RemainingSessionLength = 0;
            }
            else if(args[0].equalsIgnoreCase("pause")){
                RemainingPauseLength = getPauseLength();
                isPaused = true;
            }
            else if(args[0].equalsIgnoreCase("resume")){
                isPaused = false;
            }
        }
        if(args.length == 2){
            if(args[0].equalsIgnoreCase("settime")){
                if(Integer.parseInt(args[1]) > 0){
                    RemainingSessionLength = Integer.parseInt(args[1]);
                }
            }
            else if(args[0].equalsIgnoreCase("addtime")){
                if(Integer.parseInt(args[1]) > 0){
                    RemainingSessionLength += Integer.parseInt(args[1]);
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String str, String[] args) {
        List<String> completions = new ArrayList<>();

        if(args.length == 1){
            completions.add("start");
            completions.add("stop");
            completions.add("pause");
            completions.add("resume");
            completions.add("settime");
            completions.add("addtime");
        }
        else if (args.length == 2){
            if(args[0].equalsIgnoreCase("settime") || args[0].equalsIgnoreCase("addtime")) {
                completions.add("Másodperc");
            }
        }

        return  completions;
    }

    @EventHandler
    public void JoinEvent(AsyncPlayerPreLoginEvent e){
        if(!isPaused) return;

        e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "SZÜNET!!!!\n"+main.SecondsToClock(RemainingPauseLength));

    }

    private HashMap<Statistic, String> AllStats = new HashMap<>();

    private void SetupEndMessages(){
        //Az event kezdete óta a(z)
        AllStats.put(Statistic.PLAYER_KILLS, "a legtöbb játékost ölte meg");
        AllStats.put(Statistic.MOB_KILLS, "a legtöbb mobot ölte meg");
        AllStats.put(Statistic.ANIMALS_BRED, "a legtöbb állatot szaporította");
        AllStats.put(Statistic.MINE_BLOCK, "a legtöbb blokkot bányászta");
        AllStats.put(Statistic.BREAK_ITEM, "a legtöbb itemet törte össze");
        AllStats.put(Statistic.USE_ITEM, "a legtöbb itemet használta");
    }

    private Player GetHighestPlayerOfStat(Statistic stat){
        Player highest = (Player) Bukkit.getOnlinePlayers().toArray()[0];

        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.getStatistic(stat) > highest.getStatistic(stat)){
                highest = p;
            }
        }

        return highest;
    }
}
