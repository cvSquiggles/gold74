package scripts;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Game;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
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
	boolean hasWalkedToMuseum = true;
	boolean hasTalkedToCurator = true;
	boolean hasReturnedToExamCentre = true;
	boolean hasTalkedToExaminer2 = true;
	boolean hasFoundTeddyBear = true;
	boolean hasFoundPanningTray = true;
	boolean hasTalkedToPanningGuide = true;
	boolean hasWalkedToPanningPoint = true;
	boolean hasFoundSpecialCup = true;
	boolean hasWalkedToWorkman = true;
	boolean hasFoundAnimalSkullAndSpecimenBrush = true;
	boolean hasWalkedToFemaleStudent = true;
	boolean hasTalkedToFemaleStudent = true;
	boolean hasWalkedToStudentInOrange = true;
	boolean hasTalkedToStudentInOrange = true;
	boolean hasWalkedToStudentInGreen = true;
	boolean hasTalkedToStudentInGreen = true;
	boolean hasReturnedToExamCentre2 = true;
	boolean hasTalkedToExaminer3 = true;
	boolean hasWalkedToFemaleStudent2 = true;
	boolean hasReturnedToFemaleStudent = true;
	boolean hasWalkedToStudentInOrange2 = true;
	boolean hasReturnedToStudentInOrange = true;
	boolean hasWalkedToStudentInGreen2 = true;
	boolean hasReturnedToStudentInGreen = true;
	boolean hasReturnedToExamCentre3 = true;
	boolean hasTalkedToExaminer4 = true;
	boolean hasWalkedToFemaleStudent3 = true;
	boolean hasReturnedToFemaleStudent2a = true;
	boolean hasReturnedToFemaleStudent2b = true;
	boolean hasWalkedToStudentInOrange3 = true;
	boolean hasReturnedToStudentInOrange2 = true;
	boolean hasWalkedToStudentInGreen3 = true;
	boolean hasReturnedToStudentInGreen2 = true;
	boolean hasReturnedToExamCentre4 = true;
	boolean hasTalkedToExaminer5 = true;
	boolean hasSearchedCupboards = true;
	boolean hasWalkedToDigsite7 = true;
	boolean hasFoundAncientTalisman = true;
	boolean hasReturnedToExamCentre5;
	boolean hasTalkedToExpert;

	RSArea examCentre = new RSArea(new RSTile(3361, 3342, 0), new RSTile(3363, 3340, 0));
	RSArea museum = new RSArea(new RSTile(3258, 3450, 0), new RSTile(3254, 3447, 0));
	RSArea bush = new RSArea(new RSTile(3358, 3372, 0), new RSTile(3359, 3371, 0));
	RSArea tent = new RSArea(new RSTile(3370, 3380, 0), new RSTile(3372, 3378, 0));
	RSArea panningPoint = new RSArea(new RSTile(3377, 3380, 0), new RSTile(3379, 3378, 0));
	RSArea workmanArea = new RSArea(new RSTile(3352, 3410, 0), new RSTile(3355, 3407, 0));
	RSArea femaleStudent = new RSArea(new RSTile(3346, 3421, 0), new RSTile(3348, 3419, 0));
	RSArea orangeStudent = new RSArea(new RSTile(3369, 3418, 0), new RSTile(3371, 3416, 0));
	RSArea greenStudent = new RSArea(new RSTile(3362, 3400, 0), new RSTile(3364, 3398, 0));
	RSArea cupboard1 = new RSArea(new RSTile(3355, 3333, 0), new RSTile(3354, 3333, 0));
	RSArea cupboard2 = new RSArea(new RSTile(3356, 3336, 0), new RSTile(3355, 3336, 0));
	RSArea digsite7 = new RSArea(new RSTile(3371, 3441, 0), new RSTile(3376, 3438, 0));

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

				if (!useItem(1061, "Wear")) {
					break;
				}

				if (!useItem(1059, "Wear")) {
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
				convoWait("c", 6);
				convoWait("Can I take an exam?", 0);
				convoWait("c", 8);
				General.sleep(General.randomSD(2400, 300));

			}

			if (Game.getSetting(131) == 1) {

				hasTalkedToExaminer = true;

			}

			break;

		case WALKTOVARROCKMUSEUM:

			println("Case: WALKTOVARROCKMUSEUM");

			if (!hasWalkedToMuseum) {

				println("Walking to Exam Centre.");

				DaxWalker.walkTo(museum.getRandomTile());
				General.sleep(1800, 2400);

				if (museum.contains(Player.getPosition())) {

					hasWalkedToMuseum = true;

				}

			}

			break;

		case TALKTOCURATOR:

			if (Game.getSetting(135) == 256) {

				println("Talking to Curator.");

				RSNPC[] Curators = NPCs.findNearest(5214);
				RSNPC Curator = Curators[0];
				Curator.adjustCameraTo();
				Curator.click("Talk-to");
				General.sleep(General.randomSD(2400, 300));

				convoWait("c", 5);

				if (Game.getSetting(135) == 768) {

					hasTalkedToCurator = true;

				}
			}

			break;

		case RETURNTOEXAMCENTRE:

			if (!hasReturnedToExamCentre) {

				println("Returning to Exam Centre.");

				DaxWalker.walkTo(examCentre.getRandomTile());
				General.sleep(1800, 2400);

				if (examCentre.contains(Player.getPosition())) {

					hasReturnedToExamCentre = true;

				}

			}

			break;

		case TALKTOEXAMINER2:

			if (Game.getSetting(131) == 1) {

				println("Talking to Examiner.");

				RSNPC[] Examiners = NPCs.findNearest("Examiner");
				RSNPC Examiner = Examiners[0];
				Examiner.adjustCameraTo();
				Examiner.click("Talk-to");
				General.sleep(General.randomSD(2400, 300));

				convoWait("c", 5);
				convoWait("c", 5);
				convoWait("The", 0);
				convoWait("c", 3);
				convoWait("Magic", 0);
				convoWait("c", 3);
				convoWait("Rubber", 0);
				convoWait("c", 3);

			}

			if (Game.getSetting(131) == 2) {

				hasTalkedToExaminer2 = true;

			}

			break;

		case FINDTEDDYBEAR:

			if (Inventory.find(673).length == 0) {

				println("Walking to bush to pick up Teddy Bear.");

				DaxWalker.walkTo(bush.getRandomTile());
				General.sleep(General.randomSD(2400, 300));

				RSObject[] bushes = Objects.findNearest(10, 2358);
				bushes[0].click("Search");
				General.sleep(General.randomSD(900, 300));

			}

			if (Inventory.find(673).length > 0) {

				hasFoundTeddyBear = true;

			}

			break;

		case FINDPANNINGTRAY:

			if (Inventory.find(677).length == 0) {

				println("Walking to tent to pick up Panning Tray.");

				DaxWalker.walkTo(tent.getRandomTile());
				General.sleep(General.randomSD(2400, 300));

				RSGroundItem[] panningTray = GroundItems.findNearest(677);
				panningTray[0].click("Take");
				General.sleep(General.randomSD(1800, 300));

			}

			if (Inventory.find(677).length > 0) {

				hasFoundPanningTray = true;

			}

			break;

		case TALKTOPANNINGGUIDE:

			if (!hasTalkedToPanningGuide) {

				println("Talking to Panning Guide.");

				RSNPC[] PanningGuides = NPCs.findNearest(3640);
				RSNPC PanningGuide = PanningGuides[0];
				PanningGuide.adjustCameraTo();
				PanningGuide.click("Talk-to");

				waitForConvo("TradeRN");
				convoWait("c", 7);

				if (NPCChat.getMessage().contains("Ah! Lovely!")) {

					convoWait("c", 1);
					hasTalkedToPanningGuide = true;

				} else
					break;

			}

			break;

		case PANNING:

			if (!hasWalkedToPanningPoint) {

				println("Walking to panning point.");

				DaxWalker.walkTo(panningPoint.getRandomTile());
				General.sleep(General.randomSD(2400, 300));

				if (panningPoint.contains(Player.getPosition())) {

					hasWalkedToPanningPoint = true;

				}

			}

			if (Inventory.find(672).length == 0) {

				println("Panning for Special Cup.");

				RSObject[] panningPoint = Objects.findNearest(2, 2363);
				panningPoint[0].click("Pan");
				General.sleep(General.randomSD(4400, 300));

				if (Inventory.find(679).length == 1) {

					useItem(679, "Search");

				}

				if (Inventory.find(672).length == 1) {

					hasFoundSpecialCup = true;

				}

			}

			break;

		case STEALFROMWORKMAN:

			if (!hasWalkedToWorkman) {

				println("Walking to workman area.");

				DaxWalker.walkTo(workmanArea.getRandomTile());
				General.sleep(General.randomSD(2400, 300));

				if (workmanArea.contains(Player.getPosition())) {

					hasWalkedToWorkman = true;

				}
			}

			if (hasWalkedToWorkman) {
				if (Inventory.find(671).length < 1 || Inventory.find(670).length < 1) {

					println("Stealing from Digsite workman.");

					RSNPC[] digsiteWorkmans = NPCs.findNearest(3630);
					RSNPC digsiteWorkman = digsiteWorkmans[0];
					digsiteWorkman.adjustCameraTo();
					digsiteWorkman.click("Steal-from");
					General.sleep(General.randomSD(4800, 300));

				}

				if (Inventory.find(671).length == 1 && Inventory.find(670).length == 1) {

					hasFoundAnimalSkullAndSpecimenBrush = true;

				}
			}

			break;

		case TALKTOFEMALESTUDENT:

			if (!hasWalkedToFemaleStudent) {

				println("Walking to female student.");

				DaxWalker.walkTo(femaleStudent.getRandomTile());
				General.sleep(General.randomSD(2400, 300));

				if (femaleStudent.contains(Player.getPosition())) {

					hasWalkedToFemaleStudent = true;

				}
			}

			if (hasWalkedToFemaleStudent) {

				if (Inventory.find(673).length == 1) {

					println("Talking to female student.");

					RSNPC[] femaleStudents = NPCs.findNearest(3634);
					RSNPC femaleStudent = femaleStudents[0];
					femaleStudent.adjustCameraTo();
					femaleStudent.click("Talk-to");

					waitForConvo("TradeRN");
					convoWait("c", 7);

				}

				if (Inventory.find(673).length == 0) {

					hasTalkedToFemaleStudent = true;

				}
			}

			break;

		case TALKTOORANGESTUDENT:

			if (!hasWalkedToStudentInOrange) {

				println("Walking to student in orange.");

				DaxWalker.walkTo(orangeStudent.getRandomTile());
				General.sleep(General.randomSD(2400, 300));

				if (orangeStudent.contains(Player.getPosition())) {

					hasWalkedToStudentInOrange = true;

				}
			}

			if (hasWalkedToStudentInOrange) {
				if (Inventory.find(672).length == 1) {

					println("Talking to student in orange.");

					RSNPC[] orangeStudents = NPCs.findNearest(3633);
					RSNPC orangeStudent = orangeStudents[0];
					orangeStudent.adjustCameraTo();
					orangeStudent.click("Talk-to");

					waitForConvo("TradeRN");
					convoWait("c", 8);

				}

				if (Inventory.find(672).length == 0) {

					hasTalkedToFemaleStudent = true;

				}
			}

			break;

		case TALKTOGREENSTUDENT:

			if (!hasWalkedToStudentInGreen) {

				println("Walking to student in green.");

				DaxWalker.walkTo(greenStudent.getRandomTile());
				General.sleep(General.randomSD(2400, 300));

				if (greenStudent.contains(Player.getPosition())) {

					hasWalkedToStudentInGreen = true;

				}
			}

			if (hasWalkedToStudentInGreen) {
				if (Inventory.find(671).length == 1) {

					println("Talking to student in green.");

					RSNPC[] greenStudents = NPCs.findNearest(3632);
					RSNPC greenStudent = greenStudents[0];
					greenStudent.adjustCameraTo();
					greenStudent.click("Talk-to");

					waitForConvo("TradeRN");
					convoWait("c", 8);

				}

				if (Inventory.find(671).length == 0) {

					hasTalkedToFemaleStudent = true;

				}
			}

			break;

		case RETURNTOEXAMCENTRE2:

			if (!hasReturnedToExamCentre2) {

				println("Returning to Exam Centre.");

				DaxWalker.walkTo(examCentre.getRandomTile());
				General.sleep(1800, 2400);

				if (examCentre.contains(Player.getPosition())) {

					hasReturnedToExamCentre2 = true;

				}

			}

			break;

		case TALKTOEXAMINER3:

			if (Game.getSetting(131) == 2) {

				println("Talking to Examiner.");

				RSNPC[] Examiners = NPCs.findNearest("Examiner");
				RSNPC Examiner = Examiners[0];
				Examiner.adjustCameraTo();
				Examiner.click("Talk-to");

				waitForConvo("TradeRN");
				convoWait("c", 2);
				convoWait("Yes", 0);
				convoWait("c", 3);
				convoWait("The study of the earth", 0);
				convoWait("c", 3);
				convoWait("All that have passed", 0);
				convoWait("c", 3);
				convoWait("Gloves and boots", 0);
				convoWait("c", 5);
				General.sleep(General.randomSD(2400, 300));

				break;

			}

			if (Game.getSetting(131) == 3) {

				hasTalkedToExaminer3 = true;

			}

			break;

		case RETURNTOFEMALESTUDENT:

			if (!hasWalkedToFemaleStudent2) {

				println("Walking to female student.");

				DaxWalker.walkTo(femaleStudent.getRandomTile());
				General.sleep(General.randomSD(2400, 300));

				if (femaleStudent.contains(Player.getPosition())) {
				}
			}

			if (!hasReturnedToFemaleStudent) {
				println("Talking to female student.");

				RSNPC[] femaleStudents = NPCs.findNearest(3634);
				RSNPC femaleStudent = femaleStudents[0];
				femaleStudent.adjustCameraTo();
				femaleStudent.click("Talk-to");

				waitForConvo("TradeRN");
				convoWait("c", 5);

				checkConvoComplete(hasReturnedToFemaleStudent, "Great, thanks for your advice");
			}

			break;

		case RETURNTOORANGESTUDENT:

			if (!hasWalkedToStudentInOrange2) {

				println("Walking to student in orange.");

				DaxWalker.walkTo(orangeStudent.getRandomTile());
				General.sleep(General.randomSD(2400, 300));

				if (orangeStudent.contains(Player.getPosition())) {
					hasWalkedToStudentInOrange2 = true;
				}
			}

			if (!hasReturnedToStudentInOrange) {

				println("Talking to student in orange.");

				RSNPC[] orangeStudents = NPCs.findNearest(3633);
				RSNPC orangeStudent = orangeStudents[0];
				orangeStudent.adjustCameraTo();
				orangeStudent.click("Talk-to");

				waitForConvo("TradeRN");
				convoWait("c", 4);

			}

			checkConvoComplete(hasReturnedToStudentInOrange, "Thanks for the information.");

			break;

		case RETURNTOGREENSTUDENT:

			if (!hasWalkedToStudentInGreen2) {

				println("Walking to student in green.");

				DaxWalker.walkTo(greenStudent.getRandomTile());
				General.sleep(General.randomSD(2400, 300));

				if (greenStudent.contains(Player.getPosition())) {
					hasWalkedToStudentInGreen2 = true;
				}
			}

			if (!hasReturnedToStudentInGreen) {
				println("Talking to student in green.");

				RSNPC[] greenStudents = NPCs.findNearest(3632);
				RSNPC greenStudent = greenStudents[0];
				greenStudent.adjustCameraTo();
				greenStudent.click("Talk-to");

				waitForConvo("TradeRN");
				convoWait("c", 4);
				General.sleep(General.randomSD(1200, 300));

				checkConvoComplete(hasReturnedToStudentInGreen, "Okay, I'll");
			}

			break;

		case TALKTOEXAMINER4:

			if (!hasReturnedToExamCentre3) {

				println("Returning to Exam Centre.");

				DaxWalker.walkTo(examCentre.getRandomTile());
				General.sleep(1800, 2400);

				if (examCentre.contains(Player.getPosition())) {

					hasReturnedToExamCentre3 = true;

				}

			}

			if (Game.getSetting(131) == 3) {

				println("Talking to Examiner.");

				RSNPC[] Examiners = NPCs.findNearest("Examiner");
				RSNPC Examiner = Examiners[0];
				Examiner.adjustCameraTo();
				Examiner.click("Talk-to");

				waitForConvo("TradeRN");
				convoWait("c", 2);
				convoWait("I am ready", 0);
				convoWait("c", 3);
				convoWait("Samples taken in rough", 0);
				convoWait("c", 3);
				convoWait("Finds must be carefully", 0);
				convoWait("c", 3);
				convoWait("Always handle with care", 0);
				convoWait("c", 5);
				General.sleep(General.randomSD(2400, 300));

			}

			if (Game.getSetting(131) == 4) {

				hasTalkedToExaminer4 = true;

			}

			break;

		case RETURNTOFEMALESTUDENT2:

			if (!hasWalkedToFemaleStudent3) {

				println("Walking to female student.");

				DaxWalker.walkTo(femaleStudent.getRandomTile());
				General.sleep(General.randomSD(2400, 300));

				if (femaleStudent.contains(Player.getPosition())) {
					hasWalkedToFemaleStudent3 = true;
				}
			}

			if (!hasReturnedToFemaleStudent2a) {

				println("Talking to female student.");

				RSNPC[] femaleStudents = NPCs.findNearest(3634);
				RSNPC femaleStudent = femaleStudents[0];
				femaleStudent.adjustCameraTo();
				femaleStudent.click("Talk-to");

				waitForConvo("TradeRN");
				convoWait("c", 7);

				General.sleep(General.randomSD(2400, 300));

				checkConvoComplete(hasReturnedToFemaleStudent2a, "OK, I'll");
			}

			if (!hasReturnedToFemaleStudent2b) {

				RSNPC[] femaleStudents = NPCs.findNearest(3634);
				RSNPC femaleStudent = femaleStudents[0];
				femaleStudent.adjustCameraTo();
				femaleStudent.click("Talk-to");

				waitForConvo("TradeRN");
				convoWait("c", 7);

				General.sleep(General.randomSD(2400, 300));

				checkConvoComplete(hasReturnedToFemaleStudent2b, "Great, thanks");

			}

			break;

		case RETURNTOORANGESTUDENT2:

			if (!hasWalkedToStudentInOrange3) {

				println("Walking to student in orange.");

				DaxWalker.walkTo(orangeStudent.getRandomTile());
				General.sleep(General.randomSD(2400, 300));

				if (orangeStudent.contains(Player.getPosition())) {
					hasWalkedToStudentInOrange3 = true;
				}
			}

			if (!hasReturnedToStudentInOrange2) {
				println("Talking to student in orange.");

				RSNPC[] orangeStudents = NPCs.findNearest(3633);
				RSNPC orangeStudent = orangeStudents[0];
				orangeStudent.adjustCameraTo();
				orangeStudent.click("Talk-to");

				waitForConvo("TradeRN");
				convoWait("c", 4);

				General.sleep(General.randomSD(2400, 300));

				checkConvoComplete(hasReturnedToStudentInOrange2, "Thanks for the");

			}

			break;

		case RETURNTOGREENSTUDENT2:

			if (!hasReturnedToStudentInGreen2) {

				println("Walking to student in green.");

				DaxWalker.walkTo(greenStudent.getRandomTile());
				General.sleep(General.randomSD(2400, 300));

				if (greenStudent.contains(Player.getPosition())) {
					hasWalkedToStudentInGreen3 = true;
				}
			}

			if (!hasReturnedToStudentInGreen2) {

				println("Talking to student in green.");

				RSNPC[] greenStudents = NPCs.findNearest(3632);
				RSNPC greenStudent = greenStudents[0];
				greenStudent.adjustCameraTo();
				greenStudent.click("Talk-to");

				waitForConvo("TradeRN");
				convoWait("c", 5);
				General.sleep(General.randomSD(2400, 300));

				checkConvoComplete(hasReturnedToStudentInGreen2, "Okay, I'll remember");

			}

			break;

		case TALKTOEXAMINER5:

			if (!hasReturnedToExamCentre4) {

				println("Returning to Exam Centre.");

				DaxWalker.walkTo(examCentre.getRandomTile());
				General.sleep(1800, 2400);

				if (examCentre.contains(Player.getPosition())) {

					hasReturnedToExamCentre4 = true;

				}

			}

			if (Game.getSetting(131) == 4) {

				println("Talking to Examiner.");

				RSNPC[] Examiners = NPCs.findNearest("Examiner");
				RSNPC Examiner = Examiners[0];
				Examiner.adjustCameraTo();
				Examiner.click("Talk-to");

				waitForConvo("TradeRN");
				convoWait("c", 2);
				convoWait("I am ready", 0);
				convoWait("c", 3);
				convoWait("Samples cleaned,", 0);
				convoWait("c", 3);
				convoWait("Brush carefully and slowly", 0);
				convoWait("c", 3);
				convoWait("Handle bones very carefully", 0);
				convoWait("c", 8);
				General.sleep(General.randomSD(2400, 300));

			}

			if (Game.getSetting(131) == 5) {

				hasTalkedToExaminer4 = true;

			}

			break;

		case HASSEARCHEDCUPBOARD:

			if (Inventory.find(669).length == 0) {

				println("Walking to first cupboard to get specimen jar.");

				DaxWalker.walkTo(cupboard1.getRandomTile());
				General.sleep(General.randomSD(4800, 300));

				if (Objects.findNearest(10, 17302).length > 0) {

					println("Opening cupboard.");

					RSObject[] unopenedCupboard1 = Objects.findNearest(10, 17302);
					unopenedCupboard1[0].click("Open");
					General.sleep(General.randomSD(4800, 300));

				}

				if (Objects.findNearest(10, 17303).length > 0) {

					RSObject[] openedCupboard1 = Objects.findNearest(10, 17303);
					openedCupboard1[0].click("Search");
					General.sleep(General.randomSD(4800, 300));

				}

			}

			if (Inventory.find(669).length == 1 && Inventory.find(675).length == 0) {

				println("Walking to second cupboard to get rock pick.");

				DaxWalker.walkTo(cupboard2.getRandomTile());
				General.sleep(General.randomSD(4800, 300));

				if (Objects.findNearest(10, 17300).length > 0) {

					RSObject[] unopenedCupboard2 = Objects.findNearest(10, 17300);
					unopenedCupboard2[0].click("Open");
					General.sleep(General.randomSD(4800, 300));

				}

				if (Objects.findNearest(10, 17301).length > 0) {

					RSObject[] openedCupboard2 = Objects.findNearest(10, 17301);
					openedCupboard2[0].click("Search");
					General.sleep(General.randomSD(4800, 300));

				}

			}

			if (Inventory.find(669).length == 1 && Inventory.find(675).length == 1) {

				hasSearchedCupboards = true;

			}

			break;

		case FINDANCIENTTALISMAN:

			// travel(hasWalkedToDigsite7, digsite7);
			if (!hasWalkedToDigsite7) {

				println("Walking to " + "the place" + ".");

				DaxWalker.walkTo(digsite7.getRandomTile());
				General.sleep(General.randomSD(2400, 300));

				if (digsite7.contains(Player.getPosition())) {
					hasWalkedToDigsite7 = true;
				}
			}

			if (!hasFoundAncientTalisman) {

				if (Inventory.find(681).length == 0) {
					
					if (Inventory.isFull()) {
						
						DaxWalker.walkTo(new RSTile(3374, 3435, 0));
						
						General.sleep(General.random(600, 900));
						
						Inventory.dropAllExcept(233, 590, 229, 677, 693, 973, 676, 8007, 12625, 3008, 954,
								670, 669, 675);
						General.sleep(General.random(600, 900));
						
						DaxWalker.walkTo(new RSTile(3373, 3441, 0));
					}
					
					println("Troweling for Ancient talisman.");
					
					if (Inventory.find(676).length > 0) {
						RSItem[] trowels = Inventory.find(676);

						trowels[0].click("Use");
						General.sleep(600, 900);
					} 

					if (Objects.findNearest(10, 2378).length > 0) {
						RSObject[] soils = Objects.findNearest(10, 2378);
						soils[0].click("Use");
						General.sleep(General.randomSD(4800, 600));
					}
					
					if (Inventory.find(681).length > 0) {
						hasFoundAncientTalisman = true;
						println("We found it.");
					}
					
				}

			}

			break;
			
		case TALKTOEXPERT:
			
			if (!hasReturnedToExamCentre5) {

				println("Walking to Exam Centre.");

				DaxWalker.walkTo(examCentre.getRandomTile());
				General.sleep(General.randomSD(2400, 300));

				if (examCentre.contains(Player.getPosition())) {
					hasReturnedToExamCentre5 = true;
					DaxWalker.walkTo(new RSTile(3358, 3335, 0));
					General.sleep(600, 900);
				}
			}
			
			if (Inventory.find(696).length == 0) {

				println("Talking to Expert.");

				RSNPC[] Experts = NPCs.findNearest(3639);
				RSNPC Expert = Experts[0];
				Expert.adjustCameraTo();
				Expert.click("Talk-to");

				waitForConvo("TradeRN");
				convoWait("c", 20);
				General.sleep(General.randomSD(2400, 300));

			}

			if (Inventory.find(696).length > 0) {

				hasTalkedToExpert = true;
				println("You did it.");

			}
			
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

		PREP, WALKTOEXAMCENTRE, TALKTOEXAMINER, WALKTOVARROCKMUSEUM, TALKTOCURATOR, RETURNTOEXAMCENTRE, TALKTOEXAMINER2,
		FINDTEDDYBEAR, FINDPANNINGTRAY, TALKTOPANNINGGUIDE, PANNING, STEALFROMWORKMAN, TALKTOFEMALESTUDENT,
		TALKTOORANGESTUDENT, TALKTOGREENSTUDENT, RETURNTOEXAMCENTRE2, TALKTOEXAMINER3, RETURNTOFEMALESTUDENT,
		RETURNTOORANGESTUDENT, RETURNTOGREENSTUDENT, TALKTOEXAMINER4, RETURNTOFEMALESTUDENT2, RETURNTOORANGESTUDENT2,
		RETURNTOGREENSTUDENT2, TALKTOEXAMINER5, HASSEARCHEDCUPBOARD, FINDANCIENTTALISMAN, TALKTOEXPERT,
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
		} else if (!hasReturnedToExamCentre && hasTalkedToCurator) {
			state = State.RETURNTOEXAMCENTRE;
		} else if (!hasTalkedToExaminer2 && hasReturnedToExamCentre) {
			state = State.TALKTOEXAMINER2;
		} else if (!hasFoundTeddyBear && hasTalkedToExaminer2) {
			state = State.FINDTEDDYBEAR;
		} else if (!hasFoundPanningTray && hasFoundTeddyBear) {
			state = State.FINDPANNINGTRAY;
		} else if (!hasTalkedToPanningGuide && hasFoundPanningTray) {
			state = State.TALKTOPANNINGGUIDE;
		} else if (!hasFoundSpecialCup && hasTalkedToPanningGuide) {
			state = State.PANNING;
		} else if (!hasFoundAnimalSkullAndSpecimenBrush && hasFoundSpecialCup) {
			state = State.STEALFROMWORKMAN;
		} else if (!hasTalkedToFemaleStudent && hasFoundAnimalSkullAndSpecimenBrush) {
			state = State.TALKTOFEMALESTUDENT;
		} else if (!hasTalkedToStudentInOrange && hasTalkedToFemaleStudent) {
			state = State.TALKTOORANGESTUDENT;
		} else if (!hasTalkedToStudentInGreen && hasTalkedToStudentInOrange) {
			state = State.TALKTOGREENSTUDENT;
		} else if (!hasReturnedToExamCentre2 && hasTalkedToStudentInGreen) {
			state = State.RETURNTOEXAMCENTRE2;
		} else if (!hasTalkedToExaminer3 && hasReturnedToExamCentre2) {
			state = State.TALKTOEXAMINER3;
		} else if (!hasReturnedToFemaleStudent && hasTalkedToExaminer3) {
			state = State.RETURNTOFEMALESTUDENT;
		} else if (!hasReturnedToStudentInOrange && hasReturnedToFemaleStudent) {
			state = State.RETURNTOORANGESTUDENT;
		} else if (!hasReturnedToStudentInGreen && hasReturnedToStudentInOrange) {
			state = State.RETURNTOGREENSTUDENT;
		} else if (!hasTalkedToExaminer4 && hasReturnedToStudentInGreen) {
			state = State.TALKTOEXAMINER4;
		} else if (!hasReturnedToFemaleStudent2a && hasTalkedToExaminer4) {
			state = State.RETURNTOFEMALESTUDENT2;
		} else if (!hasReturnedToStudentInOrange2 && hasReturnedToFemaleStudent2b) {
			state = State.RETURNTOORANGESTUDENT2;
		} else if (!hasReturnedToStudentInGreen2 && hasReturnedToStudentInOrange2) {
			state = State.RETURNTOGREENSTUDENT2;
		} else if (!hasTalkedToExaminer5 && hasReturnedToStudentInGreen2) {
			state = State.TALKTOEXAMINER5;
		} else if (!hasSearchedCupboards && hasTalkedToExaminer5) {
			state = State.HASSEARCHEDCUPBOARD;
		} else if (!hasFoundAncientTalisman && hasSearchedCupboards) {
			state = State.FINDANCIENTTALISMAN;
		} else if (!hasTalkedToExpert && hasFoundAncientTalisman) {
			state = State.TALKTOEXPERT;
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

	public boolean useItem(int itemID, String y) {
		if (Inventory.find(itemID).length > 0) {
			RSItem[] x = Inventory.find(itemID);

			x[0].click(y);
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

	public boolean convoWait(String x, int y) {
		// Set string x = to "c" if you would like to simply click continue, and int y
		// is the # of times to do so.
		// Set string x = to whatever text is contained in the NPCChat.getOptions()
		// option
		// that you'd like to click, be sure to still enter a y value, good default is
		// 0.
		String currentMessage = NPCChat.getMessage();
		General.sleep(600);
		if (x == "c") {
			int q = 0;
			while (q <= y) {
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(1200, 300));

				int i = 0;
				while (i <= 5) {
					if (NPCChat.getMessage() == null) {
						break;
					}

					General.sleep(2400, 3000);

					if (NPCChat.getMessage() == null) {

						break;
					}

					if (!NPCChat.getMessage().contains(currentMessage)) {

						break;
					}

					if (NPCChat.getMessage() == null) {

						return true;
					}

					if (!NPCChat.getMessage().contains(currentMessage) && q == y) {
						return true;
					} else if (q == y)
						return false;
				}
				q++;
			}
			return false;
		} else {
			if (NPCChat.getOptions() == null) {
				return false;
			}
			String chatOptions[] = NPCChat.getOptions();
			int q = 0;
			while (q < chatOptions.length) {
				General.sleep(300);
				if (chatOptions[q].contains(x)) {
					NPCChat.selectOption(x, false);
					General.sleep(General.randomSD(1200, 300));
					return true;
				} else
					q++;

				if (q >= chatOptions.length) {
					return false;
				}
			}
		}
		return false;
	}

	public boolean checkConvoComplete(boolean x, String y) {
		if (NPCChat.getMessage().contains(y)) {

			convoWait("c", 1);
			x = true;
			return true;
		} else
			return false;
	}

	public boolean travel(boolean bool, RSArea area) {

		if (!bool) {

			println("Walking to " + area.getClass().toString() + ".");

			DaxWalker.walkTo(area.getRandomTile());
			General.sleep(General.randomSD(2400, 300));

			if (area.contains(Player.getPosition())) {
				bool = true;
				General.sleep(General.random(600, 900));
				return true;
			}
		}

		return false;
	}

	public boolean waitForConvo(String x) {

		if (!Timing.waitCondition(new Condition() {

			@Override
			public boolean active() {

				return (NPCChat.getName() != null);

			}

		}, General.random(15000, 20000)))
			;

		if (!Timing.waitCondition(new Condition() {

			@Override
			public boolean active() {

				return (NPCChat.getName().contains(x));

			}

		}, General.random(15000, 20000)))
			;
		if (!NPCChat.getName().contains(x)) {
			return false;
		}
		return true;
	}
}