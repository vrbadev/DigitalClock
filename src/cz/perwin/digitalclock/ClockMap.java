package cz.perwin.digitalclock;

import java.util.HashMap;

import cz.perwin.digitalclock.core.Clock;

public class ClockMap extends HashMap<Clock, Integer> {
	private static final long serialVersionUID = 1L;

	public Integer getByClockName(String name) {
		for(Clock c0 : this.keySet()) {
			if(c0.getName().equals(name)) {
				return this.get(c0);
			}
		}
		return -1;
	}
	
	public boolean containsKeyByClockName(String name) {
		for(Clock c0 : this.keySet()) {
			if(c0.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public Integer removeByClockName(String name) {
		for(Clock c0 : this.keySet()) {
			if(c0.getName().equals(name)) {
				return this.remove(c0);
			}
		}
		return -1;
	}
}
