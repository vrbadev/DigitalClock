package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.core.Clock;
import cz.perwin.digitalclock.core.ClockMode;

public class CommandDisablecountdown implements ICommand {
	@Override
	public int getArgsSize() {
		return 2;
	}

	@Override
	public String getPermissionName() {
		return "digitalclock.disablecountdown";
	}

	@Override
	public boolean specialCondition(DigitalClock main, Player player, String[] args) {
		return Clock.loadClockByClockName(args[1]).getClockMode() != ClockMode.COUNTDOWN;
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
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Correct usage: '/"+ usedCmd + " disablecountdown <name>'";
	}

	@Override
	public String reactNoPermissions() {
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " You aren't allowed to use this command!";
	}

	@Override
	public void specialConditionProcess(DigitalClock main, Player player, String[] args) {
		player.sendMessage(ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " This clock hasn't enabled countdown mode!");
	}

	@Override
	public String reactBadClockList(String clockName) {
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Clock '" + clockName + "' not found!";
	}

	@Override
	public void process(DigitalClock main, Player player, String[] args) {
		Clock clock = Clock.loadClockByClockName(args[1]);
		clock.enableCountdown(false);
		if(main.getClockTasks().containsKey(args[1])) {
			main.getServer().getScheduler().cancelTask(main.getClockTasks().get(args[1]));
			main.getClockTasks().remove(args[1]);
		}
		String hours = main.getGenerator().getRealNumbers(clock.getAddMinutes(), null)[0];
		String minutes = main.getGenerator().getRealNumbers(clock.getAddMinutes(), null)[1];
		String seconds = main.getGenerator().getRealNumbers(clock.getAddMinutes(), null)[2];
		main.getGenerator().generatingSequence(clock, hours, minutes, seconds, null);
		player.sendMessage(ChatColor.DARK_GREEN + DigitalClock.getMessagePrefix() + ChatColor.GREEN + " You have successfully disabled countdown mode on clock '" + args[1] + "'. This clock is now stopped, run it by command 'runclock <name>'.");
	}
}
