package cz.perwin.digitalclock.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ClockThread extends Thread {
	private static volatile Map<String, ClockThread> list = new HashMap<>();
	
	private volatile ArrayList<Generator.GeneratingSequence> ggsList = new ArrayList<>();
	private int id;
	private boolean running = true;
	private Clock clock;
	
	private ClockThread(Clock clock) {
		if(!ClockThread.getList().containsKey(clock.getName())) {
			this.clock = clock;
			this.id = ClockThread.getList().size();
			ClockThread.getList().put(clock.getName(), this);
		}
	}
	
	@Override
	public void run() {
		if(this.getClock() != null) {
			while(this.running) {
				if(this.getGGSList().size() > 0) {
					Generator.GeneratingSequence ggs = this.getGGSList().get(0);
					if(new Date().getTime()-ggs.getDate().getTime() < Generator.getGenerator().getMain().getGeneratorAccuracy()) {
						ggs.generate(this);
					}
					this.getGGSList().remove(0);
				}
			}
			ClockThread.getList().remove(this.getClock().getName());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void generate(int i, int n, ClockArea ca, Material m, byte d, Material f, byte fd, Player online) {
		for(int q = 0; q < Generator.getGenerator().getMain().getSettingsWidth(); q++) {
			for(int p = 0; p < ca.getHeight(); p++) {
				Location newLoc = ca.getLocation(p, q, i);
				Block newBlock = newLoc.getBlock();
				String[] r = Generator.getGenerator().getMain().getConfig().getString("num" + n).split(";");
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
	public void generate(int i, ClockArea ca, Material f, byte fd) {
		for(int p = 0; p < ca.getHeight(); p++) {
			Location newLoc = ca.getLocation(p, 0, i);
			Block newBlock = newLoc.getBlock();
			newBlock.setType(f);
			newBlock.setData(fd);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void removeThreadAndRestore() {
		this.abort();
		
		File file = new File(new File(Generator.getGenerator().getMain().getDataFolder(), "terrainbackups"), this.getClock().getName() + ".txt");
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
			e.printStackTrace();
		}
		
		int n = 0;
		for(int u = 0; u < this.getClock().getClockArea().getWidth(); u++) {
			for(int v = 0; v < this.getClock().getClockArea().getHeight(); v++) {
				String toSplit = lines.get(n);
				String[] data = toSplit.split(":");
				Location newLoc = this.getClock().getClockArea().getLocation(v, u, 0);
				Material mat = Material.valueOf(data[0]);
				byte md = (byte) Integer.parseInt(data[1]);
				newLoc.getBlock().setType(mat);
				newLoc.getBlock().setData(md);
				if(Generator.getGenerator().getMain().shouldGenerateSeparately()) {
					for(Player online : Bukkit.getServer().getOnlinePlayers()) {
						online.sendBlockChange(newLoc, mat, md);
					}
				}
				n++;
			}
		}
		file.delete();
	}
	
	public Clock getClock() {
		return this.clock;
	}
	
	private void abort() {
		this.running = false;
	}
	
	public int getID() {
		return this.id;
	}
	
	private synchronized static Map<String, ClockThread> getList() {
		return ClockThread.list;
	}
	
	private synchronized ArrayList<Generator.GeneratingSequence> getGGSList() {
		return this.ggsList;
	}
	
	public synchronized void addToGGSList(Generator.GeneratingSequence ggs) {
		this.getGGSList().add(ggs);
	}
	
	public synchronized static ClockThread getByClock(Clock clock) {
		if(ClockThread.getList().containsKey(clock.getName())) {
			return ClockThread.getList().get(clock.getName());
		} else {
			ClockThread ct = new ClockThread(clock);
			ct.start();
			return ct;
		}
	}
}
