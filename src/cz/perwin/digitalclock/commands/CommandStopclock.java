package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.CommandInfo;

import org.bukkit.ChatColor;

public class CommandStopclock {
	public CommandStopclock(CommandInfo info) {
		if(info.getArgs().length != 2) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/"+ info.getUsedcmd() + " stopclock <name>'");
		} else if(!info.getPlayer().hasPermission("digitalclock.stopclock") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
		} else if(!info.getMain().getClocksConf().getKeys(false).contains(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + info.getArgs()[1] + "' not found!");
		} else if(!info.getMain().getClockTasks().containsKey(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Clock '"+ info.getArgs()[1] +"' is already stopped!");
		} else {	
			info.getMain().getServer().getScheduler().cancelTask(info.getMain().getClockTasks().get(info.getArgs()[1]));
			info.getPlayer().sendMessage(ChatColor.GREEN + "[DigitalClock] You have successfully stopped clock '" + info.getArgs()[1] + "' (task number " + info.getMain().getClockTasks().get(info.getArgs()[1]) + ").");
			info.getMain().getClockTasks().remove(info.getArgs()[1]);
		}
	}
}
