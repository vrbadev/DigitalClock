package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.Clock;
import cz.perwin.digitalclock.CommandInfo;

import org.bukkit.ChatColor;

public class CommandToggleampm {
	public CommandToggleampm(CommandInfo info) {
		if(info.getArgs().length != 2) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/"+ info.getUsedcmd() + " toggleampm <name>'");
		} else if(!info.getPlayer().hasPermission("digitalclock.toggleampm") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
		} else if(!info.getMain().getClocksConf().getKeys(false).contains(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + info.getArgs()[1] + "' not found!");
		} else {	
			Clock clock = Clock.loadClockByClockName(info.getArgs()[1]);
			if(clock.getAMPM()) {
				clock.setAMPM(false);
				info.getPlayer().sendMessage(ChatColor.GREEN + "[DigitalClock] You have successfully turned AM/PM OFF on clock '" + info.getArgs()[1] + "'.");
			} else {
				clock.setAMPM(true);
				info.getPlayer().sendMessage(ChatColor.GREEN + "[DigitalClock] You have successfully turned AM/PM ON on clock '" + info.getArgs()[1] + "'.");
			}
		}
	}
}
