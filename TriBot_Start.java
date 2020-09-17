package scripts;

import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSItem;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;



@ScriptManifest(authors= {"Slippi, Sneakles"}, category = "template", name = "TriBot_Start", version = 1, description = "Initial template.")
public class TriBot_Start extends Script implements Loopable {
	
	State state;
	
	@Override
	//Initial method run by all TriBot Scripts. Executes onStart, and then begins looping until -1 is returned, or you manually stop the script via TriBot.
	public void run() {
		onStart();
		
		int i;
		
		while((i = onLoop()) != -1) {
			sleep(i);
		}
		
		onStop();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		println("Hello world.");
	}

	@Override
	public int onLoop() {
		// TODO Auto-generated method stub
		switch (getState()) {

		}
		return 5;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		println("Goodbye world.");
	}
	
	private boolean isCurrentlyBusy() {
		RSItem[] invoItems = Inventory.getAll();
		int startXp = Skills.getXP(SKILLS.FLETCHING);
		long t = System.currentTimeMillis();
		while (Timing.timeFromMark(t) < 3000) {
			if (invoItems.length > Inventory.getAll().length || Skills.getXP(SKILLS.FLETCHING) > startXp) {
				return true;
			}
			sleep(100);
		}
		return false;
	}
	
	// State names
	private enum State {

	}
	
	// Checks if a certain condition is met, then return that state.
	private State getState() {

		return state;
	}

}

