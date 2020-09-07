package scripts;

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
	boolean hasReachedWizLadder;
	boolean hasEnteredWizRoom;
	boolean hasTalkedToWiz;

	RSArea dukesRoom = new RSArea(new RSTile(3209, 3223, 1), new RSTile(3210, 3225, 1));
	RSArea wizLadder = new RSArea(new RSTile(3107, 3160, 0), new RSTile(3105, 3162, 0));
	RSArea wizRoom = new RSArea(new RSTile(3104, 9570, 0), new RSTile(3106, 9573, 0));

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
					hasReachedWizLadder = true;
				}
				;
			}

			if (!hasEnteredWizRoom) {
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
					hasEnteredWizRoom = true;
				}
				;
			}

			break;
			
		case TALKTOWIZ:
			println("Case: TALKTOWIZ");
			General.sleep(5000);
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
		WALKTODUKEROOM, TALKTODUKE, WALKTOWIZ, TALKTOWIZ
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
		return state;
	}

}
