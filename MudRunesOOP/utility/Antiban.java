package scripts.MudRunesOOP.utility;

import org.tribot.api.Clicking;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.types.RSObject;

import scripts.MudRunesOOP.data.Vars;

public class Antiban {

//	Instance manipulation
	private Antiban() {
	}

	private static Antiban antiban = new Antiban();

	public static Antiban get() {
		return antiban;
	}

//	Instantiating the Anti Ban Compliance utility
	private ABCUtil abcInstance = new ABCUtil();

//	Used in action conditions
	boolean shouldHover = false;
	boolean shouldOpenMenu = false;
	boolean shouldMoveToAnticipated = false;

//	Prints ABC3 actions info if the user selected 'print debug'
	private void print(String actionInfo) {
		if (Vars.get().printDebug)
			General.println("ABC2: " + actionInfo);
	}

	// 1. Timed Actions
	// ________________________________________________________________________________________________________________
	/**
	 * Checks internal timers. If a timed action is required: - prints action info -
	 * executes ABC2 action - resets internal timers
	 * 
	 * Important note: this method should only be called when the bot is idle.
	 */
	public void resolveTimedActions() {
		if (abcInstance.shouldCheckTabs()) {
			print("Checking tabs.");
			abcInstance.checkTabs();
		}
		if (abcInstance.shouldCheckXP()) {
			print("Checking experience.");
			abcInstance.checkXP();
		}
		if (abcInstance.shouldExamineEntity()) {
			print("Examining entity.");
			abcInstance.examineEntity();
		}
		if (abcInstance.shouldMoveMouse()) {
			print("Moving mouse.");
			abcInstance.moveMouse();
		}
		if (abcInstance.shouldPickupMouse()) {
			print("Picking up mouse");
			abcInstance.pickupMouse();
		}
		if (abcInstance.shouldRightClick()) {
			print("Right clicking.");
			abcInstance.rightClick();
		}
		if (abcInstance.shouldRotateCamera()) {
			print("Rotating camera.");
			abcInstance.rotateCamera();
		}
		if (abcInstance.shouldLeaveGame()) {
			print("Leaving game.");
			abcInstance.leaveGame();
		}
	}
	
	

	// 2. Preferences
	// ___________________________________________________________________________________________________________________
	/**
	 * - Open bank preference:		already handled by the default API methods 
	 * - Tab switching preference: 	already handled by the default API methods 
	 * - Walking preference: 		already handled by the default API methods
	 */

	public RSObject getNextTarget(Positionable[] targets) {
		return (RSObject) abcInstance.selectNextTarget(targets);
	}
	
	

	// 3. Action conditions
	//___________________________________________________________________________________________________________________
	/**
	 * All action conditions are either already implemented by default or they are
	 * not applicable in this script:
	 * 
	 * - HP to eat at: 							the script doesn't eat any food 
	 * - Energy to activate run at: 			already handled by the default API methods 
	 * - Resource switching high competition: 	not applicable to woodcutting
	 */

	/**
	 * Updates the variables.
	 * 
	 * Must be called upon starting an action. (example: clicking a tree)
	 */
	public void setHoverAndMenuOpenBooleans() {
		this.shouldHover = abcInstance.shouldHover();
		this.shouldOpenMenu = abcInstance.shouldOpenMenu();
	}

	/**
	 * Hovers over or opens the menu for target, if it should.
	 * 
	 * Will be called while performing an action. (example: while cutting a tree)
	 * 
	 * @param target to hover/open menu
	 */
	public void executeHoverOption(RSObject target) {
		if (Mouse.isInBounds() && this.shouldHover) {
			Clicking.hover(target);
			if (this.shouldOpenMenu)
				if (!ChooseOption.isOpen())
					DynamicClicking.clickRSObject(target, 3);
		}
	}

	
	
	

	// 4. Reaction times
	// ______________________________________________________________________________________________________________________

	/**
	 * Generates reaction time using bit flags.
	 * 
	 * @param waitingTime - the amount of time the script was waiting for (e.g.
	 *                    amount of time spent cutting down a tree)
	 * @return the generated reaction time in ms
	 */
	private int generateReactionTime(int waitingTime) {
		boolean menuOpen = abcInstance.shouldOpenMenu() && abcInstance.shouldHover();
		boolean hovering = abcInstance.shouldHover();
		long menuOpenOption = menuOpen ? ABCUtil.OPTION_MENU_OPEN : 0;
		long hoverOption = hovering ? ABCUtil.OPTION_HOVERING : 0;

		return abcInstance.generateReactionTime(abcInstance.generateBitFlags(waitingTime, menuOpenOption, hoverOption));

	}

	/**
	 * Generates supporting tracking information using bit flags. This method should
	 * be called right after clicking something that will require the player to wait
	 * for a while.
	 * 
	 * @param estimatedTime for the action to complete (e.g. amount of time spent
	 *                      cutting down a tree)
	 */
	public void generateSupportingTrackerInfo(int estimatedTime) {
		abcInstance.generateTrackers(estimatedTime);
	}

	/**
	 * Calls generate() and passes waitingTime as an argument. Sleeps for the
	 * generated amount of time.
	 * 
	 * @param waitingTime the amount of time the script was waiting for (e.g. amount
	 *                    of time spent cutting down a tree)
	 */
	public void generateAndSleep(int waitingTime) {
		try {
			int time = generateReactionTime(waitingTime);
			print("Sleeping for: " + ((double) (time / 1000)) + " seconds.");
			abcInstance.sleep(time);
		} catch (InterruptedException e) {
			print("The background thread interrupted the abc sleep.");
		}
	}
}
