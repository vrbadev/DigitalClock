package cz.perwin.digitalclock.core;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class ClockArea {
	private Clock clock;
	private Block startBlock;
	private Block playersBlock;
	private BlockFace direction;
	private int width;
	private int height;
	private int depth;
	
	public ClockArea(Clock clock, Block startBlock, Block playersBlock, int de) {
		this.clock = clock;
		this.startBlock = startBlock;
		this.playersBlock = playersBlock;
		this.setDirection();
		this.setDimensions();
		this.depth = de;
	}
	
	protected ClockArea(Clock clock, Location sbL, Location pbL, BlockFace bf, int de) {
		this.clock = clock;
		this.startBlock = sbL.getBlock();
		this.playersBlock = pbL.getBlock();
		this.direction = bf;
		this.setDimensions();
		this.depth = de;
	}
	
	public void setDimensions() {
		this.width = 5 * Generator.getGenerator().getMain().getSettingsWidth() + 3;
		if(this.getClock().showSeconds) {
			this.width += 3 * Generator.getGenerator().getMain().getSettingsWidth() + 1;
		}
		if(this.getClock().ampm) {
			this.width += 2 * Generator.getGenerator().getMain().getSettingsWidth() + 2;
		}
		
		this.height = Generator.getGenerator().getMain().getConfig().getInt("height");
	}
	
	private void setDirection() {
		if(this.getStartBlock().getFace(this.getPlayersBlock()) != null) {
     		this.direction = this.getStartBlock().getFace(this.getPlayersBlock());
		} else {
			int px = this.getPlayersBlock().getX();
			int pz = this.getPlayersBlock().getZ();
			int bx = this.getStartBlock().getX();
			int bz = this.getStartBlock().getZ();
			if(px < bx && pz == bz) {
				this.direction = BlockFace.NORTH;
			} else if(px > bx && pz == bz) {
				this.direction = BlockFace.SOUTH;
			} else if(pz < bz && px == bx) {
				this.direction = BlockFace.EAST;
			} else /*if(pz > block.getZ() && px == block.getX())*/ {
				this.direction = BlockFace.WEST;
			}
		}	
	}
	
	public static void resetDimensions(Clock clock) {
		ClockArea ca = clock.getClockArea();
		ca.setDimensions();
		clock.updateClockArea(ca);
		clock.write();
	}
	
	public BlockFace rotate(String direction) {
		Generator.removeClockAndRestore(this.getClock());
		this.direction = BlockFace.valueOf(direction.toUpperCase());
		this.getClock().updateClockArea(this);
		this.getClock().writeAndGenerate();
		return this.direction;
    }
	
	public void setDepth(int de) {
		Generator.removeClockAndRestore(this.getClock());
		this.depth = de;
		this.getClock().updateClockArea(this);
		this.getClock().writeAndGenerate();
	}
	
	public void move(Block block, Block playersblock) {
		Generator.removeClockAndRestore(this.getClock());
		this.startBlock = block;
		this.playersBlock = playersblock;
		this.setDirection();
		this.getClock().updateClockArea(this);
		this.getClock().writeAndGenerate();
	}
	
	public void setStartBlock(Block block) {
		this.startBlock = block;
		this.getClock().write();
	}
	
	public void setPlayersBlock(Block block) {
		this.playersBlock = block;
		this.getClock().write();
	}
	
	public Location getLocation(int up, int side, int before, int behind) {
		int x = this.getStartBlock().getX();
		int y = this.getStartBlock().getY() + up;
		int z = this.getStartBlock().getZ();
		if(this.getDirection() == BlockFace.NORTH) {
			z += side + before;
			x += behind;
		} else if(this.getDirection() == BlockFace.EAST) {
			x -= side + before;
			z += behind;
		} else if(this.getDirection() == BlockFace.SOUTH) {
			z -= side + before;
			x -= behind;
		} else {
			x += side + before;
			z -= behind;
		}
		return new Location(this.getStartBlock().getWorld(), x, y, z);
	}
	
	public Block getStartBlock() {
		return this.startBlock;
	}
	
	public Block getPlayersBlock() {
		return this.playersBlock;
	}
	
	public BlockFace getDirection() {
		return this.direction;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getDepth() {
		return this.depth;
	}
	
	public Clock getClock() {
		return this.clock;
	}
	
	public static boolean containsAny(Location l) {
		for(String clockName : Generator.getGenerator().getMain().getClocksL()) {
			Clock clock = Clock.loadClockByClockName(clockName);
			if(clock.getClockArea().contains(l)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean contains(Location l) {
		Location max = this.getLocation(this.getHeight()-1, this.getWidth()-1, 0, this.getDepth()-1);
		if(Math.abs(l.getBlockX()-this.getStartBlock().getX()) <= Math.abs(max.getBlockX()-this.getStartBlock().getX()) && Math.abs(max.getBlockX()-l.getBlockX()) <= Math.abs(max.getBlockX()-this.getStartBlock().getX()) && l.getBlockY() >= this.getStartBlock().getY() && l.getBlockY() <= max.getBlockY() && Math.abs(l.getBlockZ()-this.getStartBlock().getZ()) <= Math.abs(max.getBlockZ()-this.getStartBlock().getZ()) && Math.abs(max.getBlockZ()-l.getBlockZ()) <= Math.abs(max.getBlockZ()-this.getStartBlock().getZ())) {
			return true;
		}
		return false;
	}
}
