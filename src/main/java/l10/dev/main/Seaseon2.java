package l10.dev.main;

import Commands.*;
import Listeners.*;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@SuppressWarnings("deprecation")
public class Seaseon2 extends JavaPlugin {
    private static Seaseon2 instance;
    public static String prefix = "" + ChatColor.GRAY;
    private Game game;

    public int maxHealth = 60;

    private final Map<UUID, FastBoard> boards = new HashMap<>();
    private Team greenTeam;
    private Team yellowTeam;
    private Team redTeam;
    private Team specTeam;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        instance = this;

        getServer().getPluginManager().registerEvents(new OnJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerHealthUpdate(), this);
        getServer().getPluginManager().registerEvents(new OnDeath(), this);
        getServer().getPluginManager().registerEvents(new OnChatMessage(), this);
        getServer().getPluginManager().registerEvents(new OnLeave(), this);
        getServer().getPluginManager().registerEvents(new TntExplosion(), this);
        getServer().getPluginManager().registerEvents(new VillagerProfessionChange(), this);
        getServer().getPluginManager().registerEvents(new OnGetEffect(), this);
        getServer().getPluginManager().registerEvents(new OnKill(), this);

        getCommand("startlava").setExecutor(new StartLava());
        getCommand("startendgame").setExecutor(new StartEndGame());

        KocsogFilko kocsogClass = new KocsogFilko();
        getServer().getPluginManager().registerEvents(kocsogClass, this);
        getCommand("kocsogvagyok").setExecutor(kocsogClass);
        getCommand("boogeyman").setExecutor(kocsogClass);

        game = new Game();
        getCommand("game").setExecutor(game);
        getCommand("game").setTabCompleter(game);
        getServer().getPluginManager().registerEvents(game, this);

        getCommand("addhealth").setExecutor(new AddHealth());
        getCommand("addlife").setExecutor(new AddLife());
        getCommand("test").setExecutor(new Test());

        for (Player p : Bukkit.getOnlinePlayers()) {
            CreateAndUpdateScoreboard(p);
        }

        CreateTeams();

        ActionBarStuff();

        DefaultVillagerTrades();

        DefaultPotionConfig();

        Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE +  "------Season 2 plugin has finished loading------" + ChatColor.RESET);
    }

    @Override
    public void onDisable() {

    }

    /* PRIVATE METHODS */

    private void CreateTeams(){
        greenTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("green");
        yellowTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("yellow");
        redTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("red");
        specTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("spec");

        if(greenTeam == null){
            greenTeam = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("green");
            greenTeam.setColor(ChatColor.GREEN);
        }
        if(yellowTeam == null){
            yellowTeam = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("yellow");
            yellowTeam.setColor(ChatColor.YELLOW);
        }
        if(redTeam == null){
            redTeam = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("red");
            redTeam.setColor(ChatColor.RED);
        }
        if(specTeam == null){
            specTeam = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("spec");
            specTeam.setColor(ChatColor.GRAY);
        }
    }

    /// <summary>
    /// Minden másodpercben küld egy actionbart a playereknek az életeikről
    /// </summary>
    private void ActionBarStuff(){
        BukkitTask actionbar = new BukkitRunnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if(p.isDead()) continue;
                    if(p.getGameMode() != GameMode.SURVIVAL) continue;

                    FileConfiguration config = Seaseon2.getInstance().getConfig();
                    int lives = config.getInt("players."+p.getName()+".lives");

                    p.sendActionBar( GetColorByPlayer(p) + "Életek: " + lives);
                }
            }
        }.runTaskTimer(this, 0L, 20L);
    }

    /// <summary>
    /// Beállítja a configban az alap villager tradeket ha nincsenek beállítva
    /// </summary>
    private void DefaultVillagerTrades(){
        if(getConfig().getConfigurationSection("villager_trades") != null) return;
        for(Villager.Profession prof : Villager.Profession.values()){
            if(prof == Villager.Profession.NONE) continue;

            getConfig().set("villager_trades."+prof.name()+".0.sell_item", new ItemStack(Material.DRAGON_HEAD, 1));
            getConfig().set("villager_trades."+prof.name()+".0.purchase_limit", 1);
            getConfig().set("villager_trades."+prof.name()+".0.buy_items.0", new ItemStack(Material.EMERALD_BLOCK, 64));
            saveConfig();
            Bukkit.broadcastMessage("Villager profession: " + prof.name());
        }
    }

    private void DefaultPotionConfig(){
        if(getConfig().isSet("potion_duration_denominator")) return;
        getConfig().set("potion_duration_denominator", 2);
        saveConfig();
    }

    private void UpdateBoard(FastBoard board){
        board.updateLines(
                ChatColor.AQUA + "" + ChatColor.BOLD + "-------------", // Empty line
                ChatColor.GREEN + "Hátralévő idő",
                SecondsToClock(game.RemainingSessionLength),
                ChatColor.AQUA + "" + ChatColor.BOLD +"-------------"
        );
    }

    /* PUBLIC METHODS */

    /// <summary>
    /// Frissíti a játékos csapatát az életei alapján
    /// </summary>

    public void UpdatePlayerHealth(Player p, int newHealth){
        if(newHealth <= 0) newHealth = maxHealth;
        p.setMaxHealth(newHealth);
    }

    public void UpdatePlayerTeam(Player p){
        int lives = getConfig().getInt("players."+p.getName()+".lives");

        if(lives >= 6){
            greenTeam.addPlayer(p);
            p.setDisplayName(ChatColor.GREEN + p.getName());
            p.setGameMode(GameMode.SURVIVAL);
        }
        if(lives < 4){
            yellowTeam.addPlayer(p);
            p.setDisplayName(ChatColor.YELLOW + p.getName());
            p.setGameMode(GameMode.SURVIVAL);
        }
        if(lives < 2){
            redTeam.addPlayer(p);
            p.setDisplayName(ChatColor.RED + p.getName());
            p.setGameMode(GameMode.SURVIVAL);
        }
        if(lives <= 0){
            specTeam.addPlayer(p);
            p.setDisplayName(ChatColor.GRAY + p.getName());
            p.setGameMode(GameMode.SPECTATOR);
        }
    }

    /// <summary>
    /// Visszaadja a játékos csapatának színét
    /// </summary>
    public net.md_5.bungee.api.ChatColor GetColorByPlayer(Player p) {

        return net.md_5.bungee.api.ChatColor.getByChar(p.getDisplayName().charAt(1));
    }

    public static Seaseon2 getInstance(){ return instance; }

    /// <summary>
    /// Visszaadja a megadott másodperceket óra:perc:másodperc formátumban
    /// </summary>
    public String SecondsToClock(int seconds){
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    /// <summary>
    /// Létrehozza a játékosnak a scoreboardot és ha már létezik akkor frissíti
    /// </summary>
    public void CreateAndUpdateScoreboard(Player player) {
        if(!boards.containsKey(player.getUniqueId())){
            FastBoard board = new FastBoard(player);

            board.updateTitle("§c♥§e♥§a♥ §fSEASON 2 §a♥§e♥§c♥");

            this.boards.put(player.getUniqueId(), board);
        }

        UpdateBoard(this.boards.get(player.getUniqueId()));
    }

    /// <summary>
    /// Törli a játékos scoreboardját
    /// </summary>
    public void DeleteScoreboard(Player player){
        if(!boards.containsKey(player.getUniqueId())) return;

        FastBoard board = this.boards.remove(player.getUniqueId());

        if (board != null) {
            board.delete();
        }
    }
}
