package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;

import cz.perwin.digitalclock.core.CommandInfo;

public class CommandCreate {
	public CommandCreate(CommandInfo info) {
		if(info.getArgs().length != 2) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Correct usage: '/"+ info.getUsedcmd() + " create <name>'");
		} else if(!info.getPlayer().hasPermission("digitalclock.create") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " You aren't allowed to use this command!");
		} else if(info.getMain().getEnableBuildUsers().containsKey(info.getPlayer())) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " You are just creating another clock. You can't create more clocks in the same time!");
		} else if(info.getMain().getClocksConf().getKeys(false).contains(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Clock with this name already exists!");
		} else {
			int count = 0;
			if(info.getMain().getUsersClock().get(info.getPlayer().getName()) != null) {
				count = info.getMain().getUsersClock().get(info.getPlayer().getName());
			}
			boolean limitReached = false;
			if(info.getPlayer().hasPermission("digitalclock.limit." + count) && count != 0 && !info.getPlayer().isOp() && !info.getPlayer().hasPermission("digitalclock.limit.*")) {
				limitReached = true;
			}
			if(limitReached == false) {
		    	info.getMain().getEnableBuildUsers().put(info.getPlayer(), info.getArgs()[1]);
		      	info.getPlayer().sendMessage(ChatColor.DARK_GREEN + info.getMain().getMessagePrefix() + ChatColor.GREEN + " Now you can create your " + (count+1) + ". clock. Place any block anywhere to set start block. Click with empty hand to end creating.");
			} else {
		      	info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " You can't create next clock. You have reached the limit of " + count + " clocks.");
			}
		}
	}
}
