package cz.perwin.digitalclock.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.timeZone;

import cz.perwin.digitalclock.DigitalClock;

public class Generator {
	private static Generator generator;
	private DigitalClock i;
	
	public Generator(DigitalClock i) {
		this.i = i;
		Generator.generator = this;
	}
	
	public void generateOnce(Clock clock) {
		int mins = clock.getAddMinutes();
		ClockMode cm = clock.getClockMode();
		World w = clock.getClockArea().getStartBlock().getWorld();

		this.createBackup(clock);

		String[] realnum = this.getRealNumbers(mins, null);
		String hours = realnum[0];
		String minutes = realnum[1];
		String seconds = realnum[2];
		
		switch(cm) {
		case COUNTDOWN:
			int cdt = clock.getCountdownTime();
			if(cdt != 0 && cdt < 360000) {
				String[] num = this.getNumbersFromSeconds(cdt);
				hours = num[0];
				minutes = num[1];
				seconds = num[2];
				clock.setCountdownTime(cdt-1);
			} else { // cdt == 0
				Clock.stopTask(clock.getName());
				clock.enableCountdown(false);
				CountdownEndEvent event = new CountdownEndEvent(clock);
				this.i.getServer().getPluginManager().callEvent(event);
				hours = "00";
				minutes = "00";
				seconds = "00";
			}
			break;
		case INGAMETIME:
			hours = this.getIngameNumbers(w)[0];
			minutes = this.getIngameNumbers(w)[1];
			seconds = this.getIngameNumbers(w)[2];
			break;
		case STOPWATCH:
			int swt = clock.getStopwatchTime();
			String[] num = this.getNumbersFromSeconds(swt);
			hours = num[0];
			minutes = num[1];
			seconds = num[2];
			clock.setStopwatchTime(swt+1);
			break;
		default:
			break;
		}
		
		if(this.i.shouldGenerateSeparately() && cm == ClockMode.NORMAL) {
			for(Player online : Bukkit.getServer().getOnlinePlayers()) {
				InetAddress ia = online.getAddress().getAddress();
				if(!ia.getHostAddress().equals("127.0.0.1") && !ia.getHostAddress().startsWith("192.168.") && !ia.getHostAddress().startsWith("25.")) {
					TimeZone tz = this.getTimeZone(ia);
					realnum = this.getRealNumbers(mins, tz);
					hours = realnum[0];
					minutes = realnum[1];
					seconds = realnum[2];
					this.generatingSequence(clock, hours, minutes, seconds, online);
				} else {
					this.generatingSequence(clock, hours, minutes, seconds, online);
				}
			}
		} else {
			this.generatingSequence(clock, hours, minutes, seconds, null);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void createBackup(Clock clock) {
		File dir = new File(this.i.getDataFolder(), "terrainbackups");
		if(!dir.exists()) {
			if(dir.mkdir()) {
				this.i.getConsole().info("[DigitalClock] Directory terrainbackups has been successfully created.");
			} else {
				this.i.getConsole().info("[DigitalClock] Directory terrainbackups couldn't be created!");
			}
		}
		File file = new File(dir, clock.getName() + ".txt");
		if(!file.exists()) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				for(int u = 0; u < clock.getClockArea().getWidth(); u++) {
					for(int v = 0; v < clock.getClockArea().getHeight(); v++) {
						Block b = clock.getClockArea().getLocation(v, u, 0).getBlock();
						String block;
						if(u == 0 && v == 0) {
							block = "AIR:0";
						} else {
							block = b.getType().name() + ":" + b.getData();
						}
						writer.write(block);
						writer.newLine();
					}
				}
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	private String[] getIngameNumbers(World w) {
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
	
	public String[] getNumbersFromSeconds(int cdt) {
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
	
	public void generatingSequence(Clock clock, final String phours, final String minutes, final String seconds, final Player online) {
		final Date date = new Date();
		Generator.GeneratingSequence ggs = new Generator.GeneratingSequence() {
			@Override
			public void generate(final ClockThread ct) {
				getMain().getServer().getScheduler().scheduleSyncDelayedTask(getMain(), new Runnable() {
					@Override
					public void run() {
						ClockArea ca = ct.getClock().getClockArea();
						Material m = ct.getClock().getMaterial();
						Material f = ct.getClock().getFillingMaterial();
						byte d = ct.getClock().getData();
						byte fd = ct.getClock().getFillingData();
						boolean ss = ct.getClock().shouldShowSeconds();
						boolean bl = ct.getClock().isBlinking();
						boolean blm = ct.getClock().isBlinkingChangerON();
						boolean ampm = ct.getClock().getAMPM();

				    	String letter = null;
				    	int newHours = 0;
				    	String hours = phours;
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
				    	
				    	int w = Generator.this.getMain().getSettingsWidth();
				    	
						ct.generate(0, Character.digit(hours.charAt(0), 10), ca, m, d, f, fd, online);
						ct.generate(3, ca, f, fd);
						ct.generate(w + 1, hours.charAt(1) - '0', ca, m, d, f, fd, online);
						
						if(bl) { 
							if(blm) {
								ct.generate(w*2 + 1, 10, ca, f, d, f, fd, online);
								ct.getClock().setBlinkingChanger(false);
							} else {
								ct.generate(w*2 + 1, 10, ca, m, d, f, fd, online); 
								ct.getClock().setBlinkingChanger(true);
							}
						} else {
							ct.generate(w*2 + 1, 10, ca, m, d, f, fd, online);
						}
						
						ct.generate(w*3 + 1, minutes.charAt(0) - '0', ca, m, d, f, fd, online);
						ct.generate(w*4 + 1, ca, f, fd);
						ct.generate(w*4 + 2, minutes.charAt(1) - '0', ca, m, d, f, fd, online);
						
						if(ss) {
							if(bl) { 
								if(blm) {
							     	ct.generate(w*5 + 2, 10, ca, f, d, f, fd, online);
									ct.getClock().setBlinkingChanger(false);
								} else {
							     	ct.generate(w*5 + 2, 10, ca, m, d, f, fd, online); 
							     	ct.getClock().setBlinkingChanger(true);
								}
							} else {
						     	ct.generate(w*5 + 2, 10, ca, m, d, f, fd, online);
							}
							
					    	ct.generate(w*6 + 2, seconds.charAt(0) - '0', ca, m, d, f, fd, online);
							ct.generate(w*7 + 2, ca, f, fd);
					    	ct.generate(w*7 + 3, seconds.charAt(1) - '0', ca, m, d, f, fd, online);
					    	
					    	if(ampm && letter != null) {
					    		if(letter == "A") {
					    	    	ct.generate(w*8 + 4, 11, ca, m, d, f, fd, online);
					    		} else {
					    	    	ct.generate(w*8 + 4, 12, ca, m, d, f, fd, online);
					    		}
								ct.generate(w*9 + 4, ca, f, fd);
				    	    	ct.generate(w*9 + 5, 13, ca, m, d, f, fd, online);
					    	}
						} else {
					    	if(ampm && letter != null) {
					    		if(letter == "A") {
					    	    	ct.generate(w*5 + 3, 11, ca, m, d, f, fd, online);
					    		} else {
					    	    	ct.generate(w*5 + 3, 12, ca, m, d, f, fd, online);
					    		}
								ct.generate(w*6 + 3, ca, f, fd);
						    	ct.generate(w*6 + 4, 13, ca, m, d, f, fd, online);
					    	}
						}
					}
				});
			}
			
			@Override
			public Date getDate() {
				return date;
			}
		};
		ClockThread.getByClock(clock).addToGGSList(ggs);
	}
	
	public DigitalClock getMain() {
		return this.i;
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
	
	public static interface GeneratingSequence {
		public void generate(ClockThread ct);
		public Date getDate();
	}
	
	public static Generator getGenerator() {
		return Generator.generator;
	}
}
