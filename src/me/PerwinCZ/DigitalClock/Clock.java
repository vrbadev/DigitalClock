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
			Events.plugin.getConfig().set(this.clockName + ".creator", this.clockCreator);
			Events.plugin.getConfig().set(this.clockName + ".world", this.startBlock.getWorld().getName());
			Events.plugin.getConfig().set(this.clockName + ".x", this.startBlock.getX());
			Events.plugin.getConfig().set(this.clockName + ".y", this.startBlock.getY());
			Events.plugin.getConfig().set(this.clockName + ".z", this.startBlock.getZ());
			Events.plugin.getConfig().set(this.clockName + ".x2", this.playersBlock.getX());
			Events.plugin.getConfig().set(this.clockName + ".y2", this.playersBlock.getY());
			Events.plugin.getConfig().set(this.clockName + ".z2", this.playersBlock.getZ());
			Events.plugin.getConfig().set(this.clockName + ".direction", this.direction.name());
			Events.plugin.getConfig().set(this.clockName + ".material", this.material.name());
			Events.plugin.getConfig().set(this.clockName + ".data", this.data);
			Events.plugin.getConfig().set(this.clockName + ".filling", this.fillingMaterial.name());
			Events.plugin.getConfig().set(this.clockName + ".fdata", this.fillingData);
			Events.plugin.getConfig().set(this.clockName + ".add", this.addMinutes);
			Events.plugin.getConfig().set(this.clockName + ".seconds", this.showSeconds);
			Events.plugin.getConfig().set(this.clockName + ".blinking", this.blinking);
			Events.plugin.getConfig().set(this.clockName + ".changer", this.blinkingChanger);
			Events.plugin.getConfig().set(this.clockName + ".ampm", this.ampm);
			Events.plugin.saveConfig();
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
	
	public Material setFillingMaterial(int id, int md) {
		this.fillingMaterial = Material.getMaterial(id);
		this.fillingData = (byte) md;
		this.write();
		return this.fillingMaterial;
	}

	public static void remove(Clock clock) {
		Generator.removeClock(clock);
		clock.setRetrieveData(false);
		Events.plugin.getConfig().set(clock.getName(), null);
		Events.plugin.saveConfig();
	}
	
    public static Clock loadClockByClockName(String clockName) {
	    if(Events.plugin.getConfig().getKeys(false).contains(clockName)) {
			Location loc = new Location(Events.plugin.getServer().getWorld(Events.plugin.getConfig().getString(clockName + ".world")), Events.plugin.getConfig().getInt(clockName + ".x"), Events.plugin.getConfig().getInt(clockName + ".y"), Events.plugin.getConfig().getInt(clockName + ".z"));
			Location loc2 = new Location(Events.plugin.getServer().getWorld(Events.plugin.getConfig().getString(clockName + ".world")), Events.plugin.getConfig().getInt(clockName + ".x2"), Events.plugin.getConfig().getInt(clockName + ".y2"), Events.plugin.getConfig().getInt(clockName + ".z2"));
	    	return new Clock(clockName, Events.plugin.getConfig().getString(clockName + ".creator"), Events.plugin.getServer().getWorld(Events.plugin.getConfig().getString(clockName + ".world")).getBlockAt(loc), Events.plugin.getServer().getWorld(Events.plugin.getConfig().getString(clockName + ".world")).getBlockAt(loc2));
	    }
		return null;
	}
	
    public void reloadFromConfig() {
    	if(Events.plugin.getConfig().getKeys(false).contains(this.clockName) && this.retrieveData == true) {
        	this.location = new Location(Events.plugin.getServer().getWorld(Events.plugin.getConfig().getString(this.clockName + ".world")), Events.plugin.getConfig().getInt(this.clockName + ".x"), Events.plugin.getConfig().getInt(this.clockName + ".y"), Events.plugin.getConfig().getInt(this.clockName + ".z"));
        	this.clockCreator = Events.plugin.getConfig().getString(this.clockName + ".creator");
        	this.direction = BlockFace.valueOf(Events.plugin.getConfig().getString(this.clockName + ".direction"));
        	this.material = Material.valueOf(Events.plugin.getConfig().getString(this.clockName + ".material"));
        	this.data = (byte) Events.plugin.getConfig().getInt(this.clockName + ".data");
        	this.fillingMaterial = Material.valueOf(Events.plugin.getConfig().getString(this.clockName + ".filling"));
        	this.fillingData = (byte) Events.plugin.getConfig().getInt(this.clockName + ".fdata");
        	this.addMinutes = Integer.parseInt(Events.plugin.getConfig().getString(this.clockName + ".add"));
        	this.showSeconds = Boolean.parseBoolean(Events.plugin.getConfig().getString(this.clockName + ".seconds"));
        	this.blinking = Boolean.parseBoolean(Events.plugin.getConfig().getString(this.clockName + ".blinking"));
        	this.blinkingChanger = Boolean.parseBoolean(Events.plugin.getConfig().getString(this.clockName + ".changer"));
        	this.ampm = Boolean.parseBoolean(Events.plugin.getConfig().getString(this.clockName + ".ampm"));
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
	
	public void setCreator(String creator) {
		this.clockCreator = creator;
		this.write();
	}
	
	public void setShowingSeconds(boolean ss) {
		Generator.removeClock(this);
		this.showSeconds = ss;
		this.write();
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
	
	public void setBlinkingChanger(boolean blm) {
		this.blinkingChanger = blm;
		this.write();
	}
	
	public boolean isBlinkingChangerON() {
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
	
	public void setRetrieveData(boolean retrieveData) {
		this.retrieveData = retrieveData;
	}
	
	public boolean isSomethingMissing() {
		if(this.clockCreator != null && this.clockName != null && this.startBlock != null && this.direction != null && this.location != null && this.material != null) {
			return false;
		}
		return true;
	}
	
}
