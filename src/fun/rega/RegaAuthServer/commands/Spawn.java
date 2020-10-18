package fun.rega.RegaAuthServer.commands;


import fun.rega.RegaAuthServer.*;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fun.rega.RegaAuthServer.utils.Spawn_API;


public class Spawn implements CommandExecutor {
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    Player p = (Player)sender;
    if (cmd.getName().equalsIgnoreCase("spawn"))
      if (args.length == 0) {
        if (Spawn_API.locationExists("spawn").booleanValue()) {
          Location loc = Spawn_API.getLocation("spawn");
          p.teleport(loc);
        } else {
          p.sendMessage(String.valueOf(Main.prefix) + " §cТочка спавна еще не установлена!");
          p.closeInventory();
        } 
      } else {
        p.sendMessage(String.valueOf(Main.prefix) + " §cПравильное использование: §f/spawn");
      }  
    return true;
  }
}
