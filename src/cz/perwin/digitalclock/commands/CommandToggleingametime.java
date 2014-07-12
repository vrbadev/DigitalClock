package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;

import cz.perwin.digitalclock.core.Clock;
import cz.perwin.digitalclock.core.ClockMode;
import cz.perwin.digitalclock.core.CommandInfo;

public class CommandToggleingametime {
	public CommandToggleingametime(CommandInfo info) {
		if(info.getArgs().length != 2) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Correct usage: '/"+ info.getUsedcmd() + " toggleingametime <name>'");
		} else if(!info.getPlayer().hasPermission("digitalclock.toggleingametime") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " You aren't allowed to use this command!");
		} else if(!info.getMain().getClocksConf().getKeys(false).contains(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Clock '" + info.getArgs()[1] + "' not found!");
		} else {	
			Clock clock = Clock.loadClockByClockName(info.getArgs()[1]);
			if(clock.getClockMode() == ClockMode.INGAMETIME) {
				clock.enableIngameTime(false);
				info.getPlayer().sendMessage(ChatColor.DARK_GREEN + info.getMain().getMessagePrefix() + ChatColor.GREEN + " You have successfully disabled ingame time on clock '" + info.getArgs()[1] + "'.");
			} else {
				clock.enableIngameTime(true);
				info.getPlayer().sendMessage(ChatColor.DARK_GREEN + info.getMain().getMessagePrefix() + ChatColor.GREEN + " You have successfully enabled ingame time on clock '" + info.getArgs()[1] + "'.");
			}
		}
	}
}
