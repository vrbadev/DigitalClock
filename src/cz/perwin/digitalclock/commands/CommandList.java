package cz.perwin.digitalclock.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.core.Clock;

public class CommandList implements ICommand {
	@Override
	public int getArgsSize() {
		return 1;
	}

	@Override
	public String getPermissionName() {
		return "digitalclock.list";
	}

	@Override
	public boolean specialCondition(DigitalClock main, Player player, String[] args) {
		return false;
	}

	@Override
	public boolean checkClockExistence() {
		return false;
	}
	
	@Override
	public boolean neededClockExistenceValue() {
		return false;
	}

	@Override
	public String reactBadArgsSize(String usedCmd) {
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Correct usage: '/"+ usedCmd + " list'";
	}

	@Override
	public String reactNoPermissions() {
		return ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " You aren't allowed to use this command!";
	}

	@Override
	public void specialConditionProcess(DigitalClock main, Player player, String[] args) {
		return;
	}

	@Override
	public String reactBadClockList(String clockName) {
		return null;
	}

	@Override
	public void process(DigitalClock main, Player player, String[] args) {
		player.sendMessage(ChatColor.DARK_GREEN + DigitalClock.getMessagePrefix() + ChatColor.GREEN + " List of all existing clocks:");
		String list = "";
		int i = 0;
		if(main.getClocksL().size() != 0) {
			for(String name : main.getClocksL()) {
				Clock clock = Clock.loadClockByClockName(name);
				list += clock.getName();
				if(i != main.getClocksL().size()-1) {
					list += ", ";
				}
				i++;
			}
		} else {
			list = ChatColor.ITALIC + "No clocks found!";
		}
		player.sendMessage(ChatColor.GREEN + list);
	}
}
