package fun.rega.RegaAuthServer.utils;


import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;


public class Spawn_API {
  public static File file = new File("plugins/RegaAuthServer", "spawn.yml");
  
  public static FileConfiguration cfg = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
  
  public static void setLocation(Location loc, String name) {
    String world = loc.getWorld().getName();
    double x = loc.getX();
    double y = loc.getY();
    double z = loc.getZ();
    double yaw = loc.getYaw();
    double pitch = loc.getPitch();
    cfg.set(String.valueOf(name) + ".world", world);
    cfg.set(String.valueOf(name) + ".x", Double.valueOf(x));
    cfg.set(String.valueOf(name) + ".y", Double.valueOf(y));
    cfg.set(String.valueOf(name) + ".z", Double.valueOf(z));
    cfg.set(String.valueOf(name) + ".yaw", Double.valueOf(yaw));
    cfg.set(String.valueOf(name) + ".pitch", Double.valueOf(pitch));
    try {
      cfg.save(file);
    } catch (IOException e) {
      System.err.println("Местоположение " + name + " не удалось сохранить.");
      e.printStackTrace();
    } 
  }
  
  public static Location getLocation(String name) {
    String world = cfg.getString(String.valueOf(name) + ".world");
    double x = cfg.getDouble(String.valueOf(name) + ".x");
    double y = cfg.getDouble(String.valueOf(name) + ".y");
    double z = cfg.getDouble(String.valueOf(name) + ".z");
    double yaw = cfg.getDouble(String.valueOf(name) + ".yaw");
    double pitch = cfg.getDouble(String.valueOf(name) + ".pitch");
    Location loc = new Location(Bukkit.getWorld(world), x, y, z);
    loc.setYaw((float)yaw);
    loc.setPitch((float)pitch);
    return loc;
  }
  
  public static Boolean locationExists(String name) {
    if (cfg.contains(name))
      return Boolean.valueOf(true); 
    return Boolean.valueOf(false);
  }
  

 }
