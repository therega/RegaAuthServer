package fun.rega.RegaAuthServer.commands;


import fun.rega.RegaAuthServer.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fun.rega.RegaAuthServer.utils.Spawn_API;


public class SetSpawn implements CommandExecutor {
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    Player p = (Player)sender;
    if (p.hasPermission("system.setspawn")) {
      if (args.length == 0) {
        Spawn_API.setLocation(p.getLocation(), "spawn");
        p.sendMessage(String.valueOf(Main.prefix) + " §aТочка спавна успешно установлена!");
      } else {
        p.sendMessage(String.valueOf(Main.prefix) + " §cПравильное использование: §6/setspawn");
      } 
    } else {
      p.sendMessage(Main.noPerm);
    } 
    return false;
  }
}
