package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.CommandInfo;

import org.bukkit.ChatColor;

public class CommandMove {
	public CommandMove(CommandInfo info) {
		if(info.getArgs().length != 2) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/"+ info.getUsedcmd() + " move <name>'");
		} else if(!info.getPlayer().hasPermission("digitalclock.move") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
		} else if(!info.getMain().getClocksConf().getKeys(false).contains(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + info.getArgs()[1] + "' not found!");
		} else if(info.getMain().getEnableMoveUsers().containsKey(info.getPlayer())) {
			info.getPlayer().sendMessage(ChatColor.GREEN + "[DigitalClock] Moving clock '" + info.getArgs()[1] + "' has been rejected!");
			info.getMain().getEnableMoveUsers().remove(info.getPlayer());
		} else {	
			info.getMain().getEnableMoveUsers().put(info.getPlayer(), info.getArgs()[1]);
			info.getPlayer().sendMessage(ChatColor.GREEN + "[DigitalClock] Moving clock '" + info.getArgs()[1] + "' has been enabled. Now just right click to some place to move your clock there.");
		}
	}
}
