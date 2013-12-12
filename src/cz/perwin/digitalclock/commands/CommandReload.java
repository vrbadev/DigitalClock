package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.CommandInfo;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandReload {
	public CommandReload(CommandInfo info, CommandSender sender) {
		if(info.getArgs().length != 1) {
			sender.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/"+ info.getUsedcmd() + " reload'");
		} else if(sender instanceof Player && !info.getPlayer().hasPermission("digitalclock.reload") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
		} else {	
			info.getMain().reloadConf();
			sender.sendMessage(ChatColor.GREEN + "[DigitalClock] File config.yml has been reloaded!");
		}
	}
}
