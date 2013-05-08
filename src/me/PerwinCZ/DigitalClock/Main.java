package me.PerwinCZ.DigitalClock;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import me.PerwinCZ.DigitalClock.mcstats.Metrics;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    protected Map<Player, String> enableBuildUsers = new HashMap<Player, String>();
    protected Map<Player, String> enableMoveUsers = new HashMap<Player, String>();
    protected Map<String, Integer> usersClock = new HashMap<String, Integer>();
	protected ArrayList<String> clocks = new ArrayList<String>();
	protected Logger console = Logger.getLogger("Minecraft");
	protected Map<String, Integer> clockTasks = new HashMap<String, Integer>();
	private FileConfiguration settings = null;
	private File settingsFile = null;
	protected static int SETTINGS_WIDTH;
	protected static Main INSTANCE;
	
	public void onEnable() {
		this.console.info("[DigitalClock] Plugin has been enabled!");
		
		// PREPARING SERVER
		INSTANCE = this;
		this.saveDefaultConfig();
		this.reloadConfig();
		this.saveDefaultSettings();
		this.reloadSettings();
		Main.SETTINGS_WIDTH = this.getSettings().getInt("width");
		
		// LOADING CLOCKS
		this.getClocks();
		this.console.info("[DigitalClock] Loaded "+ this.clocks.size() +" clock(s).");
		this.runTasks();
		
		// LOADING CLASSES
		this.getServer().getPluginManager().registerEvents(new Events(), this);
		this.getServer().getPluginCommand("digitalclock").setExecutor(new Commands());
		
		// METRICS
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void onDisable() {
		this.getServer().getScheduler().cancelTasks(this);
		this.console.info("[DigitalClock] Plugin has been disabled!");
	}

	private void runTasks() {
		for(final String name : clocks) {
			int task = this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				public void run() {
						Clock clock = Clock.loadClockByClockName(name);
						if(Main.this.getConfig().getKeys(false).contains(clock.getName())) {
					    	Generator.start(clock);
						}
				}
			}, 0L, 20L);
			this.clockTasks.put(name, task);
		}
	}
	
	protected void runClock(final String name) {
		if(!this.clockTasks.containsKey(name)) {
			int task = this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				public void run() {
						Clock clock = Clock.loadClockByClockName(name);
						if(Main.this.getConfig().getKeys(false).contains(clock.getName())) {
					    	Generator.start(clock);
						}
				}
			}, 0L, 20L);
			this.clockTasks.put(name, task);
		}	
	}
	
	public void reloadSettings() {
	    settings = YamlConfiguration.loadConfiguration(settingsFile);
	    InputStream defConfigStream = this.getResource("settings.yml");
	    if(defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        settings.setDefaults(defConfig);
	    }
	}
	
	public FileConfiguration getSettings() {
	    if(settings == null) {
	        this.reloadSettings();
	    }
	    return settings;
	}
	
	private void saveDefaultSettings() {
	    if(settingsFile == null) {
	    	settingsFile = new File(getDataFolder(), "settings.yml");
	    }
        if(!settingsFile.exists()) {            
            this.saveResource("settings.yml", false);
        }
    }
	
	protected void getClocks() {
		this.clocks.clear();
		this.usersClock.clear();
		for(String name : this.getConfig().getKeys(false)) {
			Clock clock = Clock.loadClockByClockName(name);
			if(this.usersClock.containsKey(clock.getCreator())) {
				int n = this.usersClock.get(clock.getCreator());
				this.usersClock.remove(clock.getCreator());
				this.usersClock.put(clock.getCreator(), (n+1));
			} else {
				this.usersClock.put(clock.getCreator(), 1);
			}
			this.clocks.add(name);
		}
	}
}
