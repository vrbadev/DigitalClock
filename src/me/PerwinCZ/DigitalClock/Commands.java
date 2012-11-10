package me.PerwinCZ.DigitalClock;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	private Main plugin;
	
	protected Commands(Main instance) {
		this.plugin = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("digitalclock") && args.length > 0 && sender instanceof Player) {
			Player player = (Player) sender;
			if(args[0].equals("create")) {
				if(args.length != 2) {
					player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock create <name>'");
				} else if(!player.hasPermission("digitalclock.create") || !player.isOp()) {
					player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
				} else if(plugin.enableBuildUsers.containsKey(player)) {
					player.sendMessage(ChatColor.RED + "[DigitalClock] You are just creating another clock. You can't create more clocks in the same time!");
				} else if(plugin.getConfig().getKeys(false).contains(args[1])) {
					player.sendMessage(ChatColor.RED + "[DigitalClock] Clock with this name already exists!");
				} else {		
				   	plugin.enableBuildUsers.put(player, args[1]);
				   	player.sendMessage(ChatColor.GREEN + "[DigitalClock] Now you can create the clock. Click with any block anywhere to set start block.");
				}
			} else if(args[0].equals("remove") || args[0].equals("delete")) {
				if(args.length != 2) {
					player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock remove <name>'");
				} else if(!player.hasPermission("digitalclock.remove") || !player.isOp()) {
					player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
				} else if(!plugin.getConfig().getKeys(false).contains(args[1])) {
					player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' not found!");
				} else {		
					Clock clock = Clock.loadClockByClockName(args[1]);
					Clock.remove(clock);
					plugin.clocks.remove(clock);
					player.sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + args[1] + "' has been successfully removed.");
				}
			} else if(args[0].equals("rotate")) {
				if(args.length != 3) {
					player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock rotate <name> <direction>'");
				} else if(!player.hasPermission("digitalclock.rotate") || !player.isOp()) {
					player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
				} else if(!plugin.getConfig().getKeys(false).contains(args[1])) {
					player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' not found!");
				} else if(!args[2].equals("north") && !args[2].equals("south") && !args[2].equals("east") && !args[2].equals("west")) {
					player.sendMessage(ChatColor.RED + "[DigitalClock] Direction can be only north, south, east or west!");
				} else {	
					Clock clock = Clock.loadClockByClockName(args[1]);
					player.sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + args[1] + "' rotated successfully from " + clock.getDirection().name().toLowerCase() + " to " + clock.rotate(args[2]).name().toLowerCase());
				}
			} else if(args[0].equals("material")) {
				if(args.length != 3) {
					player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock material <name> <material id>'");
				} else if(!player.hasPermission("digitalclock.material") || !player.isOp()) {
					player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
				} else if(!plugin.getConfig().getKeys(false).contains(args[1])) {
					player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' not found!");
				} else {	
					Clock clock = Clock.loadClockByClockName(args[1]);
					player.sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + args[1] + "' changed material from " + clock.getMaterial().name() + " to "+ clock.changeMaterial(Integer.parseInt(args[2])).name());
				}
			} else {
				player.sendMessage(ChatColor.RED + "[DigitalClock] This argument doesn't exist. Show '/digitalclock help' for more info.");
			}
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "[DigitalClock] This command can be executed only from game and has to have minimally 1 argument!");
		}
		return false;
	}

}
