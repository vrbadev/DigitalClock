package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;

import cz.perwin.digitalclock.core.Clock;
import cz.perwin.digitalclock.core.CommandInfo;
import cz.perwin.digitalclock.core.Generator;

public class CommandStopclock {
	public CommandStopclock(CommandInfo info) {
		if(info.getArgs().length != 2) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Correct usage: '/"+ info.getUsedcmd() + " stopclock <name>'");
		} else if(!info.getPlayer().hasPermission("digitalclock.stopclock") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " You aren't allowed to use this command!");
		} else if(!info.getMain().getClocksConf().getKeys(false).contains(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Clock '" + info.getArgs()[1] + "' not found!");
		} else if(!info.getMain().getClockTasks().containsKey(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Clock '"+ info.getArgs()[1] +"' is already stopped!");
		} else {	
			Clock.stopTask(info.getArgs()[1]);
			info.getPlayer().sendMessage(ChatColor.DARK_GREEN + Generator.getGenerator().getMain().getMessagePrefix() + ChatColor.GREEN + " You have successfully stopped clock '" + info.getArgs()[1] + "' (task number " + Generator.getGenerator().getMain().getClockTasks().get(info.getArgs()[1]) + ").");
		}
	}
}
