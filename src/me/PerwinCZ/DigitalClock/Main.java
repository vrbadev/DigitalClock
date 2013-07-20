package me.PerwinCZ.DigitalClock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

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
	protected final Logger console = Logger.getLogger("Minecraft");
	protected Map<String, Integer> clockTasks = new HashMap<String, Integer>();
	protected FileConfiguration clocksConf = null;
	private File clocksFile = null;
	protected int settings_width;
	protected boolean separately;
	protected static Main i;
	
	static {
		File pluginDir = new File("plugins/DigitalClock");
		if(!pluginDir.exists()) {
			pluginDir.mkdir();
		}
		final File table = new File("plugins/DigitalClock/GeoLiteCity.dat");
		if(!table.exists()) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					Logger.getLogger("Minecraft").info("[DigitalClock] Downloading file " + table.getName() + ".");
					try {
						URL link = new URL("http://geolite.maxmind.com/download/geoip/database/GeoLiteCity.dat.gz");
					    ReadableByteChannel rbc = Channels.newChannel(new GZIPInputStream(link.openStream()));
					    FileOutputStream fos = new FileOutputStream(table);
					    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
					    fos.close();
					} catch (IOException e) {
						Logger.getLogger("Minecraft").severe("[DigitalClock] Error when downloading file " + table.getName() + ": " + e);
					} finally {
						Logger.getLogger("Minecraft").info("[DigitalClock] File " + table.getName() + " has been downloaded.");
					}
				}
			});
			thread.start();
		}
	}
	
	public void onEnable() {
		this.console.info("[DigitalClock] Plugin has been enabled!");
		
		// PREPARING SERVER
		i = this;
		this.saveDefaultConfig();
		this.reloadConfig();
		this.saveDefaultClocksConf();
		this.reloadClocksConf();
		Main.i.settings_width = this.getConfig().getInt("width");
		Main.i.separately = this.getConfig().getBoolean("generateForEachPlayerSeparately");
		
		// LOADING CLASSES
		this.getServer().getPluginManager().registerEvents(new Events(), this);
		this.getServer().getPluginCommand("digitalclock").setExecutor(new Commands());
		
		// LOADING CLOCKS AND GEOIPTABLES
		this.getServer().getScheduler().scheduleSyncDelayedTask(this, new AfterDone(), 0L);
		
		// METRICS
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
			this.console.severe(e + "");
		}
	}
	
	public void onDisable() {
		this.getServer().getScheduler().cancelTasks(this);
		this.console.info("[DigitalClock] Plugin has been disabled!");
		this.saveClocksConf();
	}

	protected void runTasks() {
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				Main.i.saveClocksConf();
			}
		}, 20L, (15*60*20));
		
		for(final String name : clocks) {
			int task = this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				public void run() {
						Clock clock = Clock.loadClockByClockName(name);
						if(Main.i.clocksConf.getKeys(false).contains(clock.getName())) {
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
						if(Main.i.clocksConf.getKeys(false).contains(clock.getName())) {
					    	Generator.start(clock);
						}
				}
			}, 0L, 20L);
			this.clockTasks.put(name, task);
		}	
	}
	
	protected void reloadConf() {
		this.reloadConfig();
		Main.i.settings_width = this.getConfig().getInt("width");
		Main.i.separately = this.getConfig().getBoolean("generateForEachPlayerSeparately");
	}
	
	public void reloadClocksConf() {
	    clocksConf = YamlConfiguration.loadConfiguration(clocksFile);
	    InputStream defConfigStream = this.getResource("clocks.yml");
	    if(defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        clocksConf.setDefaults(defConfig);
	    }
	}
	
	protected void saveDefaultClocksConf() {
	    if(clocksFile == null) {
	    	clocksFile = new File(getDataFolder(), "clocks.yml");
	    }
        if(!clocksFile.exists()) {            
            this.saveResource("clocks.yml", false);
        }
    }
	
	protected void saveClocksConf() {
	    if (clocksConf == null || clocksFile == null) {
	    return;
	    }
	    try {
	        Main.i.clocksConf.save(clocksFile);
	    } catch (IOException ex) {
	        this.console.severe(ex + "");
	    }
	}
	
	protected void getClocks() {
		this.clocks.clear();
		this.usersClock.clear();
		for(String name : Main.i.clocksConf.getKeys(false)) {
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
