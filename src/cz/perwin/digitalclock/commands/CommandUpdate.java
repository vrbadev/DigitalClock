package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.Version;

public class CommandUpdate implements ICommand {
	@Override
	public int getArgsSize() {
		return 1;
	}

	@Override
	public String getPermissionName() {
		return "digitalclock.update";
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
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Correct usage: '/"+ usedCmd + " update'";
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
		Version.update(player, DigitalClock.getMessagePrefix(), main.getDescription().getVersion(), main.getDescription().getName());
	}
}
