package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.perwin.digitalclock.Version;
import cz.perwin.digitalclock.core.CommandInfo;

public class CommandUpdate {
	public CommandUpdate(CommandInfo info, CommandSender sender) {
		if(info.getArgs().length != 1) {
			sender.sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Correct usage: '/"+ info.getUsedcmd() + " reload'");
		} else if(sender instanceof Player && !info.getPlayer().hasPermission("digitalclock.update") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " You aren't allowed to use this command!");
		} else {	
			Version.update(sender, info.getMain().getMessagePrefix(), info.getMain().getDescription().getVersion(), info.getMain().getDescription().getName());
		}
	}
}
