package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.CommandInfo;

import org.bukkit.ChatColor;

public class CommandCreate {
	public CommandCreate(CommandInfo info) {
		if(info.getArgs().length != 2) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/"+ info.getUsedcmd() + " create <name>'");
		} else if(!info.getPlayer().hasPermission("digitalclock.create") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
		} else if(info.getMain().getEnableBuildUsers().containsKey(info.getPlayer())) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] You are just creating another clock. You can't create more clocks in the same time!");
		} else if(info.getMain().getClocksConf().getKeys(false).contains(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Clock with i name already exists!");
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
		      	info.getPlayer().sendMessage(ChatColor.GREEN + "[DigitalClock] Now you can create your " + (count+1) + ". clock. Click with any block anywhere to set start block.");
			} else {
		      	info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] You can't create next clock. You have reached the limit of " + count + " clocks.");
			}
		}
	}
}
