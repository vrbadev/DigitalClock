package me.PerwinCZ.DigitalClock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Clock {
	private String clockName;
	private String clockCreator;
	private Block startBlock;
	private Block playersBlock;
	private BlockFace direction;
	private Material material;
	private Location location;
	private boolean retrieveData = true;
	
	public Clock(String name, String playerName, Block block, Block playersBlock) {
		this.clockName = name;
		this.clockCreator = playerName;
		this.startBlock = block;
		this.playersBlock = playersBlock;
		
		if(block.getFace(playersBlock) != null) {
     		this.direction = block.getFace(playersBlock);
		} else {
			int px = playersBlock.getX();
			int pz = playersBlock.getZ();
			if(px < block.getX() && pz == block.getZ()) {
				this.direction = BlockFace.NORTH;
			} else if(px > block.getX() && pz == block.getZ()) {
				this.direction = BlockFace.SOUTH;
			} else if(pz < block.getZ() && px == block.getX()) {
				this.direction = BlockFace.EAST;
			} else /*if(pz > block.getZ() && px == block.getX())*/ {
				this.direction = BlockFace.WEST;
			}
		}
		
		this.material = block.getType();
		this.location = block.getLocation();
	}
	
	public void generate() {
		if(!this.isSomethingMissing()) {
			this.write();
			Generator.start(this);
		} else {
			throw new NullPointerException();
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
			Events.plugin.saveConfig();
		} else {
			throw new NullPointerException();
		}
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
    	}
    }
    
    public BlockFace rotate(String direction) {
		Generator.removeClock(this);
		this.direction = BlockFace.valueOf(direction.toUpperCase());
    	this.write();
		Generator.start(this);
		return this.direction;
    }
    
    public Material changeMaterial(int id) {
    	this.material = Material.getMaterial(id);
    	this.write();
		Generator.start(this);
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
