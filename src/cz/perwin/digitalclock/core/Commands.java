package cz.perwin.digitalclock.core;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.commands.CommandAddingminutes;
import cz.perwin.digitalclock.commands.CommandCreate;
import cz.perwin.digitalclock.commands.CommandDisablecountdown;
import cz.perwin.digitalclock.commands.CommandDisablestopwatch;
import cz.perwin.digitalclock.commands.CommandFill;
import cz.perwin.digitalclock.commands.CommandHelp;
import cz.perwin.digitalclock.commands.CommandList;
import cz.perwin.digitalclock.commands.CommandMaterial;
import cz.perwin.digitalclock.commands.CommandMove;
import cz.perwin.digitalclock.commands.CommandReload;
import cz.perwin.digitalclock.commands.CommandRemove;
import cz.perwin.digitalclock.commands.CommandRotate;
import cz.perwin.digitalclock.commands.CommandRunclock;
import cz.perwin.digitalclock.commands.CommandSetcountdown;
import cz.perwin.digitalclock.commands.CommandSetstopwatch;
import cz.perwin.digitalclock.commands.CommandSettime;
import cz.perwin.digitalclock.commands.CommandStopclock;
import cz.perwin.digitalclock.commands.CommandTP;
import cz.perwin.digitalclock.commands.CommandToggleampm;
import cz.perwin.digitalclock.commands.CommandToggleblinking;
import cz.perwin.digitalclock.commands.CommandToggleingametime;
import cz.perwin.digitalclock.commands.CommandToggleseconds;
import cz.perwin.digitalclock.commands.CommandUpdate;
import cz.perwin.digitalclock.commands.ICommand;

public class Commands implements CommandExecutor {
	private DigitalClock i;
	public static HashMap<String, Class<?>> commandList = new HashMap<>();
	
	static {
		commandList.put("create", CommandCreate.class);
		commandList.put("remove", CommandRemove.class);
		commandList.put("delete", CommandRemove.class);
		commandList.put("rotate", CommandRotate.class);
		commandList.put("material", CommandMaterial.class);
		commandList.put("fill", CommandFill.class);
		commandList.put("move", CommandMove.class);
		commandList.put("addingminutes", CommandAddingminutes.class);
		commandList.put("tp", CommandTP.class);
		commandList.put("stopclock", CommandStopclock.class);
		commandList.put("runclock", CommandRunclock.class);
		commandList.put("toggleseconds", CommandToggleseconds.class);
		commandList.put("toggleingametime", CommandToggleingametime.class);
		commandList.put("toggleampm", CommandToggleampm.class);
		commandList.put("toggleblinking", CommandToggleblinking.class);
		commandList.put("setcountdown", CommandSetcountdown.class);
		commandList.put("disablecountdown", CommandDisablecountdown.class);
		commandList.put("setstopwatch", CommandSetstopwatch.class);
		commandList.put("disablestopwatch", CommandDisablestopwatch.class);
		commandList.put("list", CommandList.class);
		commandList.put("reload", CommandReload.class);
		commandList.put("settime", CommandSettime.class);
		commandList.put("update", CommandUpdate.class);
		commandList.put("help", CommandHelp.class);
		commandList.put("?", CommandHelp.class);
	}
	
	public Commands(DigitalClock i) {
		this.i = i;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("digitalclock") || command.getName().equalsIgnoreCase("dc")) {
			String usedcmd = command.getName().toLowerCase();
			if(args.length > 0) {
				if(sender instanceof Player) {
					Player player = (Player) sender;
					
					if(commandList.containsKey(args[0].toLowerCase())) {
						ICommand ic = null;
						try {
							Class<?> clazz = Commands.class.getClassLoader().loadClass(commandList.get(args[0].toLowerCase()).getName());
							ic = (ICommand) clazz.newInstance();
						} catch(ClassNotFoundException | InstantiationException | IllegalAccessException e) {
							System.err.println("Problem occured when processing command: " + e.getMessage());
							//e.printStackTrace();
						} finally {
							if(ic != null) {
								this.processCommand(usedcmd, player, args, ic);
							}
						}
					} else {
						player.sendMessage(ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " This argument doesn't exist. Show '/"+ usedcmd + " help' for more info.");
					}
        		} else {
        			if(args[0].equalsIgnoreCase("reload")) { // 18
    					this.processCommand(usedcmd, null, args, new CommandReload());
        			} else if(args[0].equalsIgnoreCase("update")) { // 20
    					this.processCommand(usedcmd, null, args, new CommandUpdate());
        			} else {
        				sender.sendMessage(ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " This command can be executed only from the game!");
        			}
        		}
			} else {
				sender.sendMessage(ChatColor.GREEN + "---- DigitalClock v"+ this.i.getDescription().getVersion() +" ----\nAuthor: PerwinCZ\nRun command '/"+ usedcmd + " help' in game for commands list.");
			}
			return true;
		} 
		return false;
	}
	
	private void processCommand(String usedcmd, Player player, String[] args, ICommand ic) {
		if(args.length != ic.getArgsSize()) {
			player.sendMessage(ic.reactBadArgsSize(usedcmd));
		} else if(!player.hasPermission(ic.getPermissionName()) && !player.isOp()) {
			player.sendMessage(ic.reactNoPermissions());
		} else if(ic.checkClockExistence() && i.getClocksConf().getKeys(false).contains(args[1]) != ic.neededClockExistenceValue()) {
			player.sendMessage(ic.reactBadClockList(args[1]));
		} else if(ic.specialCondition(i, player, args)) {
			ic.specialConditionProcess(i, player, args);
		} else {
			ic.process(i, player, args);
		}
	}
}
