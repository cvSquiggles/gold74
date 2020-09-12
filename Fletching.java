package scripts;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

@ScriptManifest(authors = {
		"Slippi, Sneakles" }, category = "Fletching", name = "Fletching", version = 1, description = "Start of fletching scripting.")
public class Fletching extends Script implements Loopable {

	State state;

	boolean playerLVL10;
	boolean playerLVL20;

	@Override
	// Initial method run by all TriBot Scripts. Executes onStart, and
	// then begins looping until -1 is returned, or you manually stop the script via
	// TriBot.
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

	@SuppressWarnings("deprecation")
	@Override
	public int onLoop() {
		// TODO Auto-generated method stub
		switch (getState()) {

		case LVL1TO10:
			println("Case: LVL1TO10");

			if (!playerLVL10) {

				if (Skills.getCurrentLevel(SKILLS.FLETCHING) >= 10) {
					playerLVL10 = true;
					println("Player's fletching already 10!");

					Banking.openBank();

					General.sleep(1000);

					if (Banking.isBankScreenOpen()) {
						RSItem[] fillers = Banking.find("Bank filler");
						fillers[0].click("Clear-All");
						General.sleep(2000);
						Banking.depositAllExcept("Knife");
						General.sleep(2000);
						RSInterface child2 = Interfaces.get(12, 111);
						if (child2 != null) {
							child2.click("Show menu");
						}
						General.sleep(2000);
						RSInterface child3 = Interfaces.get(12, 67);
						if (child3 != null) {
							child3.click("Fill");
						}
						General.sleep(2000);
						RSInterface child4 = Interfaces.get(12, 111);
						if (child4 != null) {
							child4.click("Hide menu");
						}
						General.sleep(2000);
					}

					break;
				}

				General.sleep(General.random(400, 1200));

				if (Inventory.find("Logs").length == 0) {
					println("I need to get some logs.");

					Banking.openBank();

					General.sleep(General.random(1000, 1100));

					Banking.withdraw(26, "Logs");

					if (!Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(200);
							return (Inventory.find("Logs").length > 25);
						}
					}, General.randomLong(10000, 12000))) {
					}
					;

					Banking.close();

					General.sleep(General.random(400, 600));

				}

				if (Inventory.find("Logs").length > 0) {
					RSItem[] Logs = Inventory.find("Logs");
					RSItem[] Knives = Inventory.find("Knife");

					if (Logs.length > 0 && Knives.length > 0) {

						if (Knives[0].click("Use")) {

							if (Logs[0].click("Use")) {
								General.sleep(1000);

								println("Making arrow shafts.");
								RSInterface child = Interfaces.get(270, 14);
								if (child != null) {
									child.click("Make");
								}

								if (!Timing.waitCondition(new Condition() {
									@Override
									public boolean active() {
										General.sleep(200);
										if (NPCChat.getClickContinueInterface() != null) {
											NPCChat.clickContinue(true);
										}
										General.sleep(400);
										return ((Inventory.find("Logs").length == 0) || !isCurrentlyBusy());
									}
								}, General.randomLong(40000, 50000))) {
								}
								;
							}
						}
					}
				}

				General.sleep(2000);
			}

			break;

		case LVL10TO20:

			General.sleep(500);
			break;
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
		LVL1TO10, LVL10TO20
	}

	// Checks if a certain condition is met, then return that state.
	private State getState() {
		if (!playerLVL10) {
			state = State.LVL1TO10;
		}
		if (playerLVL10) {
			state = State.LVL10TO20;
		}
		return state;
	}

}
