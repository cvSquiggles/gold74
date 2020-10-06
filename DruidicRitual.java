package scripts;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
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
	int gamesNecklace = 3855;

	boolean banking = true;
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
	// Initial method run by all TriBot Scripts. Executes onStart, and then begins
	// looping until -1 is returned, or you manually stop the script via TriBot.
	public void run() {
		onStart();

		int i;

		while ((i = onLoop()) != -1) {
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

		case BANKING:

			// If 'Games necklace(8)' is not equipped and Quest items aren't in inventory,
			// then pull the items from the bank and equip 'Games necklace(8)'.

			if (!banking) {

				println("Getting quest items.");

				// Open bank and wait until it's loaded.
				if (!Banking.isBankLoaded()) {
					Banking.openBank();
					Banking.waitUntilLoaded(3000);
					General.random(100, 300);
				}

				else if (Banking.isBankScreenOpen()) {

					int[] itemID = { 2138, 2136, 2134, 2132, 3853, 12625, 88, 11133 };
					int[] itemID2 = { 8007, 22795 };

					// Withdraws quest items using IDs.

					withdrawItems(itemID, 1);

					if (!Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(200);
							return (itemCheck(itemID, 1));
						}
					}, General.random(20000, 40000))) {
					}
					;

					withdrawItems(itemID2, 5);

					if (!Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(200);
							return (itemCheck(itemID2, 5));
						}
					}, General.random(20000, 40000))) {
					}
					;

					General.sleep(10000, 20000);
					if (itemCheck(itemID, 1) && itemCheck(itemID2, 5)) {
						banking = true;
					}

					if (banking) {
						Banking.close();
						General.sleep(500, 1000);

					}
				}

			}

			break;

		case WALKTOKAQEMEEX:
//			FUCK YEAH BITCH

			RSItem[] GamesNecks = Inventory.find(gamesNecklace);
			RSItem GamesNeck = GamesNecks[0];
			GamesNeck.click("Rub");

			General.sleep(5000);

			String[] Options = NPCChat.getOptions();

			println(Options[0]);
			NPCChat.selectOption("Burthorpe", true);

			General.sleep(5000, 10000);

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
		return 50;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		println("Goodbye world.");
	}

	@SuppressWarnings("unused")
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
		BANKING, WALKTOKAQEMEEX, TALKTOKAQEMEEX, WALKTOSANFEW, TALKTOSANFEW, WALKTOCAULDRON, INTERACTWITHCAULDRON,
		RETURNTOSANFEW, RETURNTOKAQEMEEX

	}

	// Checks if a certain condition is met, then return that state.
	private State getState() {

		if (!banking) {
			state = State.BANKING;
		}

		if (!hasWalkedToKaqemeex && banking) {
			state = State.WALKTOKAQEMEEX;
		}

		return state;
	}

	private boolean itemCheck(int[] x, int y) {

		int i = 0;

		while (i <= x.length - 1) {

			if (!(Inventory.getCount(x[i]) >= y)) {
				return (false);
			}
			;

			i++;
		}

		return (true);
	}

	private boolean withdrawItems(int[] x, int y) {

		int i = 0;

		while (i <= x.length - 1) {

			Banking.withdraw(y, x[i]);
			General.random(1325, 2178);

			i++;
		}

		return (false);

	}
}
