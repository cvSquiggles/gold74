package scripts;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Game;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Options;
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
import org.tribot.script.interfaces.NPCClicking;
import org.tribot.api.util.abc.*;

@SuppressWarnings({ "unused", "deprecation" })
@ScriptManifest(authors = {
		"Slippi, Sneakles" }, category = "Mud Runes", name = "DruidicRitual", version = 0.1, description = "Does the Druidic Ritual quest.")
public class DruidicRitual extends Script implements Loopable {

	State state;

	int j = 1;
	int gamesNecklace = 3853;

	public ABCUtil abc;

	boolean banking1 = true;
	boolean banking2 = true;
	boolean hasTeleported = true;
	boolean hasWalkedToKaqemeex = true;
	boolean hasTalkedToKaqemeex = true;
	boolean hasWalkedToSanfew;
	boolean hasTalkedToSanfew;
	boolean hasWalkedToCauldron;
	boolean hasInteractedWithCauldron;
	boolean hasReturnedToSanfew;
	boolean hasReturnedToKaqemeex;

	RSArea druidsCircle = new RSArea(new RSTile(2922, 3486, 0), new RSTile(2929, 3481, 0));
	RSArea sanfewsRoom = new RSArea(new RSTile(2897, 3431, 0), new RSTile(2899, 3425, 0));

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
		println("Hello world.");

	}

	@Override
	public int onLoop() {
		switch (getState()) {

		case BANKING:

			if (!banking1) {

				println("Getting quest items from the bank.");

				if (!Banking.isBankLoaded()) {

					Banking.openBank();
					Banking.waitUntilLoaded(3000);
					General.random(300, 600);

				}

				else if (Banking.isBankScreenOpen()) {

					int[] itemID = { 2138, 2136, 2134, 2132, 3853, 12625, 88, 11133 };
					int[] itemID2 = { 8007, 22795 };

					withdrawItems(itemID, 1);

					if (!Timing.waitCondition(new Condition() {

						@Override
						public boolean active() {

							General.sleep(200);
							return (itemCheck(itemID, 1));

						}

					}, General.random(20000, 40000)))
						;

					withdrawItems(itemID2, 5);

					if (!Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {

							General.sleep(200);
							return (itemCheck(itemID2, 5));

						}
					}, General.random(20000, 40000)))
						;

					General.sleep(General.randomSD(15000, 3000));

					if (itemCheck(itemID, 1) && itemCheck(itemID2, 5)) {

						banking1 = true;
						Banking.close();
						General.sleep(500, 1000);

					}

				}

			}

			if (banking1) {

				println("Equipping Boots of lightness and Regen bracelet");

				if (Inventory.find(88).length > 0 && Inventory.find(11133).length > 0) {

					RSItem[] Boots = Inventory.find(88);
					RSItem[] Braclets = Inventory.find(11133);

					Boots[0].click("Wear");
					General.sleep(300, 600);
					Braclets[0].click("Wear");
					General.sleep(300, 600);

					if (Inventory.find(88).length < 1 && Inventory.find(11133).length < 1) {

						banking2 = true;

					}

				}

			}

			break;

		case WALKTOKAQEMEEX:

			if (!hasTeleported) {

				println("Teleporting to Burthorpe.");

				RSItem[] GamesNecks = Inventory.find(gamesNecklace);
				RSItem GamesNeck = GamesNecks[0];

				GamesNeck.click("Rub");

				General.sleep(5000);

				String[] Options = NPCChat.getOptions();

				NPCChat.selectOption("Burthorpe", true);

				General.sleep(5000, 10000);

				if (Inventory.find(gamesNecklace).length == 0) {
					hasTeleported = true;
				}

				else
					break;
			}

			if (!druidsCircle.contains(Player.getPosition()) && hasTeleported) {

				println("Walking to Kaqemeex.");

				WebWalking.walkTo(druidsCircle.getRandomTile());

				General.sleep(5000, 10000);

//				abc.leaveGame();

				if (druidsCircle.contains(Player.getPosition())) {
					hasWalkedToKaqemeex = true;
				}

				else
					break;

			}

			break;

		case TALKTOKAQEMEEX:

			if (Game.getSetting(80) == 0) {

				println("Talking to Kaqemeex.");

				RSNPC[] Kaqemeexs = NPCs.findNearest(5045);
				RSNPC Kaqemeex = Kaqemeexs[0];
				Kaqemeex.adjustCameraTo();
				Kaqemeex.click("Talk-to");
				General.sleep(600, 1200);
				NPCChat.clickContinue(true);
				General.sleep(300, 600);
				NPCChat.clickContinue(true);
				General.sleep(600, 1200);
				NPCChat.selectOption("I'm in search of a quest.", true);
				General.sleep(300, 600);
				NPCChat.clickContinue(true);
				General.sleep(300, 600);
				NPCChat.clickContinue(true);
				General.sleep(300, 600);
				NPCChat.clickContinue(true);
				General.sleep(300, 600);
				NPCChat.clickContinue(true);
				General.sleep(600, 1200);
				NPCChat.selectOption("Okay, I will try and help.", false);
				General.sleep(300, 600);
				NPCChat.clickContinue(true);
				General.sleep(300, 600);
				NPCChat.clickContinue(true);
				General.sleep(300, 600);
				NPCChat.clickContinue(true);
				General.sleep(300, 600);
			}

			if (Game.getSetting(80) > 0) {

				hasTalkedToKaqemeex = true;

			}
			;

			break;

		case WALKTOSANFEW:

			if (!sanfewsRoom.contains(Player.getPosition())) {
				
				println("Walking to Sanfew.");

				WebWalking.walkTo(sanfewsRoom.getRandomTile());

				if (druidsCircle.contains(Player.getPosition())) {
					hasWalkedToKaqemeex = true;
				}
			}

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

		if (!banking2) {
			state = State.BANKING;
		}

		if (!hasWalkedToKaqemeex && banking2) {
			state = State.WALKTOKAQEMEEX;
		}

		if (!hasTalkedToKaqemeex && hasWalkedToKaqemeex) {
			state = State.TALKTOKAQEMEEX;
		}

		if (!hasWalkedToSanfew && hasTalkedToKaqemeex) {
			state = State.WALKTOSANFEW;
		}

		if (!hasTalkedToSanfew && hasWalkedToSanfew) {
			state = State.TALKTOSANFEW;
		}

		if (!hasWalkedToCauldron && hasTalkedToSanfew) {
			state = State.WALKTOCAULDRON;
		}

		if (!hasInteractedWithCauldron && hasWalkedToCauldron) {
			state = State.INTERACTWITHCAULDRON;
		}

		if (!hasReturnedToSanfew && hasInteractedWithCauldron) {
			state = State.RETURNTOSANFEW;
		}

		if (!hasReturnedToKaqemeex && hasReturnedToSanfew) {
			state = State.RETURNTOSANFEW;
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

/*	public boolean travel(RSArea x, int y, int z) {
	    while (!x.contains(Player.getPosition())) {
	        if (//get run energy method < General.random(y, z)) {
	            Options.setRunOn(false);
	        } else {
	        	
	        	if(!Options.isRunEnabled()) {
	        	Options.setRunOn(true);
	        	}
	        }

	        WebWalking.walkTo(x.getRandomTile());

	        General.randomSD(2500, 500);
	        General.sleep(2500, 3000);
	    }
*/
}
