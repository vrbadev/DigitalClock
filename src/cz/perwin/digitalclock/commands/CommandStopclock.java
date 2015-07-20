package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.core.Clock;
import cz.perwin.digitalclock.core.Generator;

public class CommandStopclock implements ICommand {
	@Override
	public int getArgsSize() {
		return 2;
	}

	@Override
	public String getPermissionName() {
		return "digitalclock.stopclock";
	}

	@Override
	public boolean specialCondition(DigitalClock main, Player player, String[] args) {
		return !main.getClockTasks().containsKey(args[1]);
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
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Correct usage: '/" + usedCmd + " stopclock <name>'";
	}

	@Override
	public String reactNoPermissions() {
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " You aren't allowed to use this command!";
	}

	@Override
	public void specialConditionProcess(DigitalClock main, Player player, String[] args) {
		player.sendMessage(ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Clock '"+ args[1] +"' is already stopped!");
	}

	@Override
	public String reactBadClockList(String clockName) {
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Clock '" + clockName + "' not found!";
	}

	@Override
	public void process(DigitalClock main, Player player, String[] args) {
		int task = Clock.stopTask(args[1]);
		Generator.getGenerator().getMain();
		player.sendMessage(ChatColor.DARK_GREEN + DigitalClock.getMessagePrefix() + ChatColor.GREEN + " You have successfully stopped clock '" + args[1] + "' (task number " + task + ").");
	}
}
