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
	protected static Main plugin;
	
	protected Events(Main instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent evt) {
		Player player = evt.getPlayer();
		if(plugin.enableBuildUsers.containsKey(player)) {
			Block block = evt.getBlockPlaced();
			Block playersBlock = player.getWorld().getBlockAt(new Location(player.getWorld(), player.getLocation().getBlockX(), block.getY(), player.getLocation().getBlockZ()));
			Clock clock = new Clock(plugin.enableBuildUsers.get(player), player.getName(), block, playersBlock);
			clock.generate();
			player.sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + clock.getName() + "' has been successfully created!");
			plugin.getClocks();
			plugin.enableBuildUsers.remove(player);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent evt) {
		Player player = evt.getPlayer();
		if(evt.getAction() == Action.RIGHT_CLICK_BLOCK && plugin.enableMoveUsers.containsKey(player)) {
			Block block = evt.getClickedBlock();
			Block playersblock = player.getWorld().getBlockAt(new Location(player.getWorld(), player.getLocation().getBlockX(), block.getY(), player.getLocation().getBlockZ()));
			Clock clock = Clock.loadClockByClockName(plugin.enableMoveUsers.get(player));
			clock.move(block, playersblock);
			player.sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + clock.getName() + "' has successfully moved to your position!");
			plugin.enableMoveUsers.remove(player);
    	}
	}
}
