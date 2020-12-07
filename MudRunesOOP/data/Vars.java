package scripts.MudRunesOOP.data;

public class Vars {
	
//	Instance manipulation
	private Vars() {}
	private static final Vars VARS = new Vars();
	public static Vars get() { return VARS; }
	
//	Script settings
	public String scriptCrashReason;
	public boolean printDebug = true;
	public boolean variablesInitialized;
	
//	Runecrafting settings
	public boolean bankRunes;
	
//	Account info
	public long startingXP;
	public int runesCount;
	
	

}
