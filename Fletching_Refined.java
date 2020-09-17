package scripts;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
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
		"Slippi, Sneakles" }, category = "Fletching", name = "Fletching Refined", version = 2, description = "Fletching script to Ruby bolts.")
public class Fletching_Refined extends Script implements Loopable {

	State state;

	boolean makeRB;

	int s;
	int makeButton;

	String item1;
	String item2;

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

	@SuppressWarnings("deprecation")
	@Override
	public int onLoop() {
		// TODO Auto-generated method stub

		s = 5;
		switch (getState()) {

		case LVLMAKING:
			println("Case: LVLMAKING");

			if (Skills.getCurrentLevel(SKILLS.FLETCHING) < 10) {
				item1 = "Logs";
				item2 = "Arrow shaft";
				makeButton = 14;
			}

			if (Skills.getCurrentLevel(SKILLS.FLETCHING) >= 10) {
				item1 = "Logs";
				item2 = "Shortbow (u)";
				makeButton = 16;
			}

			if (Skills.getCurrentLevel(SKILLS.FLETCHING) >= 20) {
				item1 = "Oak logs";
				item2 = "Oak shortbow (u)";
				makeButton = 15;
			}

			if (Skills.getCurrentLevel(SKILLS.FLETCHING) >= 35) {
				item1 = "Willow logs";
				item2 = "Willow shortbow (u)";
				makeButton = 15;
			}

			if (Skills.getCurrentLevel(SKILLS.FLETCHING) >= 50) {
				item1 = "Maple logs";
				item2 = "Maple shortbow (u)";
				makeButton = 15;
			}

			if (Skills.getCurrentLevel(SKILLS.FLETCHING) >= 63) {
				makeRB = true;
				println("Time to make Ruby bolts.");
				General.sleep(300);
				break;
			}

			General.sleep(General.random(400, 1200));

			if (Inventory.find(item1).length == 0) {
				println("I need to get some " + item1 + ".");

				Banking.openBank();

				if (!Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(200);
						return (Banking.isBankScreenOpen());
					}
				}, General.randomLong(5000, 6000))) {
				}
				;

				if (!Banking.isBankScreenOpen()) {
					break;
				}

				// If there aren't any item1 in the bank, clear filler then deposit,
				// and replace fillers.
				if (Banking.find(item2).length < 1 && Inventory.find(item2).length >= 1) {

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

				} else {
					Banking.depositAllExcept("Knife");

					if (!Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(200);
							return (Inventory.find(item2).length < 1);
						}
					}, General.randomLong(10000, 12000))) {
					}
					;

				}

				General.sleep(General.random(1000, 1100));

				if (Inventory.isFull()) {
					Banking.depositAllExcept("Knife");
					if (!Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(200);
							return (!Inventory.isFull());
						}
					}, General.randomLong(10000, 12000))) {
					}
					;
				}

				Banking.withdraw(27, item1);

				if (!Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(200);
						return (Inventory.find(item1).length > 26);
					}
				}, General.randomLong(10000, 12000))) {
				}
				;

				Banking.close();

				General.sleep(General.random(400, 600));

			}

			if (Inventory.find(item1).length > 0) {
				RSItem[] Logs = Inventory.find(item1);
				RSItem[] Knives = Inventory.find("Knife");

				if (Logs.length > 0 && Knives.length > 0) {

					if (Knives[0].click("Use")) {

						if (Logs[0].click("Use")) {
							General.sleep(1000);

							println("Making " + item2 + ".");
							RSInterface child = Interfaces.get(270, makeButton);
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
									return ((Inventory.find(item1).length == 0) || !isCurrentlyBusy());
								}
							}, General.randomLong(65000, 70000))) {
							}
							;
						}
					}
				}
			}

			General.sleep(2000);

			break;

		case RBMAKING:
			println("Case: RBMAKING");

			if (makeRB) {

				if (Inventory.find("Adamant bolts").length == 0 && Inventory.find("Ruby bolts").length == 1) {

					println("Out of bolt materials.");

					if (!Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(200);
							return (Banking.isBankScreenOpen());
						}
					}, General.randomLong(5000, 6000))) {
					}
					;

					if (!Banking.isBankScreenOpen()) {
						break;
					}

					if (Banking.isBankScreenOpen()) {
						RSItem[] fillers = Banking.find("Bank filler");
						fillers[0].click("Clear-All");
						General.sleep(2000);
						Banking.depositAll();
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

					s = -1;

					break;
				}

				if (Inventory.find("Adamant bolts").length != 0) {
					println("I need to make Ruby bolts.");

					if (Inventory.find("Adamant bolts").length > 0) {
						RSItem[] Bolts = Inventory.find("Adamant bolts");
						RSItem[] Tips = Inventory.find("Ruby bolt tips");

						if (Bolts.length > 0 && Tips.length > 0) {

							if (Tips[0].click("Use")) {

								if (Bolts[0].click("Use")) {
									General.sleep(1000);

									println("Making Ruby bolts.");
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
											return ((Inventory.find("Adamant bolts").length == 0)
													|| !isCurrentlyBusy());
										}
									}, General.randomLong(65000, 70000))) {
									}
									;
								}
							}
						}
					}
				}

				General.sleep(200);
			}

			break;

		}
		return s;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		println("Goodbye world.");
		Login.logout();
		General.sleep(3000);
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
		LVLMAKING, RBMAKING
	}

	// Checks if a certain condition is met, then return that state.
	private State getState() {
		if (!makeRB) {
			state = State.LVLMAKING;
		}
		if (makeRB) {
			state = State.RBMAKING;
		}
		return state;
	}

}
