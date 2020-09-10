package scripts;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

@ScriptManifest(authors = {
		"Slippi, Sneakles" }, category = "Mud Runes", name = "Rune Mysteries", version = 0.1, description = "Day one of scripting.")
public class dayOne extends Script implements Loopable {

	State state;

	int j = 0;

	boolean hasEnteredDukeRoom = true;
	boolean hasTalkedToDuke = true;
	boolean hasReachedWizLadder = true;
	boolean hasEnteredWizRoom = true;
	boolean hasTalkedToWiz = true;
	boolean hasExitedWizRoom = true;
	boolean hasReachedVarrock;

	RSArea dukesRoom = new RSArea(new RSTile(3209, 3223, 1), new RSTile(3210, 3225, 1));
	RSArea wizLadder = new RSArea(new RSTile(3103, 3161, 0), new RSTile(3104, 3165, 0));
	RSArea wizRoom = new RSArea(new RSTile(3104, 9570, 0), new RSTile(3106, 9573, 0));
	RSArea wizBasement = new RSArea(new RSTile(3103, 9577, 0), new RSTile(3110, 9556, 0));
	RSArea exitWizRoom = new RSArea(new RSTile(3108, 9574, 0), new RSTile(3110, 9577, 0));
	RSArea runeShop = new RSArea(new RSTile(3252, 3402, 0), new RSTile(3254, 3401, 0));

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
		switch (getState()) {

		case WALKTODUKEROOM:
			println("Case: WALKTODUKEROOM");

			if (!hasEnteredDukeRoom) {
				println("Going upstairs.");

				WebWalking.walkTo(dukesRoom.getRandomTile());

				General.sleep(General.random(1000, 2000));

				if (dukesRoom.contains(Player.getPosition())) {
					hasEnteredDukeRoom = true;
				}

			}

			break;

		case TALKTODUKE:
			println("Case: TALKTODUKE");

			if (!hasTalkedToDuke) {
				println("Talking to the Duke.");

				RSNPC[] dukes = NPCs.findNearest("Duke Horacio");
				if (dukes[0] != null) {
					RSNPC duke = dukes[0];

					General.sleep(General.random(200, 500));

					duke.click("Talk-to");

					if (!Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.sleep(200);
							return NPCChat.getName() != null;
						}
					}, General.randomLong(10000, 12000)))
						;

					NPCChat.clickContinue(true);
					General.sleep(General.random(200, 500));
					NPCChat.selectOption("Have you any quests for me?", true);
					while (j < 5) {
						NPCChat.clickContinue(false);
						General.sleep(General.random(400, 500));
						j++;
					}
					j = 0;
					General.sleep(General.random(600, 1200));
					NPCChat.selectOption("Sure, no problem.", true);
					General.sleep(General.random(400, 500));
					while (j < 3) {
						println("fuck");
						NPCChat.clickContinue(true);
						General.sleep(General.random(300, 500));
						j++;
					}
					j = 0;
				}

				if (Inventory.find("Air talisman").length > 0) {
					hasTalkedToDuke = true;
				}

			}
			break;

		case WALKTOWIZ:
			println("Case: WALKTOWIZ");

			if (!hasReachedWizLadder) {
				println("Walking to wizLadder.");
				WebWalking.walkTo(wizLadder.getRandomTile());
				WebWalking.setUseRun(active);

				if (!Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(200);
						return wizLadder.contains(Player.getPosition());
					}
				}, General.randomLong(10000, 12000))) {
				}
				;

				if (wizLadder.contains(Player.getPosition())) {
					RSObject[] ladders = Objects.findNearest(20, "Ladder");
					if (ladders[0] != null) {
						RSObject ladder = ladders[0];
						General.sleep(General.random(200, 300));

						if (!wizBasement.contains(Player.getPosition())) {
							ladder.click("Climb-down");
						}
					}
				}

				General.sleep(1500, 2000);

				if (wizBasement.contains(Player.getPosition())) {
					hasReachedWizLadder = true;
				}
			}

			if (!hasEnteredWizRoom && hasReachedWizLadder) {
				println("Walking to wizRoom.");

				WebWalking.walkTo(wizRoom.getRandomTile());
				WebWalking.setUseRun(false);

				if (!Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(200);
						return wizRoom.contains(Player.getPosition());
					}
				}, General.randomLong(10000, 12000))) {
				}
				;

				if (wizRoom.contains(Player.getPosition())) {
					hasEnteredWizRoom = true;
				}
			}

			break;

		case TALKTOWIZ:
			println("Case: TALKTOWIZ");

			RSNPC[] wizs = NPCs.findNearest("Sedridor");
			if (wizs[0] != null) {
				RSNPC wiz = wizs[0];

				if (!DynamicClicking.clickRSNPC(wiz, "Talk-to")) {
					General.sleep(500, 750);
					hasEnteredWizRoom = false;
					break;
				}

				if (!Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(200);
						return NPCChat.getName() != null;
					}
				}, General.randomLong(10000, 12000)))
					;

				NPCChat.clickContinue(false);
				General.sleep(General.random(750, 800));
				NPCChat.selectOption("head wizard.", false);
				General.sleep(General.random(750, 800));
				while (j <= 3) {
					NPCChat.clickContinue(false);
					General.sleep(General.random(1000, 1500));
					j++;
				}
				j = 0;
				General.sleep(General.random(750, 800));
				println("Ok option");
				NPCChat.selectOption("Ok", false);
				while (j <= 6) {
					NPCChat.clickContinue(false);
					General.sleep(General.random(1000, 1500));
					j++;
				}
				j = 0;
				General.sleep(General.random(750, 800));
				NPCChat.selectOption("Yes", false);
				while (j <= 5) {
					General.sleep(General.random(750, 800));
					NPCChat.clickContinue(false);
					General.sleep(General.random(750, 800));
					j++;
				}
				j = 0;

				if (Inventory.find("Research package").length > 0) {
					hasTalkedToWiz = true;
				}

			} else {
				General.sleep(200);
				break;
			}

			break;

		case WALKTOVARROCK:
			println("Case: WALKTOVARROCK");

			if (!hasExitedWizRoom) {
				WebWalking.walkTo(exitWizRoom.getRandomTile());

				if (!Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(200);
						return exitWizRoom.contains(Player.getPosition());
					}
				}, General.randomLong(10000, 12000))) {
				}
				;

				if (exitWizRoom.contains(Player.getPosition())) {
					hasExitedWizRoom = true;
				}
			}

			RSObject[] ladders = Objects.findNearest(20, "Ladder");
			if (ladders[0] != null) {
				RSObject ladder = ladders[0];
				General.sleep(General.random(200, 300));

				if (wizBasement.contains(Player.getPosition())) {
					ladder.click("Climb-up");
				}
			}

			General.sleep(5000);

			WebWalking.walkTo(runeShop.getRandomTile());

			if (!Timing.waitCondition(new Condition() {
				@Override
				public boolean active() {
					General.sleep(200);
					return runeShop.contains(Player.getPosition());
				}
			}, General.randomLong(180000, 200000))) {
			}
			;

			println("You made it.");
			General.sleep(5000);

			break;
		}
		return 1000;

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		println("Goodbye world.");
	}

	// State names
	private enum State {
		WALKTODUKEROOM, TALKTODUKE, WALKTOWIZ, TALKTOWIZ, WALKTOVARROCK //After talk to Aubury, do WALKTOWIZ by setting ladder and wizRoom to false again. Item check end Aubury talk.
	}

	// Checks if a certain condition is met, then return that state.
	private State getState() {
		if (!hasEnteredDukeRoom) {
			state = State.WALKTODUKEROOM;
		}
		if (hasEnteredDukeRoom && !hasTalkedToDuke) {
			state = State.TALKTODUKE;
		}
		if (hasTalkedToDuke && !hasEnteredWizRoom) {
			state = State.WALKTOWIZ;
		}
		if (hasEnteredWizRoom && !hasTalkedToWiz) {
			state = State.TALKTOWIZ;
		}
		if (hasTalkedToWiz && !hasReachedVarrock) {
			state = State.WALKTOVARROCK;
		}
		return state;
	}

}
