package cz.perwin.digitalclock.core;

import org.bukkit.entity.Player;

import cz.perwin.digitalclock.DigitalClock;

public class CommandInfo {
	private DigitalClock i;
	private Player player;
	private String[] args;
	private String usedcmd;
	
	protected CommandInfo(DigitalClock i, Player player, String[] args, String usedcmd) {
		this.i = i;
		this.player = player;
		this.args = args;
		this.usedcmd = usedcmd;
	}
	
	public DigitalClock getMain() {
		return this.i;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public String[] getArgs() {
		return this.args;
	}
	
	public String getUsedcmd() {
		return this.usedcmd;
	}
}
