package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.Clock;
import cz.perwin.digitalclock.CommandInfo;

import org.bukkit.ChatColor;

public class CommandRemove {
	public CommandRemove(CommandInfo info) {
		if(info.getArgs().length != 2) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/"+ info.getUsedcmd() + " remove <name>'");
		} else if(!info.getPlayer().hasPermission("digitalclock.remove") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
		} else if(!info.getMain().getClocksConf().getKeys(false).contains(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + info.getArgs()[1] + "' not found!");
		} else {		
			Clock clock = Clock.loadClockByClockName(info.getArgs()[1]);
			Clock.remove(clock);
			info.getMain().getClocks();
			info.getPlayer().sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + info.getArgs()[1] + "' has been successfully removed.");
		}
	}
}
