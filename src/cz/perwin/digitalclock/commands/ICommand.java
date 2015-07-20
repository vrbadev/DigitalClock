package cz.perwin.digitalclock.commands;

import org.bukkit.entity.Player;

import cz.perwin.digitalclock.DigitalClock;

public interface ICommand {
	public int getArgsSize();
	public String getPermissionName();
	public boolean specialCondition(DigitalClock main, Player player, String[] args);
	public boolean checkClockExistence();
	public boolean neededClockExistenceValue();
	public String reactBadArgsSize(String usedCmd);
	public String reactNoPermissions();
	public String reactBadClockList(String clockName);
	public void process(DigitalClock main, Player player, String[] args);
	public void specialConditionProcess(DigitalClock main, Player player, String[] args);
}
