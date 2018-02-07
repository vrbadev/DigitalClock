package cz.perwin.digitalclock.core;

import java.util.Date;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class StopwatchEndEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Clock clock;
	private Date endtime;
	private int endvalue;

	public static HandlerList getHandlerList() {
	    return handlers;
	}
	  
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public StopwatchEndEvent(Clock clock, int endval) {
		this.clock = clock;
		this.endtime = new Date();
		this.endvalue = endval;
	}
	
	public Clock getClock() {
		return this.clock;
	}
	
	public Date getEndTime() {
		return this.endtime;
	}
	
	public int getEndValue() {
		return this.endvalue;
	}
}
