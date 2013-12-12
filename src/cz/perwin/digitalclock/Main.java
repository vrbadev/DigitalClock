package cz.perwin.digitalclock;

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

import cz.perwin.digitalclock.mcstats.Metrics;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private Map<Player, String> enableBuildUsers = new HashMap<Player, String>();
    private Map<Player, String> enableMoveUsers = new HashMap<Player, String>();
    private Map<String, Integer> usersClock = new HashMap<String, Integer>();
	private ArrayList<String> clocks = new ArrayList<String>();
	protected final Logger console = Logger.getLogger("Minecraft");
	private Map<String, Integer> clockTasks = new HashMap<String, Integer>();
	private FileConfiguration clocksConf = null;
	private File clocksFile = null;
	protected int settings_width;
	protected boolean separately;
	private Generator generator;
	
	static {
		System.out.println("[DigitalClock] Preparing DigitalClock for loading... :)");
		File pluginDir = new File("plugins/DigitalClock");
		if(!pluginDir.exists()) {
			pluginDir.mkdir();
		}
		final File table = new File("plugins/DigitalClock/GeoLiteCity.dat");
		if(!table.exists()) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					System.out.println("[DigitalClock] Downloading file " + table.getName() + ".");
					try {
						URL link = new URL("http://geolite.maxmind.com/download/geoip/database/GeoLiteCity.dat.gz");
					    ReadableByteChannel rbc = Channels.newChannel(new GZIPInputStream(link.openStream()));
					    FileOutputStream fos = new FileOutputStream(table);
					    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
					    fos.close();
					} catch (IOException e) {
						System.err.println("[DigitalClock] Error when downloading file " + table.getName() + ": " + e);
					} finally {
						System.out.println("[DigitalClock] File " + table.getName() + " has been downloaded.");
					}
				}
			});
			thread.start();
		}
	}
	
	public void onEnable() {
		this.console.info("[DigitalClock] Plugin has been enabled!");
		
		// PREPARING SERVER
		Clock.i = this;
		this.saveDefaultConfig();
		this.reloadConfig();
		this.saveDefaultClocksConf();
		this.reloadClocksConf();
		Main.this.settings_width = this.getConfig().getInt("width");
		Main.this.separately = this.getConfig().getBoolean("generateForEachPlayerSeparately");
		this.generator = new Generator(this);
		
		// LOADING CLASSES
		this.getServer().getPluginManager().registerEvents(new Events(this), this);
		this.getServer().getPluginCommand("digitalclock").setExecutor(new Commands(this));
		this.getServer().getPluginCommand("dc").setExecutor(new Commands(this));
		
		// LOADING CLOCKS AND GEOIPTABLES
		this.getServer().getScheduler().scheduleSyncDelayedTask(this, new AfterDone(this), 0L);
		
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
				Main.this.saveClocksConf();
			}
		}, 20L, (15*60*20));
		
		for(final String name : getClocksL()) {
			int task = this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				public void run() {
						Clock clock = Clock.loadClockByClockName(name);
						if(Main.this.getClocksConf().getKeys(false).contains(clock.getName())) {
					    	Main.this.getGenerator().start(clock);
						}
				}
			}, 0L, 20L);
			this.getClockTasks().put(name, task);
		}
	}
	
	public void runClock(final String name) {
		if(!this.getClockTasks().containsKey(name)) {
			int task = this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				public void run() {
						Clock clock = Clock.loadClockByClockName(name);
						if(Main.this.getClocksConf().getKeys(false).contains(clock.getName())) {
					    	Main.this.getGenerator().start(clock);
						}
				}
			}, 0L, 20L);
			this.getClockTasks().put(name, task);
		}	
	}
	
	public void reloadConf() {
		this.reloadConfig();
		Main.this.settings_width = this.getConfig().getInt("width");
		Main.this.separately = this.getConfig().getBoolean("generateForEachPlayerSeparately");
	}
	
	public void reloadClocksConf() {
	    setClocksConf(YamlConfiguration.loadConfiguration(clocksFile));
	    InputStream defConfigStream = this.getResource("clocks.yml");
	    if(defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        getClocksConf().setDefaults(defConfig);
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
	    if (getClocksConf() == null || clocksFile == null) {
	    return;
	    }
	    try {
	        Main.this.getClocksConf().save(clocksFile);
	    } catch (IOException ex) {
	        this.console.severe(ex + "");
	    }
	}
	
	public void getClocks() {
		this.getClocksL().clear();
		this.getUsersClock().clear();
		for(String name : Main.this.getClocksConf().getKeys(false)) {
			Clock clock = Clock.loadClockByClockName(name);
			if(this.getUsersClock().containsKey(clock.getCreator())) {
				int n = this.getUsersClock().get(clock.getCreator());
				this.getUsersClock().remove(clock.getCreator());
				this.getUsersClock().put(clock.getCreator(), (n+1));
			} else {
				this.getUsersClock().put(clock.getCreator(), 1);
			}
			this.getClocksL().add(name);
		}
	}

	public Map<String, Integer> getUsersClock() {
		return usersClock;
	}

	public void setUsersClock(Map<String, Integer> usersClock) {
		this.usersClock = usersClock;
	}

	public Map<Player, String> getEnableBuildUsers() {
		return enableBuildUsers;
	}

	public void setEnableBuildUsers(Map<Player, String> enableBuildUsers) {
		this.enableBuildUsers = enableBuildUsers;
	}

	public FileConfiguration getClocksConf() {
		return clocksConf;
	}

	public void setClocksConf(FileConfiguration clocksConf) {
		this.clocksConf = clocksConf;
	}

	public Map<Player, String> getEnableMoveUsers() {
		return enableMoveUsers;
	}

	public void setEnableMoveUsers(Map<Player, String> enableMoveUsers) {
		this.enableMoveUsers = enableMoveUsers;
	}

	public Map<String, Integer> getClockTasks() {
		return clockTasks;
	}

	public void setClockTasks(Map<String, Integer> clockTasks) {
		this.clockTasks = clockTasks;
	}
	
	public Generator getGenerator() {
		return this.generator;
	}

	public ArrayList<String> getClocksL() {
		return clocks;
	}

	public void setClocksL(ArrayList<String> clocks) {
		this.clocks = clocks;
	}
}
