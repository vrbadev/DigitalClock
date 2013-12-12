package cz.perwin.digitalclock;

import org.bukkit.entity.Player;

public class CommandInfo {
	private Main i;
	private Player player;
	private String[] args;
	private String usedcmd;
	
	protected CommandInfo(Main i, Player player, String[] args, String usedcmd) {
		this.i = i;
		this.player = player;
		this.args = args;
		this.usedcmd = usedcmd;
	}
	
	public Main getMain() {
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
