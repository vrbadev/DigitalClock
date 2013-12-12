package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.Clock;
import cz.perwin.digitalclock.CommandInfo;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class CommandFill extends MaterialCommand {
	@SuppressWarnings("deprecation")
	public CommandFill(CommandInfo info) {
		if(info.getArgs().length != 3) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/"+ info.getUsedcmd() + " fill <name> <material id:data>'");
		} else if(!info.getPlayer().hasPermission("digitalclock.fill") && !info.getPlayer().isOp()) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
		} else if(!info.getMain().getClocksConf().getKeys(false).contains(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + info.getArgs()[1] + "' not found!");
		} else {	
			Clock clock = Clock.loadClockByClockName(info.getArgs()[1]);
			String oldmat = clock.getFillingMaterial().name().toLowerCase().replace("_", " ");
			if(super.isUsableNumber(info.getArgs()[2])) {
				info.getPlayer().sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + info.getArgs()[1] + "' changed filling material from " + oldmat + " to "+ clock.setFillingMaterial(Integer.parseInt(info.getArgs()[2]), 0).name().toLowerCase().replace("_", " "));
			} else {
				if(info.getArgs()[2].contains(":")) {
					String[] matdata = info.getArgs()[2].split(":");
					if(super.isUsableNumber(matdata[1])) {
						if(super.isUsableNumber(matdata[0])) {
							info.getPlayer().sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + info.getArgs()[1] + "' changed filling material from " + oldmat + " to "+ clock.setFillingMaterial(Integer.parseInt(matdata[0]), Integer.parseInt(matdata[1])).name().toLowerCase().replace("_", " "));
						} else {
							try {
								Material m = Material.valueOf(matdata[0].toUpperCase());
								if(m != null) {
									info.getPlayer().sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + info.getArgs()[1] + "' changed filling material from " + oldmat + " to "+ clock.setFillingMaterial(m.getId(), Integer.parseInt(matdata[1])).name().toLowerCase().replace("_", " "));
								} else {
									info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Material "+ matdata[0] +" does not exist!");
								}
							} catch(IllegalArgumentException e) {
								info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Material '"+ info.getArgs()[2] +"' does not exist!");
							}
						}
					} else {
						info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Material data must be positive integer!");
					}
				} else {
					try {
						Material m = Material.valueOf(info.getArgs()[2].toUpperCase());
						if(m != null) {
							info.getPlayer().sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + info.getArgs()[1] + "' changed filling material from " + oldmat + " to "+ clock.setFillingMaterial(m.getId(), 0).name().toLowerCase().replace("_", " "));
						} else {
							info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Material "+ info.getArgs()[2] +" does not exist!");
						}
					} catch(IllegalArgumentException e) {
						info.getPlayer().sendMessage(ChatColor.RED + "[DigitalClock] Material '"+ info.getArgs()[2] +"' does not exist!");
					}
				}
			}
		}
	}
}
