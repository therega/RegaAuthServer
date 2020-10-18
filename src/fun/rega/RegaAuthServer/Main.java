package fun.rega.RegaAuthServer;


import org.bukkit.plugin.java.*;
import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import java.util.*;
import org.bukkit.configuration.file.*;
import org.bukkit.potion.*;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.entity.*;
import org.bukkit.event.block.*;
import org.bukkit.event.weather.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import fun.rega.RegaAuthServer.utils.*;
import fun.rega.RegaAuthServer.commands.*;


public class Main extends JavaPlugin implements Listener
{
    private static Main p;
    private boolean protocolLib;
    public static ArrayList<String> allow_cmd;
    public static String msg;
    public static String prefix = "§8[§cRegaSpawn§8]";
    public static String noPerm = String.valueOf(prefix) + " §cÓ âàñ íåò íà ýòî ïðàâ!";
    
    public void onEnable() {
    	getCommand("setspawn").setExecutor((CommandExecutor)new SetSpawn());
        getCommand("spawn").setExecutor((CommandExecutor)new Spawn());
        this.saveDefaultConfig();
        loadConfig(this.getConfig());
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)this);
        if (this.getConfig().getBoolean("Time.fixed")) {
            for (final World w : Bukkit.getServer().getWorlds()) {
                w.setTime(this.getConfig().getLong("Time.meaning"));
                w.setGameRuleValue("doDaylightCycle", "false");
                                                     
            }        
        
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "[" + ChatColor.RED + "RegaAuthAddon" + ChatColor.GRAY + "] " + ChatColor.GREEN + "Plugin enabled!");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "[" + ChatColor.RED + "RegaAuthAddon" + ChatColor.GRAY + "] " + ChatColor.GREEN + "My site - " + ChatColor.RED + "www.rega.fun");
        this.getLogger().info("Plugin successfully initialized.");
        try {
            Class.forName("com.comphenix.protocol.ProtocolLibrary");
            this.protocolLib = true;
            this.getLogger().info("ProtocolLib found, hiding players with its API");
        }
        catch (ClassNotFoundException ignore) {
            this.getServer().getPluginManager().registerEvents((Listener)new Listener() {
                @EventHandler
                public void onJoin(final PlayerJoinEvent e) {
                    Main.this.getServer().getOnlinePlayers().forEach(player -> {
                        player.hidePlayer(e.getPlayer());
                        e.getPlayer().hidePlayer(player);
                    });
                }
            }, (Plugin)this);
            this.getLogger().info("ProtocolLib not found, using local player-hide API");
        }
        if (this.protocolLib) {
            ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this, new PacketType[] { PacketType.Play.Server.PLAYER_INFO }) {
                public void onPacketSending(final PacketEvent e) {
                    e.setCancelled(true);
                }
            });
            
        }
            
        }
    }
    
    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent e) {
    Player p = e.getPlayer();
    
    Bukkit.dispatchCommand(p, "spawn");
    
    }
    
    public static void loadConfig(final FileConfiguration cfg) {
        Main.msg = replacer(cfg.getString("Commands.msg"));
        Main.allow_cmd = (ArrayList<String>)cfg.getStringList("Commands.allow_cmd");
    }
    
    public void onDisable() {
        this.getLogger().info("Plugin successfully disabled.");
    }
    
    @EventHandler
    public void onFoodLevel(final FoodLevelChangeEvent e) {
        if (this.getConfig().getBoolean("Settings.noHunger")) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        p.getActivePotionEffects().clear();
        p.setLevel(0);
        if (this.getConfig().getBoolean("Settings.noMessage")) {
            e.setJoinMessage(null);
        }
        if (this.getConfig().getBoolean("Effects.Invisibility")) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 1));
        }
        if (this.getConfig().getBoolean("Effects.Speed")) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
        }
        if (this.getConfig().getBoolean("LvlonJoin.enabled")) {
            p.setLevel(this.getConfig().getInt("LvlonJoin.amount"));
        }
        final String gm = this.getConfig().getString("Settings.GameMode");
        if (gm != null) {
            p.setGameMode(GameMode.valueOf(gm));
        }
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        if (this.getConfig().getBoolean("Settings.noMessage")) {
            e.setQuitMessage(null);
        }
    }
    
    @EventHandler
    public void noDeath(final PlayerDeathEvent e) {
        if (this.getConfig().getBoolean("Settings.noMessage")) {
            e.setDeathMessage(null);
        }
    }
    
    @EventHandler
    public void noDamage(final EntityDamageByEntityEvent e) {
        if (this.getConfig().getBoolean("Settings.noDamage") && !e.getDamager().hasPermission(this.getConfig().getString("RegaAuthServer.bypassPvP"))) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void noDamage2(final EntityDamageEvent e) {
        if (this.getConfig().getBoolean("Settings.noDamage")) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void noBreak(final BlockBreakEvent e) {
        if (!e.getPlayer().hasPermission(this.getConfig().getString("RegaAuthServer.bypassBreak")) && this.getConfig().getBoolean("Settings.noBreak")) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void noPlace(final BlockPlaceEvent e) {
        if (!e.getPlayer().hasPermission(this.getConfig().getString("RegaAuthServer.bypassPlace")) && this.getConfig().getBoolean("Settings.noPlace")) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void noFire(final BlockBurnEvent e) {
        if (this.getConfig().getBoolean("Settings.noFire")) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void noFire(final BlockSpreadEvent e) {
        if (this.getConfig().getBoolean("Settings.noFire")) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onExplode(final EntityExplodeEvent e) {
        if (this.getConfig().getBoolean("Settings.noExplosions")) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onLeaf(final LeavesDecayEvent e) {
        if (this.getConfig().getBoolean("Settings.noLeafDecay")) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlace(final BlockPlaceEvent e) {
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onBreak(final BlockBreakEvent e) {
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onDamage(final EntityDamageEvent e) {
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onDamage(final FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void DisableWeather(final WeatherChangeEvent e) {
        e.setCancelled(e.toWeatherState());
    }
    
    @EventHandler
    public void onDrop(final PlayerDropItemEvent e) {
        e.setCancelled(true);
    }
    
    public static String replacer(final String what) {
        return ChatColor.translateAlternateColorCodes('&', what);
    }
    
    @EventHandler
    public void onChat(final AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        e.getPlayer().sendMessage(Main.msg);
    }
    
    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent e) {
        if (!Main.allow_cmd.contains(e.getMessage().split(" ")[0].replace("/", ""))) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(Main.msg);
        }
        
    }   
    
    
}