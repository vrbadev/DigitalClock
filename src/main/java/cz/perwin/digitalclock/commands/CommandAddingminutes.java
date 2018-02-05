package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.core.Clock;

public class CommandAddingminutes implements ICommand {
	@Override
	public int getArgsSize() {
		return 3;
	}

	@Override
	public String getPermissionName() {
		return "digitalclock.addingminutes";
	}

	@Override
	public boolean specialCondition(DigitalClock main, Player player, String[] args) {
		return false;
	}

	@Override
	public boolean checkClockExistence() {
		return true;
	}
	
	@Override
	public boolean neededClockExistenceValue() {
		return true;
	}

	@Override
	public String reactBadArgsSize(String usedCmd) {
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Correct usage: '/"+ usedCmd + " addingminutes <name> <minutes>'";
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
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Clock '" + clockName + "' not found!";
	}

	@Override
	public void process(DigitalClock main, Player player, String[] args) {
		int mins = Integer.parseInt(args[2]);
		if(mins >= 0 && mins < 1440) {
			Clock clock = Clock.loadClockByClockName(args[1]);
			clock.addMinutes(mins);
			player.sendMessage(ChatColor.DARK_GREEN + DigitalClock.getMessagePrefix() + ChatColor.GREEN + " Clock '" + args[1] + "' now will add " + args[2] + " minutes.");
		} else {
			player.sendMessage(ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Minutes can be only integer between 0 and 1439, not " + args[2] + "!");
		}
	}
}
