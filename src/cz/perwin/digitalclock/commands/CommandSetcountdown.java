package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.core.Clock;
import cz.perwin.digitalclock.core.ClockMode;

public class CommandSetcountdown implements ICommand {
	@Override
	public int getArgsSize() {
		return 3;
	}

	@Override
	public String getPermissionName() {
		return "digitalclock.setcountdown";
	}

	@Override
	public boolean specialCondition(DigitalClock main, Player player, String[] args) {
		ClockMode cm = Clock.loadClockByClockName(args[1]).getClockMode();
		return cm == ClockMode.COUNTDOWN || cm == ClockMode.STOPWATCH;
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
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Correct usage: '/" + usedCmd + " setcountdown <name> <seconds>'";
	}

	@Override
	public String reactNoPermissions() {
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " You aren't allowed to use this command!";
	}

	@Override
	public void specialConditionProcess(DigitalClock main, Player player, String[] args) {
		player.sendMessage(ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Clock '" + args[1] + "' has already enabled countdown or stopwatch mode! Disable it with command 'disablecountdown <name>' or 'disablestopwatch <name>'.");
	}

	@Override
	public String reactBadClockList(String clockName) {
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Clock '" + clockName + "' not found!";
	}

	@Override
	public void process(DigitalClock main, Player player, String[] args) {
		if(!(Integer.parseInt(args[2]) < 360000 && Integer.parseInt(args[2]) > 0)) {
			player.sendMessage(ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Seconds must be number between 0 and 359999!");
		} else {
			Clock clock = Clock.loadClockByClockName(args[1]);
			clock.setCountdownTime(Integer.parseInt(args[2]));
			clock.enableCountdown(true);
			if(main.getClockTasks().containsKey(args[1])) {
				main.getServer().getScheduler().cancelTask(main.getClockTasks().get(args[1]));
				main.getClockTasks().remove(args[1]);
			}
			String[] num = main.getGenerator().getNumbersFromSeconds(clock.getCountdownTime());
			String hours = num[0];
			String minutes = num[1];
			String seconds = num[2];
			main.getGenerator().generatingSequence(clock, hours, minutes, seconds, null);
			player.sendMessage(ChatColor.DARK_GREEN + DigitalClock.getMessagePrefix() + ChatColor.GREEN + " You have successfully set countdown time on clock '" + args[1] + "' to "+ hours +":"+ minutes +":"+ seconds +". This clock is now stopped, run it by command 'runclock <name>'.");
		}
	}
}
