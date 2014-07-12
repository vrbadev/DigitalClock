package cz.perwin.digitalclock.core;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Clock {
	private String clockName;
	private String clockCreator;
	private Material material;
	private Material fillingMaterial = Material.AIR;
	private boolean retrieveData = true;
	private byte data = (byte) 0;
	private byte fillingData = (byte) 0;
	private int addMinutes = 0;
	protected boolean showSeconds;
	private boolean blinking;
	private boolean blinkingChanger;
	protected boolean ampm;
	private int countdownto;
	private ClockMode clockMode;
	private ClockArea clockArea;
	
	@SuppressWarnings("deprecation")
	public Clock(String name, String playerName, Block block, Block playersBlock) {
		this.clockName = name;
		this.clockCreator = playerName;
		this.retrieveData = true;
		this.material = block.getType();
		this.data = block.getData();
		this.clockMode = ClockMode.NORMAL;
		this.clockArea = new ClockArea(this, block, playersBlock);
	}
	
	public void writeAndGenerate() {
		if(!this.isSomethingMissing()) {
			this.write();
			Generator.getGenerator().generateOnce(this);
		} else {
			throw new NullPointerException("Missing data found when generating clock '"+ this.clockName +"'!");
		}
	}
	
	public void write() {
		if(!this.isSomethingMissing()) {
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".creator", this.clockCreator);
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".world", this.clockArea.getStartBlock().getWorld().getName());
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".x", this.clockArea.getStartBlock().getX());
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".y", this.clockArea.getStartBlock().getY());
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".z", this.clockArea.getStartBlock().getZ());
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".x2", this.clockArea.getPlayersBlock().getX());
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".y2", this.clockArea.getPlayersBlock().getY());
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".z2", this.clockArea.getPlayersBlock().getZ());
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".direction", this.clockArea.getDirection().name());
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".material", this.material.name());
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".data", this.data);
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".filling", this.fillingMaterial.name());
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".fdata", this.fillingData);
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".add", this.addMinutes);
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".seconds", this.showSeconds);
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".blinking", this.blinking);
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".changer", this.blinkingChanger);
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".ampm", this.ampm);
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".cdt", this.countdownto);
			Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".mode", this.clockMode.name());
			//Generator.getGenerator().getMain().saveConfig();
		} else {
			throw new NullPointerException("Missing data to write when saving the clock '"+ this.clockName +"'!");
		}
	}
	
	public void teleportToClock(Player player) {
		player.teleport(this.getClockArea().getPlayersBlock().getLocation());
	}
	
	public Material getFillingMaterial() {
		this.reloadFromConfig();
		return this.fillingMaterial;
	}
	
	public byte getFillingData() {
		this.reloadFromConfig();
		return this.fillingData;
	}
	
	public ClockMode getClockMode() {
		this.reloadFromConfig();
		return this.clockMode;
	}
	
	public void enableIngameTime(boolean b) {
		ClockThread.getByClock(this).removeThreadAndRestore();
		this.clockMode = b ? ClockMode.INGAMETIME : ClockMode.NORMAL;
		this.write();
	}
	
	@SuppressWarnings("deprecation")
	public Material setFillingMaterial(int id, int md) {
		this.fillingMaterial = Material.getMaterial(id);
		this.fillingData = (byte) md;
		this.write();
		return this.fillingMaterial;
	}
	
	public static void stopTask(String clockName) {
		Generator.getGenerator().getMain().getServer().getScheduler().cancelTask(Generator.getGenerator().getMain().getClockTasks().get(clockName));
		Generator.getGenerator().getMain().getClockTasks().remove(clockName);
	}

	public static void eraseCompletely(Clock clock) {
		if(Generator.getGenerator().getMain().getClockTasks().containsKey(clock.getName())) {
			Clock.stopTask(clock.getName());
		}
		ClockThread.getByClock(clock).removeThreadAndRestore();
		clock.setRetrieveData(false);
		Generator.getGenerator().getMain().getClocksConf().set(clock.getName(), null);
		//Generator.getGenerator().getMain().saveConfig();
	}
	
    public static Clock loadClockByClockName(String clockName) {
	    if(Generator.getGenerator().getMain().getClocksConf().getKeys(false).contains(clockName)) {
			Location loc = new Location(Generator.getGenerator().getMain().getServer().getWorld(Generator.getGenerator().getMain().getClocksConf().getString(clockName + ".world")), Generator.getGenerator().getMain().getClocksConf().getInt(clockName + ".x"), Generator.getGenerator().getMain().getClocksConf().getInt(clockName + ".y"), Generator.getGenerator().getMain().getClocksConf().getInt(clockName + ".z"));
			Location loc2 = new Location(Generator.getGenerator().getMain().getServer().getWorld(Generator.getGenerator().getMain().getClocksConf().getString(clockName + ".world")), Generator.getGenerator().getMain().getClocksConf().getInt(clockName + ".x2"), Generator.getGenerator().getMain().getClocksConf().getInt(clockName + ".y2"), Generator.getGenerator().getMain().getClocksConf().getInt(clockName + ".z2"));
	    	return new Clock(clockName, Generator.getGenerator().getMain().getClocksConf().getString(clockName + ".creator"), Generator.getGenerator().getMain().getServer().getWorld(Generator.getGenerator().getMain().getClocksConf().getString(clockName + ".world")).getBlockAt(loc), Generator.getGenerator().getMain().getServer().getWorld(Generator.getGenerator().getMain().getClocksConf().getString(clockName + ".world")).getBlockAt(loc2));
	    }
		return null;
	}
	
    public void reloadFromConfig() {
    	if(Generator.getGenerator().getMain().getClocksConf().getKeys(false).contains(this.clockName) && this.retrieveData == true) {
        	World w = Generator.getGenerator().getMain().getServer().getWorld(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".world"));
        	this.clockArea = new ClockArea(this, new Location(w, Generator.getGenerator().getMain().getClocksConf().getInt(this.clockName + ".x"), Generator.getGenerator().getMain().getClocksConf().getInt(this.clockName + ".y"), Generator.getGenerator().getMain().getClocksConf().getInt(this.clockName + ".z")), new Location(w, Generator.getGenerator().getMain().getClocksConf().getInt(this.clockName + ".x2"), Generator.getGenerator().getMain().getClocksConf().getInt(this.clockName + ".y2"), Generator.getGenerator().getMain().getClocksConf().getInt(this.clockName + ".z2")), BlockFace.valueOf(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".direction")));
        	this.clockCreator = Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".creator");
        	this.material = Material.valueOf(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".material"));
        	this.data = (byte) Generator.getGenerator().getMain().getClocksConf().getInt(this.clockName + ".data");
        	this.fillingMaterial = Material.valueOf(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".filling"));
        	this.fillingData = (byte) Generator.getGenerator().getMain().getClocksConf().getInt(this.clockName + ".fdata");
        	this.addMinutes = Integer.parseInt(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".add"));
        	this.showSeconds = Boolean.parseBoolean(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".seconds"));
        	this.blinking = Boolean.parseBoolean(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".blinking"));
        	this.blinkingChanger = Boolean.parseBoolean(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".changer"));
        	this.ampm = Boolean.parseBoolean(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".ampm"));
        	this.countdownto = Generator.getGenerator().getMain().getClocksConf().getInt(this.clockName + ".cdt");
        	this.clockMode = ClockMode.valueOf(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".mode", "NORMAL"));
        }
    }
    
    @SuppressWarnings("deprecation")
	public Material changeMaterial(int id, int md) {
    	this.material = Material.getMaterial(id);
    	this.data = (byte) md;
    	this.writeAndGenerate();
		return this.material;
    }
    
	public String getCreator() {
		this.reloadFromConfig();
		return this.clockCreator;
	}
	
	public void enableCountdown(boolean c) {
		this.reloadFromConfig();
		this.clockMode = c ? ClockMode.COUNTDOWN : ClockMode.NORMAL;
		this.write();
	}
	
	public int getCountdownTime() {
		this.reloadFromConfig();
		return this.countdownto;
	}
	
	public void setCountdownTime(int t) {
		this.reloadFromConfig();
		this.countdownto = t;
		this.write();
	}
	
	public void setCreator(String creator) {
		this.clockCreator = creator;
		this.write();
	}
	
	public void setShowingSeconds(boolean ss) {
		ClockThread.getByClock(this).removeThreadAndRestore();
		this.showSeconds = ss;
		this.write();
		ClockArea.resetDimensions(this);
	}
	
	public void setBlinking(boolean bl) {
		this.blinking = bl;
		this.write();
	}
	
	public boolean isBlinking() {
		this.reloadFromConfig();
		return this.blinking;
	}
	
	public void setAMPM(boolean ap) {
		ClockThread.getByClock(this).removeThreadAndRestore();
		this.ampm = ap;
		this.write();
		ClockArea.resetDimensions(this);
	}
	
	public boolean getAMPM() {
		this.reloadFromConfig();
		return this.ampm;
	}
	
	protected void setBlinkingChanger(boolean blm) {
		this.blinkingChanger = blm;
		this.write();
	}
	
	protected boolean isBlinkingChangerON() {
		this.reloadFromConfig();
		return this.blinkingChanger;
	}
	
	public boolean shouldShowSeconds() {
		this.reloadFromConfig();
		return this.showSeconds;
	}

	public void addMinutes(int m) {
		this.reloadFromConfig();
		this.addMinutes = m;
		this.write();
	}
	
	public String getName() {
		this.reloadFromConfig();
		return this.clockName;
	}
	
	public void setName(String clockName) {
		this.clockName = clockName;
		this.write();
	}
	
	public Material getMaterial() {
		this.reloadFromConfig();
		return this.material;
	}

	public byte getData() {
		this.reloadFromConfig();
		return this.data;
	}

	public int getAddMinutes() {
		this.reloadFromConfig();
		return this.addMinutes;
	}
	
	public void updateClockArea(ClockArea ca) {
		this.clockArea = ca;
		this.write();
	}
	
	public ClockArea getClockArea() {
		this.reloadFromConfig();
		return this.clockArea;
	}
	
	protected void setRetrieveData(boolean retrieveData) {
		this.retrieveData = retrieveData;
	}
	
	protected boolean isSomethingMissing() {
		if(this.clockCreator != null && this.clockName != null && this.clockArea != null && this.material != null && this.clockMode != null) {
			return false;
		}
		return true;
	}
}
