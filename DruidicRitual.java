package scripts;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

@SuppressWarnings({ "unused", "deprecation" })
@ScriptManifest(authors = {
		"Slippi, Sneakles" }, category = "Mud Runes", name = "DruidicRitual", version = 0.1, description = "Does the Druidic Ritual quest.")
public class DruidicRitual extends Script implements Loopable {

	State state;

	int j = 1;

	boolean hasWalkedToKaqemeex;
	boolean hasTalkedToKaqemeex;
	boolean hasWalkedToSanfew;
	boolean hasTalkedToSandew;
	boolean hasWalkedToCauldron;
	boolean hasInteractedWithCauldron;
	boolean hasReturnedToSanfew;
	boolean hasReturnedToKaqemeex;

	RSArea druidsCircle = new RSArea(new RSTile(0, 0, 0), new RSTile(0, 0, 0));

	@Override
	public void onStart() {

		int i;

		while ((i = onLoop()) != -1) {
			sleep(i);
		}

		onStop();

	}

	@Override
	public int onLoop() {

		switch (getState()) {

		case WALKTOKAQEMEEX:

			// If 'Games necklace(8)' is not equipped and Quest items aren't in inventory,
			// then pull the items from the bank and equip 'Games necklace(8)'.

			if (!Equipment.isEquipped(3853) && Inventory.find(2138, 2136, 2132, 2134, 8007).length < 6) {

				println("Getting quest items.");

				// Open bank and wait until it's loaded.
				if (!Banking.isBankLoaded()) {
					Banking.openBank();
					Banking.waitUntilLoaded(3000);
					General.random(100, 300);
				}

				else if (Banking.isBankScreenOpen()) {
					// Withdraws quest items using IDs.
					Banking.withdraw(0, 2138, 2136, 2132, 2134, 8007, 3853);
					General.random(100, 300);
				}
				

			}

			else if (Equipment.isEquipped(3853) && Inventory.find(2138, 2136, 2132, 2134, 8007).length == 5) {
				println("Teleporting to Burthorpe.");

			}

			break;

		case TALKTOKAQEMEEX:

			break;

		case WALKTOSANFEW:

			break;

		case TALKTOSANFEW:

			break;

		case WALKTOCAULDRON:

			break;

		case INTERACTWITHCAULDRON:

			break;

		case RETURNTOSANFEW:

			break;

		case RETURNTOKAQEMEEX:

			break;

		}

		return 100;
	}

	@Override
	public void onStop() {

		println("Script stopped.");

	}

	@Override
	public void run() {

		onStart();

		int i;

		while ((i = onLoop()) != -1) {
			sleep(i);
		}

		onStop();

	}

	private enum State {
		WALKTOKAQEMEEX, TALKTOKAQEMEEX, WALKTOSANFEW, TALKTOSANFEW, WALKTOCAULDRON, INTERACTWITHCAULDRON,
		RETURNTOSANFEW, RETURNTOKAQEMEEX
	}

	private State getState() {

		if (!hasWalkedToKaqemeex) {
			state = State.WALKTOKAQEMEEX;
		}

		return state;
	}

}