package scripts.MudRunesOOP.tasks;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;

import scripts.MudRunesOOP.data.Vars;
import scripts.MudRunesOOP.framework.AbstractTask;
import scripts.MudRunesOOP.utility.Antiban;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.MudRunesOOP.data.Constants;

public class Runecrafting implements AbstractTask {
	

//	Runecrafting statistics; used in ABC2
	private long lastRunecraftingWaitTime;
	private long averageRunecraftingWaitTime;
	private long totalRunecraftingWaitTime;
	private long totalRunecraftingInstances;

	private RSObject target;


	/**
	 * Updates the variables used in ABC2
	 * 
	 * Prints them to the console if the user selected 'print debug'
	 */
	private void updateRunecraftingStatistics(long startedRunecrafting) {
		lastRunecraftingWaitTime = System.currentTimeMillis() - startedRunecrafting;
		totalRunecraftingInstances++;
		totalRunecraftingWaitTime += lastRunecraftingWaitTime;
		averageRunecraftingWaitTime = totalRunecraftingWaitTime / totalRunecraftingInstances;

		if (!Vars.get().printDebug)
			return;
		General.println("Player stopped runecrafting!");
		General.println("Last runecrafting wait time: " + lastRunecraftingWaitTime / 1000 + " seconds");
		General.println("Total runecrafting instances: " + totalRunecraftingInstances);
		General.println("Total runecrafting wait time: " + totalRunecraftingWaitTime / 1000 + " seconds");
		General.println("Average runecrafting wait time: " + averageRunecraftingWaitTime / 1000 + " seconds");

	}

	@Override
	public String info() {
		return "Runecrafting task";
	}

	@Override
	public boolean shouldExecute() {
		return Inventory.isFull() && Constants.altarEntrance.contains(Player.getPosition());
	}

	@Override
	public void execute() {
//		If player is runecrafting
		if (Player.getAnimation() != -1) {

			long startedRunecrafting = System.currentTimeMillis();

//			While the player is runecrafting,
			while (Player.getAnimation() != -1) {
//				Do the following:
				General.sleep(300);
				Antiban.get().executeHoverOption(target);
				Antiban.get().resolveTimedActions();
			} // End while runecrafting.

//			The player has just stopped runecrafting. Do the following:
			updateRunecraftingStatistics(startedRunecrafting);
			Antiban.get().generateAndSleep((int) lastRunecraftingWaitTime);
		} // End if runecrafting.

	}
	
	boolean findAndEnterAltar() {
		
		RSObject[] altarEntrances = Objects.find(10, Constants.ALTAR_ENTRANCE);
		
		if (altarEntrances == null || altarEntrances.length == 0) {
			
			General.println("No altar entrance was found!");
		
			return false;
		}
		
		target = Antiban.get().getNextTarget(altarEntrances);
		
		if (!target.isOnScreen()) {
			General.println("Altar entrance is not on screen");
			DaxWalker.walkTo(target.getPosition());
			Camera.turnToTile(target.getPosition());
		}
		
		if (Clicking.click("Enter", target)) {
			return true;
		}
		return false;
	}
	
	boolean findAndClickAltar() {
			
			RSObject[] altars = Objects.find(20, Constants.ALTAR);
			
			if (altars == null || altars.length == 0) {
				
				General.println("No altar was found.");
				
				return false;
		}
			target = Antiban.get().getNextTarget(altars);
			
			if (!target.isOnScreen()) {
				General.println("Altar is not on screen");
				DaxWalker.walkTo(target.getPosition());
				Camera.turnToTile(target.getPosition());
			}

			if (Clicking.click("Craft-rune", target)) {
				return true;
			}
			return false;

	}
}
