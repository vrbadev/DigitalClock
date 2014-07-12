package cz.perwin.digitalclock.core;

import java.util.Date;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CountdownEndEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Clock clock;
	private Date endtime;

	public static HandlerList getHandlerList() {
	    return handlers;
	}
	  
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public CountdownEndEvent(Clock clock) {
		this.clock = clock;
		this.endtime = new Date();
	}
	
	public Clock getClock() {
		return this.clock;
	}
	
	public Date getEndTime() {
		return this.endtime;
	}
}
