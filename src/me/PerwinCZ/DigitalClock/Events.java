package me.PerwinCZ.DigitalClock;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Events implements Listener {
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent evt) {
		Player player = evt.getPlayer();
		if(Main.i.enableBuildUsers.containsKey(player)) {
			Block block = evt.getBlockPlaced();
			Block playersBlock = player.getWorld().getBlockAt(new Location(player.getWorld(), player.getLocation().getBlockX(), block.getY(), player.getLocation().getBlockZ()));
			Clock clock = new Clock(Main.i.enableBuildUsers.get(player), player.getName(), block, playersBlock);
			clock.generate();
			player.sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + clock.getName() + "' has been successfully created!");
			Main.i.getClocks();
			Main.i.enableBuildUsers.remove(player);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent evt) {
		Player player = evt.getPlayer();
		if(evt.getAction() == Action.RIGHT_CLICK_BLOCK && Main.i.enableMoveUsers.containsKey(player)) {
			Block block = evt.getClickedBlock();
			Block playersblock = player.getWorld().getBlockAt(new Location(player.getWorld(), player.getLocation().getBlockX(), block.getY(), player.getLocation().getBlockZ()));
			Clock clock = Clock.loadClockByClockName(Main.i.enableMoveUsers.get(player));
			clock.move(block, playersblock);
			player.sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + clock.getName() + "' has successfully moved to your position!");
			Main.i.enableMoveUsers.remove(player);
    	}
	}
	
	@EventHandler
	public void onCountdownEnd(CountdownEndEvent evt) {
		Main.i.console.info("[DigitalClock] Countdown of clock '" + evt.getClock().getName() + "' has reached zero time! Clock has been stopped.");
		Generator.generatingSequence(evt.getClock(), "00", "00", "00", null); // <- not working, why?
	}
}
