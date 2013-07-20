package me.PerwinCZ.DigitalClock;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("digitalclock")) {
			if(args.length > 0) {
				if(sender instanceof Player) {
					Player player = (Player) sender;
					if(args[0].equals("create")) { // 1
						if(args.length != 2) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock create <name>'");
						} else if(!player.hasPermission("digitalclock.create") && !player.isOp()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
						} else if(Main.i.enableBuildUsers.containsKey(player)) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You are just creating another clock. You can't create more clocks in the same time!");
						} else if(Main.i.clocksConf.getKeys(false).contains(args[1])) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock with Main.i name already exists!");
						} else {
							int count = 0;
							if(Main.i.usersClock.get(player.getName()) != null) {
								count = Main.i.usersClock.get(player.getName());
							}
							boolean limitReached = false;
							if(player.hasPermission("digitalclock.limit." + count) && count != 0 && !player.isOp() && !player.hasPermission("digitalclock.limit.*")) {
								limitReached = true;
							}
							if(limitReached == false) {
						    	Main.i.enableBuildUsers.put(player, args[1]);
						      	player.sendMessage(ChatColor.GREEN + "[DigitalClock] Now you can create your " + (count+1) + ". clock. Click with any block anywhere to set start block.");
							} else {
						      	player.sendMessage(ChatColor.RED + "[DigitalClock] You can't create next clock. You have reached the limit of " + count + " clocks.");
							}
						}
					} else if(args[0].equals("remove") || args[0].equals("delete")) { // 2
						if(args.length != 2) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock remove <name>'");
						} else if(!player.hasPermission("digitalclock.remove") && !player.isOp()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
						} else if(!Main.i.clocksConf.getKeys(false).contains(args[1])) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' not found!");
						} else {		
							Clock clock = Clock.loadClockByClockName(args[1]);
							Clock.remove(clock);
							Main.i.getClocks();
							player.sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + args[1] + "' has been successfully removed.");
						}
					} else if(args[0].equals("rotate")) { // 3
						if(args.length != 3) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock rotate <name> <direction>'");
						} else if(!player.hasPermission("digitalclock.rotate") && !player.isOp()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
						} else if(!Main.i.clocksConf.getKeys(false).contains(args[1])) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' not found!");
						} else if(!args[2].equals("north") && !args[2].equals("south") && !args[2].equals("east") && !args[2].equals("west")) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Direction can be only north, south, east or west!");
						} else {	
							Clock clock = Clock.loadClockByClockName(args[1]);
							player.sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + args[1] + "' rotated successfully from " + clock.getDirection().name().toLowerCase() + " to " + clock.rotate(args[2]).name().toLowerCase());
						}
					} else if(args[0].equals("material")) { // 4
						if(args.length != 3) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock material <name> <material id:data>'");
						} else if(!player.hasPermission("digitalclock.material") && !player.isOp()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
						} else if(!Main.i.clocksConf.getKeys(false).contains(args[1])) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' not found!");
						} else {	
							Clock clock = Clock.loadClockByClockName(args[1]);
							String[] matdata = args[2].split(":");
							if(matdata.length == 1 && isUsableNumber(matdata[0])) {
								player.sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + args[1] + "' changed material from " + clock.getMaterial().name().toLowerCase().replace("_", " ") + " to "+ clock.changeMaterial(Integer.parseInt(matdata[0]), 0).name().toLowerCase().replace("_", " "));
							} else if(matdata.length == 2 && isUsableNumber(matdata[0]) && isUsableNumber(matdata[1])) {
								player.sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + args[1] + "' changed material from " + clock.getMaterial().name().toLowerCase().replace("_", " ") + " to "+ clock.changeMaterial(Integer.parseInt(matdata[0]), Integer.parseInt(matdata[1])).name().toLowerCase().replace("_", " "));
							} else {
								player.sendMessage(ChatColor.RED + "[DigitalClock] Material has to be in format ID:DATA, where ID and DATA are positive integers!");
							}
						}
					} else if(args[0].equals("fill")) { // 5
						if(args.length != 3) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock fill <name> <material id:data>'");
						} else if(!player.hasPermission("digitalclock.fill") && !player.isOp()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
						} else if(!Main.i.clocksConf.getKeys(false).contains(args[1])) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' not found!");
						} else {	
							Clock clock = Clock.loadClockByClockName(args[1]);
							String[] matdata = args[2].split(":");
							if(matdata.length == 1 && isUsableNumber(matdata[0])) {
								player.sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + args[1] + "' changed filling material from " + clock.getMaterial().name().toLowerCase().replace("_", " ") + " to "+ clock.setFillingMaterial(Integer.parseInt(matdata[0]), 0).name().toLowerCase().replace("_", " "));
							} else if(matdata.length == 2 && isUsableNumber(matdata[0]) && isUsableNumber(matdata[1])) {
								player.sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + args[1] + "' changed filling material from " + clock.getMaterial().name().toLowerCase().replace("_", " ") + " to "+ clock.setFillingMaterial(Integer.parseInt(matdata[0]), Integer.parseInt(matdata[1])).name().toLowerCase().replace("_", " "));
							} else {
								player.sendMessage(ChatColor.RED + "[DigitalClock] Filling material has to be in format ID:DATA, where ID and DATA are positive integers!");
							}
						}
					} else if(args[0].equals("move")) { // 6
						if(args.length != 2) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock move <name>'");
						} else if(!player.hasPermission("digitalclock.move") && !player.isOp()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
						} else if(!Main.i.clocksConf.getKeys(false).contains(args[1])) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' not found!");
						} else if(Main.i.enableMoveUsers.containsKey(player)) {
							player.sendMessage(ChatColor.GREEN + "[DigitalClock] Moving clock '" + args[1] + "' has been rejected!");
							Main.i.enableMoveUsers.remove(player);
						} else {	
							Main.i.enableMoveUsers.put(player, args[1]);
							player.sendMessage(ChatColor.GREEN + "[DigitalClock] Moving clock '" + args[1] + "' has been enabled. Now just right click to some place to move your clock there.");
						}
					} else if(args[0].equals("addingminutes")) { // 7
						if(args.length != 3) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock addingminutes <name> <minutes>'");
						} else if(!player.hasPermission("digitalclock.addingminutes") && !player.isOp()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
						} else if(!Main.i.clocksConf.getKeys(false).contains(args[1])) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' not found!");
						} else {
							int mins = Integer.parseInt(args[2]);
							if(mins >= 0 && mins < 1440) {
								Clock clock = Clock.loadClockByClockName(args[1]);
								clock.addMinutes(mins);
								player.sendMessage(ChatColor.GREEN + "[DigitalClock] Clock '" + args[1] + "' now will add " + args[2] + " minutes.");
							} else {
								player.sendMessage(ChatColor.RED + "[DigitalClock] Minutes can be only integer between 0 and 1439, not " + args[2] + "!");
							}
						}	
					} else if(args[0].equals("tp")) { // 8
						if(args.length != 2) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock tp <name>'");
						} else if(!player.hasPermission("digitalclock.tp") && !player.isOp()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
						} else if(!Main.i.clocksConf.getKeys(false).contains(args[1])) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' not found!");
						} else {	
							Clock clock = Clock.loadClockByClockName(args[1]);
							clock.teleportToClock(player);
							player.sendMessage(ChatColor.GREEN + "[DigitalClock] You have been successfully teleported to your clock '" + args[1] + "'.");
						}
					} else if(args[0].equals("stopclock")) { // 9
						if(args.length != 2) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock stopclock <name>'");
						} else if(!player.hasPermission("digitalclock.stopclock") && !player.isOp()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
						} else if(!Main.i.clocksConf.getKeys(false).contains(args[1])) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' not found!");
						} else if(!Main.i.clockTasks.containsKey(args[1])) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '"+ args[1] +"' is already stopped!");
						} else {	
							Main.i.getServer().getScheduler().cancelTask(Main.i.clockTasks.get(args[1]));
							player.sendMessage(ChatColor.GREEN + "[DigitalClock] You have successfully stopped clock '" + args[1] + "' (task number " + Main.i.clockTasks.get(args[1]) + ").");
							Main.i.clockTasks.remove(args[1]);
						}
					} else if(args[0].equals("runclock")) { // 10
						if(args.length != 2) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock runclock <name>'");
						} else if(!player.hasPermission("digitalclock.runclock") && !player.isOp()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
						} else if(!Main.i.clocksConf.getKeys(false).contains(args[1])) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' not found!");
						} else if(Main.i.clockTasks.containsKey(args[1])) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '"+ args[1] +"' is already running!");
						} else {	
							Main.i.runClock(args[1]);
							player.sendMessage(ChatColor.GREEN + "[DigitalClock] Clock '" + args[1] + "' is now running under task number " + Main.i.clockTasks.get(args[1]) + ".");
						}
					} else if(args[0].equals("toggleseconds")) { // 11
						if(args.length != 2) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock toggleseconds <name>'");
						} else if(!player.hasPermission("digitalclock.toggleseconds") && !player.isOp()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
						} else if(!Main.i.clocksConf.getKeys(false).contains(args[1])) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' not found!");
						} else {	
							Clock clock = Clock.loadClockByClockName(args[1]);
							if(clock.shouldShowSeconds()) {
								clock.setShowingSeconds(false);
								player.sendMessage(ChatColor.GREEN + "[DigitalClock] You have successfully hidden seconds on clock '" + args[1] + "'.");
							} else {
								clock.setShowingSeconds(true);
								player.sendMessage(ChatColor.GREEN + "[DigitalClock] You have successfully shown seconds on clock '" + args[1] + "'.");
							}
						}
					} else if(args[0].equals("toggleingametime")) { // 12
							if(args.length != 2) {
								player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock toggleingametime <name>'");
							} else if(!player.hasPermission("digitalclock.toggleingametime") && !player.isOp()) {
								player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
							} else if(!Main.i.clocksConf.getKeys(false).contains(args[1])) {
								player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' not found!");
							} else {	
								Clock clock = Clock.loadClockByClockName(args[1]);
								if(clock.isIngameTimeEnabled()) {
									clock.enableIngameTime(false);
									player.sendMessage(ChatColor.GREEN + "[DigitalClock] You have successfully disabled ingame time on clock '" + args[1] + "'.");
								} else {
									clock.enableIngameTime(true);
									player.sendMessage(ChatColor.GREEN + "[DigitalClock] You have successfully enabled ingame time on clock '" + args[1] + "'.");
								}
							}
					} else if(args[0].equals("toggleampm")) { // 13
						if(args.length != 2) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock toggleampm <name>'");
						} else if(!player.hasPermission("digitalclock.toggleampm") && !player.isOp()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
						} else if(!Main.i.clocksConf.getKeys(false).contains(args[1])) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' not found!");
						} else {	
							Clock clock = Clock.loadClockByClockName(args[1]);
							if(clock.getAMPM()) {
								clock.setAMPM(false);
								player.sendMessage(ChatColor.GREEN + "[DigitalClock] You have successfully turned AM/PM OFF on clock '" + args[1] + "'.");
							} else {
								clock.setAMPM(true);
								player.sendMessage(ChatColor.GREEN + "[DigitalClock] You have successfully turned AM/PM ON on clock '" + args[1] + "'.");
							}
						}
					} else if(args[0].equals("toggleblinking")) { // 14
						if(args.length != 2) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock toggleblinking <name>'");
						} else if(!player.hasPermission("digitalclock.toggleblinking") && !player.isOp()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
						} else if(!Main.i.clocksConf.getKeys(false).contains(args[1])) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' not found!");
						} else {
							Clock clock = Clock.loadClockByClockName(args[1]);
							if(clock.isBlinking()) {
								clock.setBlinking(false);
								player.sendMessage(ChatColor.GREEN + "[DigitalClock] You have successfully stopped blinking of colon on clock '" + args[1] + "'.");
							} else {
								clock.setBlinking(true);
								player.sendMessage(ChatColor.GREEN + "[DigitalClock] You have successfully started blinking of colon on clock '" + args[1] + "'.");
							}
						}
					} else if(args[0].equals("setcountdown")) { // 15
						if(args.length != 3) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock setcountdown <name> <seconds>'");
						} else if(!player.hasPermission("digitalclock.setcountdown") && !player.isOp()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
						} else if(!Main.i.clocksConf.getKeys(false).contains(args[1])) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' not found!");
						} else if(Clock.loadClockByClockName(args[1]).isCountdownEnabled()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' has already enabled countdown mode! Disable it with command '/digitalclock disablecountdown <name>'.");
						} else if(!(Integer.parseInt(args[2]) < 360000 && Integer.parseInt(args[2]) > 0)) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Seconds must be number between 0 and 359999!");
						} else {
							Clock clock = Clock.loadClockByClockName(args[1]);
							clock.setCountdownTime(Integer.parseInt(args[2]));
							clock.enableCountdown(true);
							if(Main.i.clockTasks.containsKey(args[1])) {
								Main.i.getServer().getScheduler().cancelTask(Main.i.clockTasks.get(args[1]));
								Main.i.clockTasks.remove(args[1]);
							}
							String hours = Generator.getCountdownNumbers(clock.getCountdownTime())[0];
							String minutes = Generator.getCountdownNumbers(clock.getCountdownTime())[1];
							String seconds = Generator.getCountdownNumbers(clock.getCountdownTime())[2];
							Generator.generatingSequence(clock, hours, minutes, seconds, null);
							player.sendMessage(ChatColor.GREEN + "[DigitalClock] You have successfully set countdown time on clock '" + args[1] + "' to "+ hours +":"+ minutes +":"+ seconds +". Main.i clock is now stopped, run it by command '/digitalclock runclock <name>'.");
						}
					} else if(args[0].equals("disablecountdown")) { // 16
						if(args.length != 2) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock disablecountdown <name>'");
						} else if(!player.hasPermission("digitalclock.disablecountdown") && !player.isOp()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
						} else if(!Main.i.clocksConf.getKeys(false).contains(args[1])) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' not found!");
						} else if(!Clock.loadClockByClockName(args[1]).isCountdownEnabled()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' hasn't enabled countdown mode!");
						} else {
							Clock clock = Clock.loadClockByClockName(args[1]);
							clock.enableCountdown(false);
							if(Main.i.clockTasks.containsKey(args[1])) {
								Main.i.getServer().getScheduler().cancelTask(Main.i.clockTasks.get(args[1]));
								Main.i.clockTasks.remove(args[1]);
							}
							String hours = Generator.getRealNumbers(clock.getAddMinutes(), null)[0];
							String minutes = Generator.getRealNumbers(clock.getAddMinutes(), null)[1];
							String seconds = Generator.getRealNumbers(clock.getAddMinutes(), null)[2];
							Generator.generatingSequence(clock, hours, minutes, seconds, null);
							player.sendMessage(ChatColor.GREEN + "[DigitalClock] You have successfully disabled countdown mode on clock '" + args[1] + "'. Main.i clock is now stopped, run it by command '/digitalclock runclock <name>'.");
						}
					} else if(args[0].equals("list")) { // 17
						if(args.length != 1) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock list'");
						} else if(!player.hasPermission("digitalclock.list") && !player.isOp()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
						} else {	
							player.sendMessage(ChatColor.GREEN + "[DigitalClock] List of all existing clocks:");
							String list = "";
							int i = 0;
							if(Main.i.clocks.size() != 0) {
								for(String name : Main.i.clocks) {
									Clock clock = Clock.loadClockByClockName(name);
									list += clock.getName();
									if(i != Main.i.clocks.size()-1) {
										list += ", ";
									}
									i++;
								}
							} else {
								list = ChatColor.ITALIC + "No clocks found!";
							}
							player.sendMessage(ChatColor.GREEN + list);
						}
					} else if(args[0].equals("reload")) { // 18
						if(args.length != 1) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock reload'");
						} else if(!player.hasPermission("digitalclock.reload") && !player.isOp()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
						} else {	
							Main.i.reloadConf();
							player.sendMessage(ChatColor.GREEN + "[DigitalClock] File config.yml has been reloaded!");
						}
					} else if(args[0].equals("settime")) { // 19
						if(args.length != 3) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock settime <name> <HH:MM>'");
						} else if(!player.hasPermission("digitalclock.settime") && !player.isOp()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
						} else if(!Main.i.clocksConf.getKeys(false).contains(args[1])) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Clock '" + args[1] + "' not found!");
						} else {
							String input = args[2];
							if(input.length() == 5 
									&& Character.isDigit(input.charAt(0))
									&& Character.isDigit(input.charAt(1))
									&& Character.isDigit(input.charAt(3))
									&& Character.isDigit(input.charAt(4))
									&& input.charAt(2) == ':') {
								int hours = Integer.parseInt(Character.toString(input.charAt(0)))*10 + Integer.parseInt(Character.toString(input.charAt(1)));
								int mins = Integer.parseInt(Character.toString(input.charAt(3)))*10 + Integer.parseInt(Character.toString(input.charAt(4)));
								if(hours >= 0 && hours < 24 && mins >= 0 && mins < 60) {
									Clock clock = Clock.loadClockByClockName(args[1]);
									int currentTimeInMinutes = Integer.parseInt(Generator.getRealNumbers(0, null)[0])*60 + Integer.parseInt(Generator.getRealNumbers(0, null)[1]);
									int newAddingMinutes = (currentTimeInMinutes - hours*60 - mins)*-1;
									clock.addMinutes(newAddingMinutes);
									player.sendMessage(ChatColor.GREEN + "[DigitalClock] Time on clock '" + args[1] + " is now "+ input +". To set the time back to real time just use command '/digitalclock addingminutes "+ args[1] +" 0'.");
								} else {
									player.sendMessage(ChatColor.RED + "[DigitalClock] HH must be integer from 00 to 23 and MM must be integer from 00 to 59!");
								}
							} else {
								player.sendMessage(ChatColor.RED + "[DigitalClock] Time has incorrect format, it has to be HH:MM!");
							}
						}		
					} else if(args[0].equals("help") || args[0].equals("?")) { // 20
						if(args.length != 1) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] Correct usage: '/digitalclock help'");
						} else if(!player.hasPermission("digitalclock.help") && !player.isOp()) {
							player.sendMessage(ChatColor.RED + "[DigitalClock] You aren't allowed to use this command!");
						} else {	
							player.sendMessage(ChatColor.GREEN + "[DigitalClock] All possible arguments:");
							player.sendMessage(ChatColor.GREEN + "/digitalclock " + 
									ChatColor.DARK_GREEN + "create" + ChatColor.GREEN + ", " + 
									ChatColor.DARK_GREEN + "remove" + ChatColor.GREEN + ", " + 
									ChatColor.DARK_GREEN + "rotate" + ChatColor.GREEN + ", " + 
									ChatColor.DARK_GREEN + "material" + ChatColor.GREEN + ", " + 
									ChatColor.DARK_GREEN + "fill" + ChatColor.GREEN + ", " + 
									ChatColor.DARK_GREEN + "addingminutes" + ChatColor.GREEN + ", " + 
									ChatColor.DARK_GREEN + "settime" + ChatColor.GREEN + ", " +
									ChatColor.DARK_GREEN + "move" + ChatColor.GREEN + ", " + 
									ChatColor.DARK_GREEN + "setcountdown" + ChatColor.GREEN + ", " + 
									ChatColor.DARK_GREEN + "disablecountdown" + ChatColor.GREEN + ", " + 
									ChatColor.DARK_GREEN + "toggleampm" + ChatColor.GREEN + ", " + 
									ChatColor.DARK_GREEN + "toggleseconds" + ChatColor.GREEN + ", " + 
									ChatColor.DARK_GREEN + "toggleblinking" + ChatColor.GREEN + ", " + 
									ChatColor.DARK_GREEN + "toggleingametime" + ChatColor.GREEN + ", " + 
									ChatColor.DARK_GREEN + "tp" + ChatColor.GREEN + ", " + 
									ChatColor.DARK_GREEN + "stopclock" + ChatColor.GREEN + ", " + 
									ChatColor.DARK_GREEN + "runclock" + ChatColor.GREEN + ", " + 
									ChatColor.DARK_GREEN + "list" + ChatColor.GREEN + ", " + 
									ChatColor.DARK_GREEN + "reload" + ChatColor.GREEN + " and " + 
									ChatColor.DARK_GREEN + "help" + ChatColor.GREEN + ".\nYou can find more information on\n" + ChatColor.BLUE + "http://dev.bukkit.org/server-mods/digitalclock");
						}
			    	} else {
			    		player.sendMessage(ChatColor.RED + "[DigitalClock] This argument doesn't exist. Show '/digitalclock help' for more info.");
			    	}
        		} else {
    				sender.sendMessage(ChatColor.RED + "[DigitalClock] This command can be executed only from the game!");
        		}
			} else {
				sender.sendMessage(ChatColor.GREEN + "---- DigitalClock v"+ Main.i.getDescription().getVersion() +" ----\nAuthor: PerwinCZ\nRun command '/digitalclock help' in game for commands list.");
			}
			return true;
		} 
		return false;
	}
	
	private boolean isUsableNumber(String s) {
		if(s.matches("^[0-9]*([,]{1}[0-9]{0,2}){0,1}$")) {
			return true;
		}
		return false;
	}
}
