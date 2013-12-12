package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.Clock;
import cz.perwin.digitalclock.CommandInfo;

import org.bukkit.ChatColor;

public class CommandRotate {
	public CommandRotate(CommandInfo info) {
		if(info.getArgs().length != 3) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/"+ info.getUsedcmd() + " rotate <name> <direction>'");
		} else if(!info.getPlayer().hasPermission("digitalclock.rotate") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
		} else if(!info.getMain().getClocksConf().getKeys(false).contains(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + info.getArgs()[1] + "' not found!");
		} else if(!info.getArgs()[2].equals("north") && !info.getArgs()[2].equals("south") && !info.getArgs()[2].equals("east") && !info.getArgs()[2].equals("west")) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Direction can be only north, south, east or west!");
		} else {	
			Clock clock = Clock.loadClockByClockName(info.getArgs()[1]);
			info.getPlayer().sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + info.getArgs()[1] + "' rotated successfully from " + clock.getDirection().name().toLowerCase() + " to " + clock.rotate(info.getArgs()[2]).name().toLowerCase());
		}
	}
}
