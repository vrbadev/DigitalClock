package cz.perwin.digitalclock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.timeZone;

public class Generator {
	private Main i;
	
	protected Generator(Main i) {
		this.i = i;
	}
	
	public void start(Clock clock) {
		int mins = clock.getAddMinutes();
		boolean cd = clock.isCountdownEnabled();
		int cdt = clock.getCountdownTime();
		World w = clock.getStartBlockLocation().getWorld();
		boolean igt = clock.isIngameTimeEnabled();

		this.i.getGenerator().createBackup(clock);

		String hours = this.i.getGenerator().getRealNumbers(mins, null)[0];
		String minutes = this.i.getGenerator().getRealNumbers(mins, null)[1];
		String seconds = this.i.getGenerator().getRealNumbers(mins, null)[2];
		
		if(cd && !igt && cdt != 0 && cdt < 360000) {
			hours = this.i.getGenerator().getCountdownNumbers(cdt)[0];
			minutes = this.i.getGenerator().getCountdownNumbers(cdt)[1];
			seconds = this.i.getGenerator().getCountdownNumbers(cdt)[2];
			clock.setCountdownTime(cdt-1);
		} else if(cd && !igt && cdt == 0) {
			this.i.getServer().getScheduler().cancelTask(this.i.getClockTasks().get(clock.getName()));
			this.i.getClockTasks().remove(clock.getName());
			clock.enableCountdown(false);
			CountdownEndEvent event = new CountdownEndEvent(clock);
			this.i.getServer().getPluginManager().callEvent(event);
		} else if(igt && !cd) {
			hours = this.i.getGenerator().getIngameNumbers(w)[0];
			minutes = this.i.getGenerator().getIngameNumbers(w)[1];
			seconds = this.i.getGenerator().getIngameNumbers(w)[2];
		}
		
		if(this.i.separately && !cd && !igt) {
			for(Player online : Bukkit.getServer().getOnlinePlayers()) {
				InetAddress ia = online.getAddress().getAddress();
				if(!ia.getHostAddress().equals("127.0.0.1") && !ia.getHostAddress().startsWith("192.168.") && !ia.getHostAddress().startsWith("25.")) {
					TimeZone tz = this.i.getGenerator().getTimeZone(ia);
					hours = this.i.getGenerator().getRealNumbers(mins, tz)[0];
					minutes = this.i.getGenerator().getRealNumbers(mins, tz)[1];
					seconds = this.i.getGenerator().getRealNumbers(mins, tz)[2];
					this.i.getGenerator().generatingSequence(clock, hours, minutes, seconds, online);
				} else {
					this.i.getGenerator().generatingSequence(clock, hours, minutes, seconds, online);
				}
			}
		} else {
			this.i.getGenerator().generatingSequence(clock, hours, minutes, seconds, null);
		}
	}
	
	protected String[] getIngameNumbers(World w) {
		long time = w.getTime();
		if(time < 18000) {
			time += 6000;
		} else {
			time -= 18000;
		}
		long m = ((time % 1000) * 60 / 1000);
		String minutes = m + "";
		if(m < 10) {
			minutes = "0" + minutes;
		}
		String hours = time/1000 + "";
		if(Integer.parseInt(hours) < 10) {
			hours = "0" + hours;
		}
		String seconds = "00";
    	String[] result = {hours, minutes, seconds};
    	return result;
	}
	
	public String[] getCountdownNumbers(int cdt) {
		String hours = "00";
		String minutes = "00";
		String seconds = "00";
		int ho = (int) Math.floor(cdt/3600);
		int mi = (int) Math.floor((cdt-ho*3600)/60);
		int se = cdt-ho*3600-mi*60;
		if(ho < 100 && mi < 60 && se < 60) {
			hours = ho + "";
			if(ho < 10) {
				hours = "0" + hours;
			}
			minutes = mi + "";
			if(mi < 10) {
				minutes = "0" + minutes;
			}
			seconds = se + "";
			if(se < 10) {
				seconds = "0" + seconds;
			}
		}
    	String[] result = {hours, minutes, seconds};
    	return result;
	}
	
	public String[] getRealNumbers(int mins, TimeZone tz) {
    	Calendar cal = Calendar.getInstance();
    	if(tz != null) {
        	cal.setTimeZone(tz);
    	}
    	cal.add(Calendar.MINUTE, mins);
    	String hours = new SimpleDateFormat("HH").format(cal.getTime()) + "";
    	String minutes = new SimpleDateFormat("mm").format(cal.getTime()) + "";
    	String seconds = new SimpleDateFormat("ss").format(cal.getTime()) + "";
    	String[] result = {hours, minutes, seconds};
    	return result;
	}
	
	public void generatingSequence(Clock clock, String hours, String minutes, String seconds, Player online) {
		Location l = clock.getStartBlockLocation();
		BlockFace b = clock.getDirection();
		Material m = clock.getMaterial();
		Material f = clock.getFillingMaterial();
		byte d = clock.getData();
		byte fd = clock.getFillingData();
		boolean ss = clock.shouldShowSeconds();
		boolean bl = clock.isBlinking();
		boolean blm = clock.isBlinkingChangerON();
		boolean ampm = clock.getAMPM();

    	String letter = null;
    	int newHours = 0;
    	if(ampm) {
    		newHours = Integer.parseInt(hours);
    		if(newHours > 11 && newHours < 24) {
    			if(newHours != 12) {
        			newHours -= 12;
    			}
    			hours = Integer.toString(newHours);
    			if(newHours < 10) {
    				hours = "0" + hours;
    			}
    			letter = "P";
    		} else {
    			if(newHours == 0) {
    				hours = "12";
    			}
    			letter = "A";
    		}
    	}
    	
		this.i.getGenerator().generate(0, Character.digit(hours.charAt(0), 10), l, b, m, d, f, fd, online);
		this.i.getGenerator().generate(this.i.settings_width + 1, hours.charAt(1) - '0', l, b, m, d, f, fd, online);
		
		if(bl) { 
			if(blm) {
				this.i.getGenerator().generate(this.i.settings_width*2 + 1, 10, l, b, f, d, f, fd, online);
				clock.setBlinkingChanger(false);
			} else {
				this.i.getGenerator().generate(this.i.settings_width*2 + 1, 10, l, b, m, d, f, fd, online); 
				clock.setBlinkingChanger(true);
			}
		} else {
			this.i.getGenerator().generate(this.i.settings_width*2 + 1, 10, l, b, m, d, f, fd, online);
		}
		
		this.i.getGenerator().generate(this.i.settings_width*3 + 1, minutes.charAt(0) - '0', l, b, m, d, f, fd, online);
		this.i.getGenerator().generate(this.i.settings_width*4 + 2, minutes.charAt(1) - '0', l, b, m, d, f, fd, online);
		
		if(ss) {
			if(bl) { 
				if(blm) {
			     	this.i.getGenerator().generate(this.i.settings_width*5 + 2, 10, l, b, f, d, f, fd, online);
					clock.setBlinkingChanger(false);
				} else {
			     	this.i.getGenerator().generate(this.i.settings_width*5 + 2, 10, l, b, m, d, f, fd, online); 
					clock.setBlinkingChanger(true);
				}
			} else {
		     	this.i.getGenerator().generate(this.i.settings_width*5 + 2, 10, l, b, m, d, f, fd, online);
			}
			
	    	this.i.getGenerator().generate(this.i.settings_width*6 + 2, seconds.charAt(0) - '0', l, b, m, d, f, fd, online);
	    	this.i.getGenerator().generate(this.i.settings_width*7 + 3, seconds.charAt(1) - '0', l, b, m, d, f, fd, online);
	    	
	    	if(ampm && letter != null) {
	    		if(letter == "A") {
	    	    	this.i.getGenerator().generate(this.i.settings_width*8 + 4, 11, l, b, m, d, f, fd, online);
	    		} else {
	    	    	this.i.getGenerator().generate(this.i.settings_width*8 + 4, 12, l, b, m, d, f, fd, online);
	    		}
    	    	this.i.getGenerator().generate(this.i.settings_width*9 + 5, 13, l, b, m, d, f, fd, online);
	    	}
		} else {
	    	if(ampm && letter != null) {
	    		if(letter == "A") {
	    	    	this.i.getGenerator().generate(this.i.settings_width*5 + 3, 11, l, b, m, d, f, fd, online);
	    		} else {
	    	    	this.i.getGenerator().generate(this.i.settings_width*5 + 3, 12, l, b, m, d, f, fd, online);
	    		}
		    	this.i.getGenerator().generate(this.i.settings_width*6 + 4, 13, l, b, m, d, f, fd, online);
	    	}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void generate(int i, int n, Location l, BlockFace b, Material m, byte d, Material f, byte fd, Player online) {
		World w = l.getWorld();
		for(int q = 0; q < this.i.settings_width; q++) {
			int x = l.getBlockX();
			int z = l.getBlockZ();
			if(b == BlockFace.NORTH) {
				z = z + q + i;
			} else if(b == BlockFace.EAST) {
				x = x - q - i;
			} else if(b == BlockFace.SOUTH) {
				z = z - q - i;
			} else {
				x = x + q + i;
			}
			for(int p = 0; p < this.i.getConfig().getInt("height"); p++) {
				int y = (l.getBlockY()+p);
				Location newLoc = new Location(w, x, y, z);
				Block newBlock = w.getBlockAt(newLoc);
				String[] r = this.i.getConfig().getString("num" + n).split(";");
				String[] r2 = new StringBuffer(r[q]).reverse().toString().split(",");
				Material mat;
				byte md;
				if(r2[p].equals("1")) {
					mat = m;
					md = d;
				} else {
					mat = f;
					md = fd;
				}
				if(online != null) {
					online.sendBlockChange(newLoc, mat, md);
				} else {
					newBlock.setType(mat);
					newBlock.setData(md);
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void removeClock(Clock clock) {
		if(clock.isSomethingMissing()) {
			clock.reloadFromConfig();
		}
		Location l = clock.getStartBlockLocation();
		BlockFace b = clock.getDirection();
		World w = l.getWorld();
		
		File file = new File(new File("plugins/DigitalClock/terrainbackups"), clock.getName() + ".txt");
		BufferedReader br;
		String line;
		ArrayList<String> lines = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader(file));
			while((line = br.readLine()) != null) {
				lines.add(line);
			}
			br.close();
		} catch (IOException e) {
			this.i.console.severe(e + "");
		}
		
		int n = 0;
		for(int u = 0; u < getTotalWidth(clock); u++) {
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
			for(int v = 0; v < this.i.getConfig().getInt("height"); v++) {
				int y = l.getBlockY() + v;
				String toSplit = lines.get(n);
				String[] data = toSplit.split(":");
				Location newLoc = new Location(w, x, y, z);
				Material mat = Material.valueOf(data[0]);
				byte md = (byte) Integer.parseInt(data[1]);
				w.getBlockAt(newLoc).setType(mat);
				w.getBlockAt(newLoc).setData(md);
				if(this.i.separately) {
					for(Player online : Bukkit.getServer().getOnlinePlayers()) {
						online.sendBlockChange(newLoc, mat, md);
					}
				}
				n++;
			}
		}
		file.delete();
	}
	
	@SuppressWarnings("deprecation")
	private void createBackup(Clock clock) {
		File dir = new File("plugins/DigitalClock/terrainbackups");
		if(!dir.exists()) {
			if(dir.mkdir()) {
				this.i.console.info("[DigitalClock] Directory terrainbackups has been successfully created.");
			} else {
				this.i.console.info("[DigitalClock] Directory terrainbackups couldn't be created!");
			}
		}
		File file = new File(dir, clock.getName() + ".txt");
		if(!file.exists()) {
			try {
				Location l = clock.getStartBlockLocation();
				BlockFace b = clock.getDirection();
				World w = l.getWorld();
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				for(int u = 0; u < getTotalWidth(clock); u++) {
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
					for(int v = 0; v < this.i.getConfig().getInt("height"); v++) {
						int y = l.getBlockY() + v;
						String block;
						if(u == 0 && v == 0) {
							block = "AIR:0";
						} else {
							block = w.getBlockAt(x,y,z).getType().name() + ":" + w.getBlockAt(x,y,z).getData();
						}
						writer.write(block);
						writer.newLine();
					}
				}
				writer.close();
			} catch (IOException e) {
				this.i.console.severe(e + "");
			}
		}
	}
	
	private int getTotalWidth(Clock clock) {
		int start = 5 * this.i.settings_width + 3;
		if(clock.shouldShowSeconds()) {
			start += 3 * this.i.settings_width + 1;
		}
		if(clock.getAMPM()) {
			start += 2 * this.i.settings_width + 2;
		}
		return start;
	}
	
	private TimeZone getTimeZone(InetAddress ia) {
		TimeZone tz = null;
		LookupService ls = null;
		try {
			ls = new LookupService(new File(this.i.getDataFolder(), "GeoLiteCity.dat"), LookupService.GEOIP_MEMORY_CACHE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		com.maxmind.geoip.Location loc = ls.getLocation(ia);
		if(loc != null) {
			tz = TimeZone.getTimeZone(timeZone.timeZoneByCountryAndRegion(loc.countryCode, loc.region));
		}
		return tz;
	}
}
