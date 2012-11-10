package me.PerwinCZ.DigitalClock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    protected Map<Player, String> enableBuildUsers = new HashMap<Player, String>();
	protected Logger console = Logger.getLogger("Minecraft");
	protected ArrayList<Clock> clocks = new ArrayList<Clock>();
	
	public void onEnable() {
		this.console.info("[DigitalClock] Plugin has been enabled!");
		this.getServer().getPluginManager().registerEvents(new Events(this), this);
		this.getServer().getPluginCommand("digitalclock").setExecutor(new Commands(this));
		this.saveDefaultConfig();
		
		clocks.clear();
		for(String name : this.getConfig().getKeys(false)) {
			Clock clock = Clock.loadClockByClockName(name);
			clocks.add(clock);
		}
		
		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
			public void run() {
				for(Clock clock : clocks) {
					if(Events.plugin.getConfig().getKeys(false).contains(clock.getName())) {
				    	Generator.start(clock);
					}
				}
			}
		}, 0L, 20L);
	}
	
	public void onDisable() {
		this.console.info("[DigitalClock] Plugin has been disabled!");
	}
}
