package cz.perwin.digitalclock;

public class AfterDone implements Runnable {
	private Main i;
	
	public AfterDone(Main i) {
		this.i = i;
	}
	
	public void run() {
		this.i.getClocks();
		this.i.console.info("[DigitalClock] Loaded "+ this.i.getClocksL().size() +" clock(s).");
		this.i.runTasks();
	}
}
