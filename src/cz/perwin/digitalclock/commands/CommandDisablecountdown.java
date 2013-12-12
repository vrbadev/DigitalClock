package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;

import cz.perwin.digitalclock.Clock;
import cz.perwin.digitalclock.CommandInfo;

public class CommandDisablecountdown {
	public CommandDisablecountdown(CommandInfo info) {
		if(info.getArgs().length != 2) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/"+ info.getUsedcmd() + " disablecountdown <name>'");
		} else if(!info.getPlayer().hasPermission("digitalclock.disablecountdown") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
		} else if(!info.getMain().getClocksConf().getKeys(false).contains(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + info.getArgs()[1] + "' not found!");
		} else if(!Clock.loadClockByClockName(info.getArgs()[1]).isCountdownEnabled()) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + info.getArgs()[1] + "' hasn't enabled countdown mode!");
		} else {
			Clock clock = Clock.loadClockByClockName(info.getArgs()[1]);
			clock.enableCountdown(false);
			if(info.getMain().getClockTasks().containsKey(info.getArgs()[1])) {
				info.getMain().getServer().getScheduler().cancelTask(info.getMain().getClockTasks().get(info.getArgs()[1]));
				info.getMain().getClockTasks().remove(info.getArgs()[1]);
			}
			String hours = info.getMain().getGenerator().getRealNumbers(clock.getAddMinutes(), null)[0];
			String minutes = info.getMain().getGenerator().getRealNumbers(clock.getAddMinutes(), null)[1];
			String seconds = info.getMain().getGenerator().getRealNumbers(clock.getAddMinutes(), null)[2];
			info.getMain().getGenerator().generatingSequence(clock, hours, minutes, seconds, null);
			info.getPlayer().sendMessage(ChatColor.GREEN + "[DigitalClock] You have successfully disabled countdown mode on clock '" + info.getArgs()[1] + "'. Main.i clock is now stopped, run it by command '/"+ info.getUsedcmd() + " runclock <name>'.");
		}
	}
}
