package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import cz.perwin.digitalclock.core.Clock;
import cz.perwin.digitalclock.core.CommandInfo;

public class CommandMaterial extends MaterialCommand {
	@SuppressWarnings({ "deprecation" })
	public CommandMaterial(CommandInfo info) {
		if(info.getArgs().length != 3) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Correct usage: '/"+ info.getUsedcmd() + " material <name> <material id:data>'");
		} else if(!super.isPermitted(info.getPlayer(), info.getArgs())) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " You aren't allowed to use this command!");
		} else if(!info.getMain().getClocksConf().getKeys(false).contains(info.getArgs()[1])) {
			info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Clock '" + info.getArgs()[1] + "' not found!");
		} else {	
			Clock clock = Clock.loadClockByClockName(info.getArgs()[1]);
			String oldmat = clock.getMaterial().name().toLowerCase().replace("_", " ");
			if(super.isUsableNumber(info.getArgs()[2])) {
				Material m = Material.getMaterial(Integer.parseInt(info.getArgs()[2]));
				if(super.isSolid(m)) {
					info.getPlayer().sendMessage(ChatColor.DARK_GREEN + info.getMain().getMessagePrefix() + ChatColor.GREEN + " Your clock '" + info.getArgs()[1] + "' changed material from " + oldmat + " to "+ clock.changeMaterial(m.getId(), 0).name().toLowerCase().replace("_", " "));
				} else {
					info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " You can't use "+ m.name().toLowerCase().replace("_", " ") +" as a material, because it is not a block (cuboid shape)!");	
				}
			} else {
				if(info.getArgs()[2].contains(":")) {
					String[] matdata = info.getArgs()[2].split(":");
					if(super.isUsableNumber(matdata[1])) {
						if(super.isUsableNumber(matdata[0])) {
							Material m = Material.getMaterial(Integer.parseInt(matdata[0]));
							if(super.isSolid(m)) {
								info.getPlayer().sendMessage(ChatColor.DARK_GREEN + info.getMain().getMessagePrefix() + ChatColor.GREEN + " Your clock '" + info.getArgs()[1] + "' changed material from " + oldmat + " to "+ clock.changeMaterial(m.getId(), Integer.parseInt(matdata[1])).name().toLowerCase().replace("_", " "));
							} else {
								info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " You can't use "+ m.name().toLowerCase().replace("_", " ") +" as a material, because it is not a block (cuboid shape)!");	
							}
						} else {
							try {
								Material m = Material.valueOf(matdata[0].toUpperCase());
								if(m != null) {
									if(super.isSolid(m)) {
										info.getPlayer().sendMessage(ChatColor.DARK_GREEN + info.getMain().getMessagePrefix() + ChatColor.GREEN + " Your clock '" + info.getArgs()[1] + "' changed material from " + oldmat + " to "+ clock.changeMaterial(m.getId(), Integer.parseInt(matdata[1])).name().toLowerCase().replace("_", " "));
									} else {
										info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " You can't use "+ m.name().toLowerCase().replace("_", " ") +" as a material, because it is not a block (cuboid shape)!");			
									}
								} else {
									info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Material "+ matdata[0] +" does not exist!");
								}
							} catch(IllegalArgumentException e) {
								info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Material '"+ matdata[0] +"' does not exist!");
							}
						}
					} else {
						info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Material data must be positive integer!");
					}
				} else {
					try {
						Material m = Material.valueOf(info.getArgs()[2].toUpperCase());
						if(m != null) {
							if(super.isSolid(m)) {
								info.getPlayer().sendMessage(ChatColor.DARK_GREEN + info.getMain().getMessagePrefix() + ChatColor.GREEN + " Your clock '" + info.getArgs()[1] + "' changed material from " + oldmat + " to "+ clock.changeMaterial(m.getId(), 0).name().toLowerCase().replace("_", " "));
							} else {
								info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " You can't use "+ m.name().toLowerCase().replace("_", " ") +" as a material, because it is not a block (cuboid shape)!");			
							}
						} else {
							info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Material "+ info.getArgs()[2] +" does not exist!");
						}
					} catch(IllegalArgumentException e) {
						info.getPlayer().sendMessage(ChatColor.DARK_RED + info.getMain().getMessagePrefix() + ChatColor.RED + " Material '"+ info.getArgs()[2] +"' does not exist!");
					}
				}
			}
		}
	}
}
