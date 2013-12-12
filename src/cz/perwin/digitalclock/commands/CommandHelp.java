package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.CommandInfo;

import org.bukkit.ChatColor;

public class CommandHelp {
	public CommandHelp(CommandInfo info) {
		if(info.getArgs().length != 1) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/"+ info.getUsedcmd() + " help'");
		} else if(!info.getPlayer().hasPermission("digitalclock.help") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
		} else {	
			info.getPlayer().sendMessage(ChatColor.GREEN + "[DigitalClock] All possible arguments:");
			info.getPlayer().sendMessage(ChatColor.GREEN + "/"+ info.getUsedcmd() + " " + 
					ChatColor.DARK_GREEN + "create" + ChatColor.GREEN + ", " + 
					ChatColor.DARK_GREEN + "remove" + ChatColor.GREEN + ", " + 
					ChatColor.DARK_GREEN + "rotate" + ChatColor.GREEN + ", " + 
					ChatColor.DARK_GREEN + "material" + ChatColor.GREEN + ", " + 
					ChatColor.DARK_GREEN + "fill" + ChatColor.GREEN + ", " + 
					ChatColor.DARK_GREEN + "addingminutes" + ChatColor.GREEN + ", " + 
					ChatColor.DARK_GREEN + "settime" + ChatColor.GREEN + ", " +
					ChatColor.DARK_GREEN + "move" + ChatColor.GREEN + ", " + 
					ChatColor.DARK_GREEN + "setcountdown" + ChatColor.GREEN + ", " + 
					ChatColor.DARK_GREEN + "disablecountdown" + ChatColor.GREEN + ", " + 
					ChatColor.DARK_GREEN + "toggleampm" + ChatColor.GREEN + ", " + 
					ChatColor.DARK_GREEN + "toggleseconds" + ChatColor.GREEN + ", " + 
					ChatColor.DARK_GREEN + "toggleblinking" + ChatColor.GREEN + ", " + 
					ChatColor.DARK_GREEN + "toggleingametime" + ChatColor.GREEN + ", " + 
					ChatColor.DARK_GREEN + "tp" + ChatColor.GREEN + ", " + 
					ChatColor.DARK_GREEN + "stopclock" + ChatColor.GREEN + ", " + 
					ChatColor.DARK_GREEN + "runclock" + ChatColor.GREEN + ", " + 
					ChatColor.DARK_GREEN + "list" + ChatColor.GREEN + ", " + 
					ChatColor.DARK_GREEN + "reload" + ChatColor.GREEN + " and " + 
					ChatColor.DARK_GREEN + "help" + ChatColor.GREEN + ".\nYou can find more information on\n" + ChatColor.BLUE + "http://dev.bukkit.org/server-mods/digitalclock");
		}
	}
}
