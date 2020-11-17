package scripts;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;

@SuppressWarnings("deprecation")
@ScriptManifest(authors = {
		"Slippi, Sneakles" }, category = "Mud Runes", name = "The Dig Site", version = 1, description = "Does The Dig Site quest.")
public class TheDigSite extends Script implements Loopable {

	State state;

	boolean banking = true;
	boolean equipping = true;
	boolean hasWalkedToExamCentre = true;
	boolean hasTalkedToExaminer = true;
	boolean hasWalkedToMuseum;
	boolean hasTalkedToCurator;
	boolean hasReturnedToExamCentre;
	boolean hasTalkedToExaminer2;
	boolean hasFoundTeddyBear;
	boolean hasFoundPanningTray;
	boolean hasTalkedToPanningGuide;
	boolean hasFoundSpecialCup;
	boolean hasFoundAnimalSkullAndSpecimenBrush;
	boolean hasTalkedToFemaleStudent;
	boolean hasTalkedToStudentInOrange;
	boolean hasTalkedToStudentInGreen;
	boolean hasTalkedToExaminer3;
	boolean hasReturnedToFemaleStudent;
	boolean hasReturnedToStudentInOrange;
	boolean hasReturnedToStudentInGreen;
	
	

	RSArea examCentre = new RSArea(new RSTile(3361, 3342, 0), new RSTile(3363, 3340, 0));

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

		println("Starting script.");

		DaxWalker.setCredentials(new DaxCredentialsProvider() {
			@Override
			public DaxCredentials getDaxCredentials() {
				return new DaxCredentials("sub_IOoPK5QRFwMbww", "9b8ce8a6-9604-481e-8dff-d665db473d8f");
			}
		});

	}

	@Override
	public int onLoop() {

		switch (getState()) {

		case PREP:

			if (!banking) {

				println("Getting quest items from the bank.");

				if (!Banking.isBankLoaded()) {

					Banking.openBank();
					Banking.waitUntilLoaded(3000);
					General.sleep(600, 1200);

				}

				else if (Banking.isBankScreenOpen()) {

					int[] itemID = { 233, 590, 229, 1978, 1609, 973, 1059, 1061, 12625, 3008, 8007 };
					int[] itemID2 = { 954, 8007 };

					withdrawItems(itemID, 1);

					if (!Timing.waitCondition(new Condition() {

						@Override
						public boolean active() {

							return (itemCheck(itemID, 1));

						}

					}, General.random(15000, 20000)))
						;

					withdrawItems(itemID2, 2);

					if (!Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {

							General.sleep(300);
							return (itemCheck(itemID2, 2));

						}
					}, General.random(15000, 30000)))
						;

					General.sleep(General.randomSD(3000, 1200));

					if (itemCheck(itemID, 1) && itemCheck(itemID2, 2)) {

						banking = true;
						Banking.close();
						General.sleep(600, 1200);

					}

				}

			}

			if (banking) {

				println("Equipping leather boots and leather gloves.");

				if (!equipItem(1061)) {
					break;
				}

				if (!equipItem(1059)) {
					break;
				}

				if (Inventory.find(1059).length < 1 && Inventory.find(1061).length < 1) {

					equipping = true;

				}

			}

			break;

		case WALKTOEXAMCENTRE:

			if (!hasWalkedToExamCentre) {

				println("Walking to Exam Centre.");

				DaxWalker.walkTo(examCentre.getRandomTile());
				General.sleep(1800, 2400);

				if (examCentre.contains(Player.getPosition())) {

					hasWalkedToExamCentre = true;

				}

			}

			break;

		case TALKTOEXAMINER:

			if (Game.getSetting(131) == 0) {

				println("Talking to Examiner.");

				RSNPC[] Examiners = NPCs.findNearest("Examiner");
				RSNPC Examiner = Examiners[0];
				Examiner.adjustCameraTo();
				Examiner.click("Talk-to");
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.selectOption("Can I take an exam?", false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));

			}

			if (Game.getSetting(131) == 1) {

				hasTalkedToExaminer = true;

			}
			break;

		case WALKTOVARROCKMUSEUM:

			break;
		case TALKTOCURATOR:
			
			break;
		}
		return 50;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		println("Stopping script.");
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

	private enum State {

		PREP, WALKTOEXAMCENTRE, TALKTOEXAMINER, WALKTOVARROCKMUSEUM, TALKTOCURATOR,

	}

	private State getState() {

		if (!banking) {
			state = State.PREP;
		} else if (!hasWalkedToExamCentre && banking) {
			state = State.WALKTOEXAMCENTRE;
		} else if (!hasTalkedToExaminer && hasWalkedToExamCentre) {
			state = State.TALKTOEXAMINER;
		} else if (!hasWalkedToMuseum && hasTalkedToExaminer) {
			state = State.WALKTOVARROCKMUSEUM;
		} else if (!hasTalkedToCurator && hasWalkedToMuseum) {
			state = State.TALKTOCURATOR;
		}

		return state;
	}

	private boolean withdrawItems(int[] x, int y) {

		int i = 0;

		while (i <= x.length - 1) {

			Banking.withdraw(y, x[i]);
			General.sleep(General.randomSD(600, 300));

			i++;
		}

		return (false);

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

	public boolean equipItem(int itemID) {
		if (Inventory.find(itemID).length > 0) {
			RSItem[] x = Inventory.find(itemID);

			x[0].click("Wear");
			General.sleep(600, 900);
		} else {
			return false;
		}

		if (Inventory.find(itemID).length < 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean convoWait(String x) {

		String currentMessage = NPCChat.getMessage();
		General.sleep(600);
		println(currentMessage);
		if (x == "c") {
			NPCChat.clickContinue(false);
			General.sleep(General.randomSD(1200, 300));

			int i = 0;
			while (NPCChat.getMessage() == null && NPCChat.getMessage().contains(currentMessage) && i < 5) {

				println("Looping");
				General.sleep(2400, 3000);
				i++;

				if (NPCChat.getMessage() == null) {
					break;
				}
			}

			General.sleep(General.randomSD(1800, 300));

			if (NPCChat.getMessage() == null) {
				return true;
			}

			if (!NPCChat.getMessage().contains(currentMessage)) {
				println(NPCChat.getMessage());
				return true;
			} else
				println("Returned false");
			return false;
		} else {
			NPCChat.selectOption(x, false);
			General.sleep(General.randomSD(1200, 300));
			return true;
		}

	}

}