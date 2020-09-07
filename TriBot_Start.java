package scripts;

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
	
	// State names
	private enum State {

	}
	
	// Checks if a certain condition is met, then return that state.
	private State getState() {

		return state;
	}

}

