package cz.perwin.digitalclock.commands;

public class MaterialCommand {
	protected boolean isUsableNumber(String s) {
		if(s.matches("^[0-9]*([,]{1}[0-9]{0,2}){0,1}$")) {
			return true;
		}
		return false;
	}
}
