package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;

import cz.perwin.digitalclock.core.Clock;
import cz.perwin.digitalclock.core.CommandInfo;

public class CommandList {
	public CommandList(CommandInfo info) {
		if(info.getArgs().length != 1) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Correct usage: '/"+ info.getUsedcmd() + " list'");
		} else if(!info.getPlayer().hasPermission("digitalclock.list") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " You aren't allowed to use this command!");
		} else {	
			info.getPlayer().sendMessage(ChatColor.DARK_GREEN + info.getMain().getMessagePrefix() + ChatColor.GREEN + " List of all existing clocks:");
			String list = "";
			int i = 0;
			if(info.getMain().getClocksL().size() != 0) {
				for(String name : info.getMain().getClocksL()) {
					Clock clock = Clock.loadClockByClockName(name);
					list += clock.getName();
					if(i != info.getMain().getClocksL().size()-1) {
						list += ", ";
					}
					i++;
				}
			} else {
				list = ChatColor.ITALIC + "No clocks found!";
			}
			info.getPlayer().sendMessage(ChatColor.GREEN + list);
		}
	}
}
