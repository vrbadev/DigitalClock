package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;

import cz.perwin.digitalclock.core.Clock;
import cz.perwin.digitalclock.core.CommandInfo;

public class CommandToggleseconds {
	public CommandToggleseconds(CommandInfo info) {
		if(info.getArgs().length != 2) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Correct usage: '/"+ info.getUsedcmd() + " toggleseconds <name>'");
		} else if(!info.getPlayer().hasPermission("digitalclock.toggleseconds") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " You aren't allowed to use this command!");
		} else if(!info.getMain().getClocksConf().getKeys(false).contains(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Clock '" + info.getArgs()[1] + "' not found!");
		} else {	
			Clock clock = Clock.loadClockByClockName(info.getArgs()[1]);
			if(clock.shouldShowSeconds()) {
				clock.setShowingSeconds(false);
				info.getPlayer().sendMessage(ChatColor.DARK_GREEN + info.getMain().getMessagePrefix() + ChatColor.GREEN + " You have successfully hidden seconds on clock '" + info.getArgs()[1] + "'.");
			} else {
				clock.setShowingSeconds(true);
				info.getPlayer().sendMessage(ChatColor.DARK_GREEN + info.getMain().getMessagePrefix() + ChatColor.GREEN + " You have successfully shown seconds on clock '" + info.getArgs()[1] + "'.");
			}
		}
	}
}
