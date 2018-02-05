package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import cz.perwin.digitalclock.DigitalClock;

public class CommandRunclock implements ICommand {
	@Override
	public int getArgsSize() {
		return 2;
	}

	@Override
	public String getPermissionName() {
		return "digitalclock.runclock";
	}

	@Override
	public boolean specialCondition(DigitalClock main, Player player, String[] args) {
		return main.getClockTasks().containsKeyByClockName(args[1]);
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
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Correct usage: '/" + usedCmd + " runclock <name>'";
	}

	@Override
	public String reactNoPermissions() {
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " You aren't allowed to use this command!";
	}

	@Override
	public void specialConditionProcess(DigitalClock main, Player player, String[] args) {
		player.sendMessage(ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Clock '"+ args[1] +"' is already running!");
	}

	@Override
	public String reactBadClockList(String clockName) {
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Clock '" + clockName + "' not found!";
	}

	@Override
	public void process(DigitalClock main, Player player, String[] args) {
		main.run(args[1]);
		player.sendMessage(ChatColor.DARK_GREEN + DigitalClock.getMessagePrefix() + ChatColor.GREEN + " Clock '" + args[1] + "' is now running under task number " + main.getClockTasks().getByClockName(args[1]) + ".");
	}
}
