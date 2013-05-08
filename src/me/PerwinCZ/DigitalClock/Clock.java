package me.PerwinCZ.DigitalClock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Clock {
	private String clockName;
	private String clockCreator;
	private Block startBlock;
	private Block playersBlock;
	private BlockFace direction;
	private Material material;
	private Material fillingMaterial = Material.AIR;
	private Location location;
	private boolean retrieveData = true;
	private byte data = (byte) 0;
	private byte fillingData = (byte) 0;
	private int addMinutes = 0;
	private boolean showSeconds;
	private boolean blinking;
	private boolean blinkingChanger;
	private boolean ampm;
	private boolean countdown;
	private int countdownto;
	private boolean ingametime;
	
	public Clock(String name, String playerName, Block block, Block playersBlock) {
		this.clockName = name;
		this.clockCreator = playerName;
		this.startBlock = block;
		this.playersBlock = playersBlock;
		this.retrieveData = true;
		this.material = block.getType();
		this.data = block.getData();
		this.location = block.getLocation();
		this.resetBlockFace();
	}
	
	public void generate() {
		if(!this.isSomethingMissing()) {
			this.write();
			Generator.start(this);
		} else {
			throw new NullPointerException();
		}
	}
	
	public void resetBlockFace() {
		if(this.startBlock.getFace(this.playersBlock) != null) {
     		this.direction = this.startBlock.getFace(this.playersBlock);
		} else {
			int px = this.playersBlock.getX();
			int pz = this.playersBlock.getZ();
			if(px < this.startBlock.getX() && pz == this.startBlock.getZ()) {
				this.direction = BlockFace.NORTH;
			} else if(px > this.startBlock.getX() && pz == this.startBlock.getZ()) {
				this.direction = BlockFace.SOUTH;
			} else if(pz < this.startBlock.getZ() && px == this.startBlock.getX()) {
				this.direction = BlockFace.EAST;
			} else /*if(pz > block.getZ() && px == block.getX())*/ {
				this.direction = BlockFace.WEST;
			}
		}
		
	}
	
	public void write() {
		if(!this.isSomethingMissing()) {
			Main.INSTANCE.getConfig().set(this.clockName + ".creator", this.clockCreator);
			Main.INSTANCE.getConfig().set(this.clockName + ".world", this.startBlock.getWorld().getName());
			Main.INSTANCE.getConfig().set(this.clockName + ".x", this.startBlock.getX());
			Main.INSTANCE.getConfig().set(this.clockName + ".y", this.startBlock.getY());
			Main.INSTANCE.getConfig().set(this.clockName + ".z", this.startBlock.getZ());
			Main.INSTANCE.getConfig().set(this.clockName + ".x2", this.playersBlock.getX());
			Main.INSTANCE.getConfig().set(this.clockName + ".y2", this.playersBlock.getY());
			Main.INSTANCE.getConfig().set(this.clockName + ".z2", this.playersBlock.getZ());
			Main.INSTANCE.getConfig().set(this.clockName + ".direction", this.direction.name());
			Main.INSTANCE.getConfig().set(this.clockName + ".material", this.material.name());
			Main.INSTANCE.getConfig().set(this.clockName + ".data", this.data);
			Main.INSTANCE.getConfig().set(this.clockName + ".filling", this.fillingMaterial.name());
			Main.INSTANCE.getConfig().set(this.clockName + ".fdata", this.fillingData);
			Main.INSTANCE.getConfig().set(this.clockName + ".add", this.addMinutes);
			Main.INSTANCE.getConfig().set(this.clockName + ".seconds", this.showSeconds);
			Main.INSTANCE.getConfig().set(this.clockName + ".blinking", this.blinking);
			Main.INSTANCE.getConfig().set(this.clockName + ".changer", this.blinkingChanger);
			Main.INSTANCE.getConfig().set(this.clockName + ".ampm", this.ampm);
			Main.INSTANCE.getConfig().set(this.clockName + ".countdown", this.countdown);
			Main.INSTANCE.getConfig().set(this.clockName + ".cdt", this.countdownto);
			Main.INSTANCE.getConfig().set(this.clockName + ".igt", this.ingametime);
			Main.INSTANCE.saveConfig();
		} else {
			throw new NullPointerException();
		}
	}
	
	public void teleportToClock(Player player) {
		player.teleport(this.playersBlock.getLocation());
	}
	
	public void move(Block block, Block playersblock) {
		Generator.removeClock(this);
		this.startBlock = block;
		this.playersBlock = playersblock;
		this.resetBlockFace();
		this.generate();
	}
	
	public Material getFillingMaterial() {
		this.reloadFromConfig();
		return this.fillingMaterial;
	}
	
	public byte getFillingData() {
		this.reloadFromConfig();
		return this.fillingData;
	}
	
	public boolean isIngameTimeEnabled() {
		this.reloadFromConfig();
		return this.ingametime;
	}
	
	public void enableIngameTime(boolean b) {
		Generator.removeClock(this);
		this.ingametime = b;
		this.write();
		Main.INSTANCE.runClock(this.getName());
	}
	
	public Material setFillingMaterial(int id, int md) {
		this.fillingMaterial = Material.getMaterial(id);
		this.fillingData = (byte) md;
		this.write();
		return this.fillingMaterial;
	}

	public static void remove(Clock clock) {
		if(Main.INSTANCE.clockTasks.containsKey(clock.getName())) {
			Main.INSTANCE.getServer().getScheduler().cancelTask(Main.INSTANCE.clockTasks.get(clock.getName()));
			Main.INSTANCE.clockTasks.remove(clock.getName());
		}
		Generator.removeClock(clock);
		clock.setRetrieveData(false);
		Main.INSTANCE.getConfig().set(clock.getName(), null);
		Main.INSTANCE.saveConfig();
	}
	
    public static Clock loadClockByClockName(String clockName) {
	    if(Main.INSTANCE.getConfig().getKeys(false).contains(clockName)) {
			Location loc = new Location(Main.INSTANCE.getServer().getWorld(Main.INSTANCE.getConfig().getString(clockName + ".world")), Main.INSTANCE.getConfig().getInt(clockName + ".x"), Main.INSTANCE.getConfig().getInt(clockName + ".y"), Main.INSTANCE.getConfig().getInt(clockName + ".z"));
			Location loc2 = new Location(Main.INSTANCE.getServer().getWorld(Main.INSTANCE.getConfig().getString(clockName + ".world")), Main.INSTANCE.getConfig().getInt(clockName + ".x2"), Main.INSTANCE.getConfig().getInt(clockName + ".y2"), Main.INSTANCE.getConfig().getInt(clockName + ".z2"));
	    	return new Clock(clockName, Main.INSTANCE.getConfig().getString(clockName + ".creator"), Main.INSTANCE.getServer().getWorld(Main.INSTANCE.getConfig().getString(clockName + ".world")).getBlockAt(loc), Main.INSTANCE.getServer().getWorld(Main.INSTANCE.getConfig().getString(clockName + ".world")).getBlockAt(loc2));
	    }
		return null;
	}
	
    public void reloadFromConfig() {
    	if(Main.INSTANCE.getConfig().getKeys(false).contains(this.clockName) && this.retrieveData == true) {
        	this.location = new Location(Main.INSTANCE.getServer().getWorld(Main.INSTANCE.getConfig().getString(this.clockName + ".world")), Main.INSTANCE.getConfig().getInt(this.clockName + ".x"), Main.INSTANCE.getConfig().getInt(this.clockName + ".y"), Main.INSTANCE.getConfig().getInt(this.clockName + ".z"));
        	this.clockCreator = Main.INSTANCE.getConfig().getString(this.clockName + ".creator");
        	this.direction = BlockFace.valueOf(Main.INSTANCE.getConfig().getString(this.clockName + ".direction"));
        	this.material = Material.valueOf(Main.INSTANCE.getConfig().getString(this.clockName + ".material"));
        	this.data = (byte) Main.INSTANCE.getConfig().getInt(this.clockName + ".data");
        	this.fillingMaterial = Material.valueOf(Main.INSTANCE.getConfig().getString(this.clockName + ".filling"));
        	this.fillingData = (byte) Main.INSTANCE.getConfig().getInt(this.clockName + ".fdata");
        	this.addMinutes = Integer.parseInt(Main.INSTANCE.getConfig().getString(this.clockName + ".add"));
        	this.showSeconds = Boolean.parseBoolean(Main.INSTANCE.getConfig().getString(this.clockName + ".seconds"));
        	this.blinking = Boolean.parseBoolean(Main.INSTANCE.getConfig().getString(this.clockName + ".blinking"));
        	this.blinkingChanger = Boolean.parseBoolean(Main.INSTANCE.getConfig().getString(this.clockName + ".changer"));
        	this.ampm = Boolean.parseBoolean(Main.INSTANCE.getConfig().getString(this.clockName + ".ampm"));
        	this.countdown = Boolean.parseBoolean(Main.INSTANCE.getConfig().getString(this.clockName + ".countdown"));
        	this.countdownto = Main.INSTANCE.getConfig().getInt(this.clockName + ".cdt");
        	this.ingametime = Boolean.parseBoolean(Main.INSTANCE.getConfig().getString(this.clockName + ".igt"));
        }
    }
    
    public BlockFace rotate(String direction) {
		Generator.removeClock(this);
		this.direction = BlockFace.valueOf(direction.toUpperCase());
    	this.generate();
		return this.direction;
    }
    
    public Material changeMaterial(int id, int md) {
    	this.material = Material.getMaterial(id);
    	this.data = (byte) md;
    	this.generate();
		return this.material;
    }
    
	public String getCreator() {
		this.reloadFromConfig();
		if(this.clockCreator != null) {
			return this.clockCreator;
		}
		return null;
	}
	
	public boolean isCountdownEnabled() {
		this.reloadFromConfig();
		return this.countdown;
	}
	
	public void enableCountdown(boolean c) {
		this.reloadFromConfig();
		this.countdown = c;
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
		Generator.removeClock(this);
		this.showSeconds = ss;
		this.write();
		Main.INSTANCE.runClock(this.getName());
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
		Generator.removeClock(this);
		this.ampm = ap;
		this.write();
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
		if(this.clockName != null) {
			return this.clockName;
		}
		return null;
	}
	
	public void setName(String clockName) {
		this.clockName = clockName;
		this.write();
	}
	
	public Location getStartBlockLocation() {
		this.reloadFromConfig();
		if(this.location != null) {
			return this.location;
		}
		return null;
	}
	
	public void setStartBlockLocation(Location location) {
		this.location = location;
		this.write();
	}
	
	public Material getMaterial() {
		this.reloadFromConfig();
		if(this.material != null) {
			return this.material;
		}
		return null;
	}

	public byte getData() {
		this.reloadFromConfig();
		return this.data;
	}
	

	public int getAddMinutes() {
		this.reloadFromConfig();
		return this.addMinutes;
	}
	
	public Block getStartBlock() {
		this.reloadFromConfig();
		if(this.startBlock != null) {
			return this.startBlock;
		}
		return null;
	}
	
	public void setStartBlock(Block block) {
		this.startBlock = block;
		this.write();
	}
	
	public void setPlayersBlock(Block block) {
		this.playersBlock = block;
		this.write();
	}
	
	public BlockFace getDirection() {
		this.reloadFromConfig();
		if(this.direction != null) {
			return this.direction;
		}
		return null;
	}
	
	protected void setRetrieveData(boolean retrieveData) {
		this.retrieveData = retrieveData;
	}
	
	protected boolean isSomethingMissing() {
		if(this.clockCreator != null && this.clockName != null && this.startBlock != null && this.direction != null && this.location != null && this.material != null) {
			return false;
		}
		return true;
	}
	
}
