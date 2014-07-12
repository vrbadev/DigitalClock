package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;

import cz.perwin.digitalclock.core.Clock;
import cz.perwin.digitalclock.core.CommandInfo;

public class CommandRotate {
	public CommandRotate(CommandInfo info) {
		if(info.getArgs().length != 3) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Correct usage: '/"+ info.getUsedcmd() + " rotate <name> <direction>'");
		} else if(!info.getPlayer().hasPermission("digitalclock.rotate") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " You aren't allowed to use this command!");
		} else if(!info.getMain().getClocksConf().getKeys(false).contains(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Clock '" + info.getArgs()[1] + "' not found!");
		} else if(!info.getArgs()[2].equals("north") && !info.getArgs()[2].equals("south") && !info.getArgs()[2].equals("east") && !info.getArgs()[2].equals("west")) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Direction can be only north, south, east or west!");
		} else {	
			Clock clock = Clock.loadClockByClockName(info.getArgs()[1]);
			info.getPlayer().sendMessage(ChatColor.DARK_GREEN + info.getMain().getMessagePrefix() + ChatColor.GREEN + " Your clock '" + info.getArgs()[1] + "' rotated successfully from " + clock.getClockArea().getDirection().name().toLowerCase() + " to " + clock.getClockArea().rotate(info.getArgs()[2]).name().toLowerCase());
		}
	}
}
