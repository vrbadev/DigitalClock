package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.Clock;
import cz.perwin.digitalclock.CommandInfo;

import org.bukkit.ChatColor;

public class CommandAddingminutes {
	public CommandAddingminutes(CommandInfo info) {
		if(info.getArgs().length != 3) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/"+ info.getUsedcmd() + " addingminutes <name> <minutes>'");
		} else if(!info.getPlayer().hasPermission("digitalclock.addingminutes") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
		} else if(!info.getMain().getClocksConf().getKeys(false).contains(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + info.getArgs()[1] + "' not found!");
		} else {
			int mins = Integer.parseInt(info.getArgs()[2]);
			if(mins >= 0 && mins < 1440) {
				Clock clock = Clock.loadClockByClockName(info.getArgs()[1]);
				clock.addMinutes(mins);
				info.getPlayer().sendMessage(ChatColor.GREEN + "[DigitalClock] Clock '" + info.getArgs()[1] + "' now will add " + info.getArgs()[2] + " minutes.");
			} else {
				info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Minutes can be only integer between 0 and 1439, not " + info.getArgs()[2] + "!");
			}
		}	
	}
}
