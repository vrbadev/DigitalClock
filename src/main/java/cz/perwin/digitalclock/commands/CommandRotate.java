package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.core.Clock;

public class CommandRotate implements ICommand {
	@Override
	public int getArgsSize() {
		return 3;
	}

	@Override
	public String getPermissionName() {
		return "digitalclock.rotate";
	}

	@Override
	public boolean specialCondition(DigitalClock main, Player player, String[] args) {
		return !args[2].equals("north") && !args[2].equals("south") && !args[2].equals("east") && !args[2].equals("west");
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
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Correct usage: '/"+ usedCmd + " rotate <name> <direction>'";
	}

	@Override
	public String reactNoPermissions() {
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " You aren't allowed to use this command!";
	}
	
	@Override
	public void specialConditionProcess(DigitalClock main, Player player, String[] args) {
		player.sendMessage(ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Direction can be only north, south, east or west!");
	}

	@Override
	public String reactBadClockList(String clockName) {
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Clock '" + clockName + "' not found!";
	}

	@Override
	public void process(DigitalClock main, Player player, String[] args) {
		Clock clock = Clock.loadClockByClockName(args[1]);
		player.sendMessage(ChatColor.DARK_GREEN + DigitalClock.getMessagePrefix() + ChatColor.GREEN + " Your clock '" + args[1] + "' rotated successfully from " + clock.getClockArea().getDirection().name().toLowerCase() + " to " + clock.getClockArea().rotate(args[2]).name().toLowerCase());
	}
}
