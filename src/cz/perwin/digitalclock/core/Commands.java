package cz.perwin.digitalclock.core;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.commands.CommandAddingminutes;
import cz.perwin.digitalclock.commands.CommandCreate;
import cz.perwin.digitalclock.commands.CommandDisablecountdown;
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
import cz.perwin.digitalclock.commands.CommandSettime;
import cz.perwin.digitalclock.commands.CommandStopclock;
import cz.perwin.digitalclock.commands.CommandTP;
import cz.perwin.digitalclock.commands.CommandToggleampm;
import cz.perwin.digitalclock.commands.CommandToggleblinking;
import cz.perwin.digitalclock.commands.CommandToggleingametime;
import cz.perwin.digitalclock.commands.CommandToggleseconds;
import cz.perwin.digitalclock.commands.CommandUpdate;

public class Commands implements CommandExecutor {
	private DigitalClock i;
	
	public Commands(DigitalClock i) {
		this.i = i;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("digitalclock") || command.getName().equalsIgnoreCase("dc")) {
			String usedcmd = command.getName().toLowerCase();
			// TODO zmena oproti verzi 1.6 (pridan prikaz /dc)
			if(args.length > 0) {
				if(sender instanceof Player) {
					Player player = (Player) sender;
					CommandInfo info = new CommandInfo(this.i, player, args, usedcmd);
					
					if(args[0].equalsIgnoreCase("create")) { // 1
						new CommandCreate(info);
					} else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")) { // 2
						new CommandRemove(info);
					} else if(args[0].equalsIgnoreCase("rotate")) { // 3
						new CommandRotate(info);
					} else if(args[0].equalsIgnoreCase("material")) { // 4
						new CommandMaterial(info);
					} else if(args[0].equalsIgnoreCase("fill")) { // 5
						new CommandFill(info);
					} else if(args[0].equalsIgnoreCase("move")) { // 6
						new CommandMove(info);
					} else if(args[0].equalsIgnoreCase("addingminutes")) { // 7
						new CommandAddingminutes(info);
					} else if(args[0].equalsIgnoreCase("tp")) { // 8
						new CommandTP(info);
					} else if(args[0].equalsIgnoreCase("stopclock")) { // 9
						new CommandStopclock(info);
					} else if(args[0].equalsIgnoreCase("runclock")) { // 10
						new CommandRunclock(info);
					} else if(args[0].equalsIgnoreCase("toggleseconds")) { // 11
						new CommandToggleseconds(info);
					} else if(args[0].equalsIgnoreCase("toggleingametime")) { // 12
						new CommandToggleingametime(info);
					} else if(args[0].equalsIgnoreCase("toggleampm")) { // 13
						new CommandToggleampm(info);
					} else if(args[0].equalsIgnoreCase("toggleblinking")) { // 14
						new CommandToggleblinking(info);
					} else if(args[0].equalsIgnoreCase("setcountdown")) { // 15
						new CommandSetcountdown(info);
					} else if(args[0].equalsIgnoreCase("disablecountdown")) { // 16
						new CommandDisablecountdown(info);
					} else if(args[0].equalsIgnoreCase("list")) { // 17
						new CommandList(info);
					} else if(args[0].equalsIgnoreCase("reload")) { // 18
						new CommandReload(info, sender);
					} else if(args[0].equalsIgnoreCase("settime")) { // 19
						new CommandSettime(info);
					} else if(args[0].equalsIgnoreCase("update")) { // 20
						new CommandUpdate(info, sender);
					} else if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) { // 21
						new CommandHelp(info);
			    	} else {
			    		player.sendMessage(ChatColor.DARK_RED + this.i.getMessagePrefix() + ChatColor.RED + " This argument doesn't exist. Show '/"+ usedcmd + " help' for more info.");
			    	}
        		} else {
        			if(args[0].equalsIgnoreCase("reload")) { // 18
    					CommandInfo info = new CommandInfo(this.i, null, args, usedcmd);
						new CommandReload(info, sender);
        			} else if(args[0].equalsIgnoreCase("update")) { // 20
    					CommandInfo info = new CommandInfo(this.i, null, args, usedcmd);
						new CommandUpdate(info, sender);
        			} else {
        				sender.sendMessage(ChatColor.DARK_RED + this.i.getMessagePrefix() + ChatColor.RED + " This command can be executed only from the game!");
        			}
        		}
			} else {
				sender.sendMessage(ChatColor.GREEN + "---- DigitalClock v"+ this.i.getDescription().getVersion() +" ----\nAuthor: PerwinCZ\nRun command '/"+ usedcmd + " help' in game for commands list.");
			}
			return true;
		} 
		return false;
	}
}
