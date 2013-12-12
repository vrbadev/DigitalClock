package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.Clock;
import cz.perwin.digitalclock.CommandInfo;

import org.bukkit.ChatColor;

public class CommandSettime {
	public CommandSettime(CommandInfo info) {
		if(info.getArgs().length != 3) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/"+ info.getUsedcmd() + " settime <name> <HH:MM>'");
		} else if(!info.getPlayer().hasPermission("digitalclock.settime") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
		} else if(!info.getMain().getClocksConf().getKeys(false).contains(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + info.getArgs()[1] + "' not found!");
		} else {
			String input = info.getArgs()[2];
			if(input.length() == 5 
					&& Character.isDigit(input.charAt(0))
					&& Character.isDigit(input.charAt(1))
					&& Character.isDigit(input.charAt(3))
					&& Character.isDigit(input.charAt(4))
					&& input.charAt(2) == ':') {
				int hours = Integer.parseInt(Character.toString(input.charAt(0)))*10 + Integer.parseInt(Character.toString(input.charAt(1)));
				int mins = Integer.parseInt(Character.toString(input.charAt(3)))*10 + Integer.parseInt(Character.toString(input.charAt(4)));
				if(hours >= 0 && hours < 24 && mins >= 0 && mins < 60) {
					Clock clock = Clock.loadClockByClockName(info.getArgs()[1]);
					int currentTimeInMinutes = Integer.parseInt(info.getMain().getGenerator().getRealNumbers(0, null)[0])*60 + Integer.parseInt(info.getMain().getGenerator().getRealNumbers(0, null)[1]);
					int newAddingMinutes = (currentTimeInMinutes - hours*60 - mins)*-1;
					clock.addMinutes(newAddingMinutes);
					info.getPlayer().sendMessage(ChatColor.GREEN + "[DigitalClock] Time on clock '" + info.getArgs()[1] + " is now "+ input +". To set the time back to real time just use command '/"+ info.getUsedcmd() + " addingminutes "+ info.getArgs()[1] +" 0'.");
				} else {
					info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] HH must be integer from 00 to 23 and MM must be integer from 00 to 59!");
				}
			} else {
				info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Time has incorrect format, it has to be HH:MM!");
			}
		}	
	}
}
