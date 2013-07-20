package me.PerwinCZ.DigitalClock;

public class AfterDone implements Runnable {
	public void run() {
		Main.i.getClocks();
		Main.i.console.info("[DigitalClock] Loaded "+ Main.i.clocks.size() +" clock(s).");
		Main.i.runTasks();
	}
}
