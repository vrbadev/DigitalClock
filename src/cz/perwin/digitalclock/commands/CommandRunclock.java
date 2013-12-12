package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.CommandInfo;

import org.bukkit.ChatColor;

public class CommandRunclock {
	public CommandRunclock(CommandInfo info) {
		if(info.getArgs().length != 2) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/"+ info.getUsedcmd() + " runclock <name>'");
		} else if(!info.getPlayer().hasPermission("digitalclock.runclock") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
		} else if(!info.getMain().getClocksConf().getKeys(false).contains(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + info.getArgs()[1] + "' not found!");
		} else if(info.getMain().getClockTasks().containsKey(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Clock '"+ info.getArgs()[1] +"' is already running!");
		} else {	
			info.getMain().runClock(info.getArgs()[1]);
			info.getPlayer().sendMessage(ChatColor.GREEN + "[DigitalClock] Clock '" + info.getArgs()[1] + "' is now running under task number " + info.getMain().getClockTasks().get(info.getArgs()[1]) + ".");
		}
	}
}
