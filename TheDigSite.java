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
	boolean hasWalkedToPanningPoint;
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
	RSArea museum = new RSArea(new RSTile(3258, 3450, 0), new RSTile(3254, 3447, 0));
	RSArea bush = new RSArea(new RSTile(3358, 3372, 0), new RSTile(3359, 3371, 0));
	RSArea tent = new RSArea(new RSTile(3370, 3380, 0), new RSTile(3372, 3378, 0));
	RSArea panningPoint = new RSArea(new RSTile(3377, 3380, 0), new RSTile(3379, 3378, 0));

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

//				delete
				convoWait("c", 2);
				convoWait("Yes", 0);
				convoWait("c", 3);
				convoWait("The", 0);
				convoWait("c", 3);
				convoWait("Magic", 0);
				convoWait("c", 3);
				convoWait("Rubber", 0);
				convoWait("c", 6);
//				delete

//				keep
//			convoWait("c", 5);
//			convoWait("c", 5);
//			convoWait("The", 0);
//			convoWait("c", 3);
//			convoWait("Magic", 0);
//			convoWait("c", 3);
//			convoWait("Rubber", 0);
//			convoWait("c", 3);
//				keep
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
		FINDTEDDYBEAR, FINDPANNINGTRAY, TALKTOPANNINGGUIDE, PANNING

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