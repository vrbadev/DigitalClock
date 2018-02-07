package cz.perwin.digitalclock.core;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.Version;
import cz.perwin.digitalclock.commands.MaterialCommand;

public class Events implements Listener {
	private DigitalClock i;
	
	public Events(DigitalClock i) {
		this.i = i;
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent evt) {
		if(this.i.protectClocks() && ClockArea.containsAny(evt.getBlock().getLocation())) {
			evt.setCancelled(true);
		} else {
			Player player = evt.getPlayer();
			if(this.i.getEnableBuildUsers().containsKey(player)) {
				Block block = evt.getBlockPlaced();
				if(new MaterialCommand().isSolid(block.getType())) {
					Block playersBlock = player.getWorld().getBlockAt(new Location(player.getWorld(), player.getLocation().getBlockX(), block.getY(), player.getLocation().getBlockZ()));
					Clock clock = new Clock(this.i.getEnableBuildUsers().get(player), player.getName(), block, playersBlock, 1);
					clock.writeAndGenerate();
					player.sendMessage(ChatColor.DARK_GREEN + DigitalClock.getMessagePrefix() + ChatColor.GREEN + " Your clock '" + clock.getName() + "' has been successfully created!" + (this.i.shouldRun() ? " It is running now." : " It isn't running, you can start it by '/dc runclock "+ clock.getName() +"'."));
					this.i.getClocks();
					if(this.i.shouldRun()) {
						this.i.run(clock.getName());
					}
					this.i.getEnableBuildUsers().remove(player);
				} else {
					player.sendMessage(ChatColor.DARK_RED + DigitalClock.getMessagePrefix() + ChatColor.RED + " Placed item is not a block (cuboid shape). Please place a block.");
				}
				evt.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent evt) {
		Player player = evt.getPlayer();
		if(this.i.getEnableBuildUsers().containsKey(player) && evt.getItem() == null) {
			player.sendMessage(ChatColor.DARK_GREEN + DigitalClock.getMessagePrefix() + ChatColor.GREEN + " Creating of new clock stopped.");
			this.i.getEnableBuildUsers().remove(player);
		}
		if(evt.getAction() == Action.RIGHT_CLICK_BLOCK && this.i.getEnableMoveUsers().containsKey(player)) {
			Block block = evt.getClickedBlock();
			Block playersblock = player.getWorld().getBlockAt(new Location(player.getWorld(), player.getLocation().getBlockX(), block.getY(), player.getLocation().getBlockZ()));
			Clock clock = Clock.loadClockByClockName(this.i.getEnableMoveUsers().get(player));
			clock.getClockArea().move(block, playersblock);
			player.sendMessage(ChatColor.DARK_GREEN + DigitalClock.getMessagePrefix() + ChatColor.GREEN + " Your clock '" + clock.getName() + "' has successfully moved to your position!");
			this.i.getEnableMoveUsers().remove(player);
    	}
	}
	
	@EventHandler
	public void onCountdownEnd(CountdownEndEvent evt) {
		this.i.getConsole().info("[DigitalClock] Countdown of clock '" + evt.getClock().getName() + "' ended! Clock has been stopped.");
		//this.i.getGenerator().generatingSequence(evt.getClock(), "00", "00", "00", null);
	}
	
	@EventHandler
	public void onStopwatchEnd(StopwatchEndEvent evt) {
		this.i.getConsole().info("[DigitalClock] Stopwatch of clock '" + evt.getClock().getName() + "' ended at "+ evt.getEndValue() +" seconds! Clock has been stopped.");
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent evt) {
		if(this.i.versionWarning()) {
			if(evt.getPlayer().isOp() || evt.getPlayer().hasPermission("digitalclock.update")) {
				if(!Version.getActualVersion().getVersion().equals(this.i.getDescription().getVersion())) {
					evt.getPlayer().sendMessage(ChatColor.DARK_GREEN + DigitalClock.getMessagePrefix() + ChatColor.GREEN + " There is a newer version (v"+ Version.getActualVersion().getVersion() +") of this plugin (v"+ this.i.getDescription().getVersion() +"). Download it from "+ ChatColor.UNDERLINE + ChatColor.BLUE + Version.getActualVersion().getDownloadLink() + "" + ChatColor.RESET + ChatColor.GREEN +" or use command '/dc update'.");
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockDamage(BlockDamageEvent evt) {
		if(this.i.protectClocks() && ClockArea.containsAny(evt.getBlock().getLocation())) {
			evt.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent evt) {
		if(this.i.protectClocks() && ClockArea.containsAny(evt.getBlock().getLocation())) {
			evt.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onItemSpawn(ItemSpawnEvent evt) {
		if(ClockArea.containsAny(evt.getLocation())) {
			evt.setCancelled(true);
		}
	}
}
