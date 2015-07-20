package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.core.Commands;

public class CommandHelp implements ICommand {
	@Override
	public int getArgsSize() {
		return 1;
	}

	@Override
	public String getPermissionName() {
		return "digitalclock.help";
	}

	@Override
	public boolean specialCondition(DigitalClock main, Player player, String[] args) {
		return false;
	}

	@Override
	public boolean checkClockExistence() {
		return false;
	}
	
	@Override
	public boolean neededClockExistenceValue() {
		return false;
	}

	@Override
	public String reactBadArgsSize(String usedCmd) {
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Correct usage: '/"+ usedCmd + " help'";
	}

	@Override
	public String reactNoPermissions() {
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " You aren't allowed to use this command!";
	}

	@Override
	public void specialConditionProcess(DigitalClock main, Player player, String[] args) {
		return;
	}

	@Override
	public String reactBadClockList(String clockName) {
		return null;
	}

	@Override
	public void process(DigitalClock main, Player player, String[] args) {
		String s = ChatColor.DARK_GREEN + DigitalClock.getMessagePrefix() + ChatColor.GREEN + " All possible arguments:\n";
		s+= ChatColor.GREEN + "/dc ";
		int i = 0;
		for(String cmd : Commands.commandList.keySet()) {
			if(i == Commands.commandList.size()-2) {
				s += ChatColor.DARK_GREEN + cmd + ChatColor.GREEN + " and ";
			} else if(i == Commands.commandList.size()-1) {
				s += ChatColor.DARK_GREEN + cmd + ChatColor.GREEN + ".\n";
			} else {
				s += ChatColor.DARK_GREEN + cmd + ChatColor.GREEN + ", ";
			}
			i++;
		}
		s += "You can find more information on\n" + ChatColor.BLUE + "http://dev.bukkit.org/server-mods/digitalclock";
		if(player == null) {
			System.out.println(s);
		} else {
			player.sendMessage(s);
		}
	}
}
