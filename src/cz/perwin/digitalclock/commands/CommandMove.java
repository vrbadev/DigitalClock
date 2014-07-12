package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;

import cz.perwin.digitalclock.core.CommandInfo;

public class CommandMove {
	public CommandMove(CommandInfo info) {
		if(info.getArgs().length != 2) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Correct usage: '/"+ info.getUsedcmd() + " move <name>'");
		} else if(!info.getPlayer().hasPermission("digitalclock.move") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " You aren't allowed to use this command!");
		} else if(!info.getMain().getClocksConf().getKeys(false).contains(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Clock '" + info.getArgs()[1] + "' not found!");
		} else if(info.getMain().getEnableMoveUsers().containsKey(info.getPlayer())) {
			info.getPlayer().sendMessage(ChatColor.DARK_GREEN + info.getMain().getMessagePrefix() + ChatColor.GREEN + " Moving clock '" + info.getArgs()[1] + "' has been rejected!");
			info.getMain().getEnableMoveUsers().remove(info.getPlayer());
		} else {	
			info.getMain().getEnableMoveUsers().put(info.getPlayer(), info.getArgs()[1]);
			info.getPlayer().sendMessage(ChatColor.DARK_GREEN + info.getMain().getMessagePrefix() + ChatColor.GREEN + " Moving clock '" + info.getArgs()[1] + "' has been enabled. Now just right click to some place to move your clock there.");
		}
	}
}
