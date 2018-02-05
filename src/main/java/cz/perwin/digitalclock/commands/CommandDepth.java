package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.core.Clock;

public class CommandDepth implements ICommand {
	@Override
	public int getArgsSize() {
		return 3;
	}

	@Override
	public String getPermissionName() {
		return "digitalclock.depth";
	}

	@Override
	public boolean specialCondition(DigitalClock main, Player player, String[] args) {
		try {
			int i = Integer.parseInt(args[2]);
			if(i > 0) {
				return false;
			} else {
				return true;
			}
		} catch(Exception e) {
			return true;
		}
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
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Correct usage: '/"+ usedCmd + " depth <name> <depth>'";
	}

	@Override
	public String reactNoPermissions() {
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " You aren't allowed to use this command!";
	}

	@Override
	public void specialConditionProcess(DigitalClock main, Player player, String[] args) {
		player.sendMessage(ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Depth must be positive integer bigger than 0!");
	}

	@Override
	public String reactBadClockList(String clockName) {
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Clock '" + clockName + "' not found!";
	}

	@Override
	public void process(DigitalClock main, Player player, String[] args) {
		Clock clock = Clock.loadClockByClockName(args[1]);
		int de = Integer.parseInt(args[2]);
		clock.getClockArea().setDepth(de);
		player.sendMessage(ChatColor.DARK_GREEN + DigitalClock.getMessagePrefix() + ChatColor.GREEN + " Your clock '" + args[1] + "' changed depth to " + de + " blocks.");
	}
}
