package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;

import cz.perwin.digitalclock.core.Clock;
import cz.perwin.digitalclock.core.ClockMode;
import cz.perwin.digitalclock.core.CommandInfo;

public class CommandSetcountdown {
	public CommandSetcountdown(CommandInfo info) {
		if(info.getArgs().length != 3) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Correct usage: '/"+ info.getUsedcmd() + " setcountdown <name> <seconds>'");
		} else if(!info.getPlayer().hasPermission("digitalclock.setcountdown") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " You aren't allowed to use this command!");
		} else if(!info.getMain().getClocksConf().getKeys(false).contains(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Clock '" + info.getArgs()[1] + "' not found!");
		} else if(Clock.loadClockByClockName(info.getArgs()[1]).getClockMode() == ClockMode.COUNTDOWN) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Clock '" + info.getArgs()[1] + "' has already enabled countdown mode! Disable it with command '/"+ info.getUsedcmd() + " disablecountdown <name>'.");
		} else if(!(Integer.parseInt(info.getArgs()[2]) < 360000 && Integer.parseInt(info.getArgs()[2]) > 0)) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Seconds must be number between 0 and 359999!");
		} else {
			Clock clock = Clock.loadClockByClockName(info.getArgs()[1]);
			clock.setCountdownTime(Integer.parseInt(info.getArgs()[2]));
			clock.enableCountdown(true);
			if(info.getMain().getClockTasks().containsKey(info.getArgs()[1])) {
				info.getMain().getServer().getScheduler().cancelTask(info.getMain().getClockTasks().get(info.getArgs()[1]));
				info.getMain().getClockTasks().remove(info.getArgs()[1]);
			}
			String hours = info.getMain().getGenerator().getCountdownNumbers(clock.getCountdownTime())[0];
			String minutes = info.getMain().getGenerator().getCountdownNumbers(clock.getCountdownTime())[1];
			String seconds = info.getMain().getGenerator().getCountdownNumbers(clock.getCountdownTime())[2];
			info.getMain().getGenerator().generatingSequence(clock, hours, minutes, seconds, null);
			info.getPlayer().sendMessage(ChatColor.DARK_GREEN + info.getMain().getMessagePrefix() + ChatColor.GREEN + " You have successfully set countdown time on clock '" + info.getArgs()[1] + "' to "+ hours +":"+ minutes +":"+ seconds +". Main.i clock is now stopped, run it by command '/"+ info.getUsedcmd() + " runclock <name>'.");
		}
	}
}
