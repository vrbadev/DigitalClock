package me.PerwinCZ.DigitalClock;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Generator {
	public static void start(Clock clock) {
		Location l = clock.getStartBlockLocation();
		BlockFace b = clock.getDirection();
		Material m = clock.getMaterial();
		
    	Calendar cal = Calendar.getInstance();
    	String hours = new SimpleDateFormat("HH").format(cal.getTime()) + "";
    	String minutes = new SimpleDateFormat("mm").format(cal.getTime()) + "";
    	String seconds = new SimpleDateFormat("ss").format(cal.getTime()) + "";
    	
		Generator.generate(0, Character.digit(hours.charAt(0), 10), l, b, m);
		Generator.generate(4, hours.charAt(1) - '0', l, b, m);
		Generator.generate(7, 10, l, b, m);
		Generator.generate(10, minutes.charAt(0) - '0', l, b, m);
		Generator.generate(14, minutes.charAt(1) - '0', l, b, m);
		Generator.generate(17, 10, l, b, m);
		Generator.generate(20, seconds.charAt(0) - '0', l, b, m);
		Generator.generate(24, seconds.charAt(1) - '0', l, b, m);
	}
	
	private static void generate(int i, int n, Location l, BlockFace b, Material m) {
		World w = l.getWorld();
		String[] nums = {
				"1,1,1,1,1;1,0,0,0,1;1,1,1,1,1", 
				"0,1,0,0,1;1,1,1,1,1;0,0,0,0,1", 
				"1,0,1,1,1;1,0,1,0,1;1,1,1,0,1", 
				"1,0,1,0,1;1,0,1,0,1;1,1,1,1,1", 
				"1,1,1,0,0;0,0,1,0,0;1,1,1,1,1",
				"1,1,1,0,1;1,0,1,0,1;1,0,1,1,1",
				"1,1,1,1,1;1,0,1,0,1;1,0,1,1,1",
				"1,0,0,0,0;1,0,0,0,0;1,1,1,1,1",
				"1,1,1,1,1;1,0,1,0,1;1,1,1,1,1",
				"1,1,1,0,1;1,0,1,0,1;1,1,1,1,1",
				"0,0,0,0,0;0,1,0,1,0;0,0,0,0,0"
				};
		for(int q = 0; q < 3; q++) {
			int x = l.getBlockX();
			int z = l.getBlockZ();
			if(b == BlockFace.NORTH) {
				// roste na vychod (z+)
				z = z + q + i;
			} else if(b == BlockFace.EAST) {
				// zmensuje se na jih (x-)
				x = x - q - i;
			} else if(b == BlockFace.SOUTH) {
				// zmensuje se na zapad (z-)
				z = z - q - i;
			} else {
				// roste na sever (x+)
				x = x + q + i;
			}
			for(int p = 0; p < 5; p++) {
				int y = (l.getBlockY()+p);
				Block newBlock = w.getBlockAt(x,y,z);
				String[] r = nums[n].split(";");
				String[] r2 = new StringBuffer(r[q]).reverse().toString().split(",");
				if(r2[p].equals("1")) {
					newBlock.setType(m);
				} else {
					newBlock.setType(Material.AIR);
				}
			}
		}
	}
	
	public static void removeClock(Clock clock) {
		Location l = clock.getStartBlockLocation();
		BlockFace b = clock.getDirection();
		
		World w = l.getWorld();
		for(int u = 0; u < 27; u++) {
			int x = l.getBlockX();
			int z = l.getBlockZ();
			if(b == BlockFace.NORTH) {
				z += u;
			} else if(b == BlockFace.EAST) {
				x -= u;
			} else if(b == BlockFace.SOUTH) {
				z -= u;
			} else {
				x += u;
			}
			for(int v = 0; v < 5; v++) {
				int y = l.getBlockY() + v;
				w.getBlockAt(x,y,z).setType(Material.AIR);
			}
		}
	}
}
