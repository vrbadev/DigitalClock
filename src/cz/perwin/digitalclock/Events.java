package cz.perwin.digitalclock;

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
	private Main i;
	
	public Events(Main i) {
		this.i = i;
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent evt) {
		Player player = evt.getPlayer();
		if(this.i.getEnableBuildUsers().containsKey(player)) {
			Block block = evt.getBlockPlaced();
			Block playersBlock = player.getWorld().getBlockAt(new Location(player.getWorld(), player.getLocation().getBlockX(), block.getY(), player.getLocation().getBlockZ()));
			Clock clock = new Clock(this.i.getEnableBuildUsers().get(player), player.getName(), block, playersBlock);
			clock.generate();
			player.sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + clock.getName() + "' has been successfully created!");
			this.i.getClocks();
			this.i.getEnableBuildUsers().remove(player);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent evt) {
		Player player = evt.getPlayer();
		if(evt.getAction() == Action.RIGHT_CLICK_BLOCK && this.i.getEnableMoveUsers().containsKey(player)) {
			Block block = evt.getClickedBlock();
			Block playersblock = player.getWorld().getBlockAt(new Location(player.getWorld(), player.getLocation().getBlockX(), block.getY(), player.getLocation().getBlockZ()));
			Clock clock = Clock.loadClockByClockName(this.i.getEnableMoveUsers().get(player));
			clock.move(block, playersblock);
			player.sendMessage(ChatColor.GREEN + "[DigitalClock] Your clock '" + clock.getName() + "' has successfully moved to your position!");
			this.i.getEnableMoveUsers().remove(player);
    	}
	}
	
	@EventHandler
	public void onCountdownEnd(CountdownEndEvent evt) {
		this.i.console.info("[DigitalClock] Countdown of clock '" + evt.getClock().getName() + "' has reached zero time! Clock has been stopped.");
		this.i.getGenerator().generatingSequence(evt.getClock(), "00", "00", "00", null); // <- not working, why?
	}
}
