package scripts.StarterScripts;

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

import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;

import org.tribot.api.util.abc.*;

@SuppressWarnings({ "unused", "deprecation" })
@ScriptManifest(authors = {
		"Slippi, Sneakles" }, category = "Mud Runes", name = "DruidicRitual", version = 0.1, description = "Does the Druidic Ritual quest.")
public class DruidicRitual extends Script {

	State state;

	int j = 1;

	public ABCUtil abc;

	boolean banking1;
	boolean banking2;
	boolean hasTeleported;
	boolean hasWalkedToKaqemeex;
	boolean hasTalkedToKaqemeex;
	boolean hasWalkedToSanfew;
	boolean hasTalkedToSanfew;
	boolean hasWalkedToCauldronLadder;
	boolean hasWalkedToCauldron;
	boolean hasInteractedWithCauldron;
	boolean hasReturnedToSanfew;
	boolean hasReturnedToKaqemeex;

	RSArea druidsCircle = new RSArea(new RSTile(2922, 3486, 0), new RSTile(2929, 3481, 0));
	RSArea sanfewsRoom = new RSArea(new RSTile(2897, 3431, 0), new RSTile(2899, 3425, 0));
	RSArea sanfewsRoom2 = new RSArea(new RSTile(2896, 3429, 1), new RSTile(2900, 3425, 1));
	RSArea cauldronLadder = new RSArea(new RSTile(2882, 3399, 0), new RSTile(2886, 3395, 0));
	RSArea cauldronEntrance = new RSArea(new RSTile(2887, 9831, 0), new RSTile(2888, 9830, 0));

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

	public void onStart() {

		println("Starting script.");

		DaxWalker.setCredentials(new DaxCredentialsProvider() {
			@Override
			public DaxCredentials getDaxCredentials() {
				return new DaxCredentials("sub_IOoPK5QRFwMbww", "9b8ce8a6-9604-481e-8dff-d665db473d8f");
			}
		});

	}

	public int onLoop() {
		switch (getState()) {

		case BANKING:

			if (!banking1) {

				println("Getting quest items from the bank.");

				if (!Banking.isBankLoaded()) {

					Banking.openBank();
					Banking.waitUntilLoaded(3000);
					General.sleep(600, 1200);

				}

				else if (Banking.isBankScreenOpen()) {

					int[] itemID = { 2138, 2136, 2134, 2132, 3853, 12625, 88, 11133 };
					int[] itemID2 = { 8007, 22795 };

					withdrawItems(itemID, 1);

					if (!Timing.waitCondition(new Condition() {

						@Override
						public boolean active() {

							return (itemCheck(itemID, 1));

						}

					}, General.random(15000, 20000)))
						;

					withdrawItems(itemID2, 5);

					if (!Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {

							General.sleep(300);
							return (itemCheck(itemID2, 5));

						}
					}, General.random(15000, 30000)))
						;

					General.sleep(General.randomSD(3000, 1200));

					if (itemCheck(itemID, 1) && itemCheck(itemID2, 5)) {

						banking1 = true;
						Banking.close();
						General.sleep(600, 1200);

					}

				}

			}

			if (banking1) {

				println("Equipping Boots of lightness and Regen bracelet.");

				if (Inventory.find(88).length > 0 && Inventory.find(11133).length > 0) {

					RSItem[] Boots = Inventory.find(88);
					RSItem[] Braclets = Inventory.find(11133);

					Boots[0].click("Wear");
					General.sleep(300, 600);
					Braclets[0].click("Wear");
					General.sleep(1800, 2400);

				}

				if (Inventory.find(88).length < 1 && Inventory.find(11133).length < 1) {

					banking2 = true;

				}

			}

			break;

		case WALKTOKAQEMEEX:

			if (!hasTeleported) {

				println("Teleporting to Burthorpe.");

				RSItem[] GamesNecks = Inventory.find(3853);
				RSItem GamesNeck = GamesNecks[0];

				GamesNeck.click("Rub");
				General.sleep(600, 1200);
				String[] Options = NPCChat.getOptions();
				NPCChat.selectOption("Burthorpe", true);
				General.sleep(3000, 6000);

				if (Inventory.find(3853).length == 0) {

					hasTeleported = true;

				}

				else
					break;
			}

			if (!druidsCircle.contains(Player.getPosition()) && hasTeleported) {

				println("Walking to Kaqemeex.");

				DaxWalker.walkTo(druidsCircle.getRandomTile());
				General.sleep(1800, 2400);

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
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.selectOption("I'm in search of a quest.", false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.selectOption("Okay, I will try and help.", false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
			}

			if (Game.getSetting(80) > 0) {

				hasTalkedToKaqemeex = true;

			}

			break;

		case WALKTOSANFEW:

			if (!sanfewsRoom.contains(Player.getPosition())) {

				println("Walking to Sanfew.");

				WebWalking.walkTo(sanfewsRoom.getRandomTile());
				General.sleep(1800, 2400);
			}

			if (sanfewsRoom.contains(Player.getPosition())) {
				RSObject[] staircases = Objects.findNearest(20, 16671);
				staircases[0].click("Climb-up");
				General.sleep(General.randomSD(2400, 300));

				if (sanfewsRoom2.contains(Player.getPosition())) {
					hasWalkedToSanfew = true;
				}
			}

			break;

		case TALKTOSANFEW:

			if (Game.getSetting(80) == 1) {

				println("Talking to Sanfew.");

				RSNPC[] Sanfews = NPCs.findNearest(5044);
				RSNPC Sanfew = Sanfews[0];
				Sanfew.adjustCameraTo();
				Sanfew.click("Talk-to");
				General.sleep(2000, 4000);
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.selectOption("I've been sent to help purify the Varrock stone circle.", false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.clickContinue(false);
				General.sleep(General.randomSD(2400, 300));
				NPCChat.selectOption("Ok, I'll do that then.", false);
				General.sleep(General.randomSD(2400, 300));

			}

			if (Game.getSetting(80) == 2) {

				hasTalkedToSanfew = true;

			}

			break;

		case WALKTOCAULDRON:

			if (!hasWalkedToCauldron) {

				println("Walking to Cauldron.");

				if (sanfewsRoom2.contains(Player.getPosition())) {

					RSObject[] staircases = Objects.findNearest(20, 16673);
					staircases[0].click("Climb-down");
					General.sleep(2400, 3000);

				}

				if (!hasWalkedToCauldronLadder) {

					WebWalking.walkTo(cauldronLadder.getRandomTile());
					General.sleep(1800, 2400);

					if (cauldronLadder.contains(Player.getPosition())) {

						General.sleep(General.random(200, 400));
						RSObject[] ladders = Objects.findNearest(20, 16680);
						ladders[0].click("Climb-down");
						General.sleep(General.random(1800, 2400));
						hasWalkedToCauldronLadder = true;

					}

				}

				if (hasWalkedToCauldronLadder && !cauldronEntrance.contains(Player.getPosition())) {

					WebWalking.walkTo(cauldronEntrance.getRandomTile());
					General.sleep(General.random(300, 600));

				}
			}

			if (cauldronEntrance.contains(Player.getPosition())) {

				RSObject[] objArmors = Objects.findNearest(10, 818);
				RSNPC[] npcArmors = NPCs.findNearest(20, 5043);

				if (objArmors.length > 0) {

					RSObject[] Doors = Objects.findNearest(20, 2143);
					Doors[0].click("Open");
					General.sleep(General.random(1200, 1800));
					Doors[0].click("Open");
					General.sleep(General.random(600, 1200));
					Doors[0].click("Open");
					General.sleep(General.random(600, 1200));

					hasWalkedToCauldron = true;

				}

				if (npcArmors.length > 1) {

					RSObject[] Doors = Objects.findNearest(20, 2143);
					Doors[0].click("Open");
					General.sleep(General.random(1200, 1800));

					hasWalkedToCauldron = true;

				}

			}

			break;

		case INTERACTWITHCAULDRON:

			if (!hasInteractedWithCauldron) {

				println("Interacting with cauldron.");

				if (Inventory.find(2138).length > 0) {

					println("Using raw chicken on caudlron.");

					RSItem[] chicken = Inventory.find(2138);
					chicken[0].click("Use");
					General.sleep(General.random(1200, 1800));
					RSObject[] cauldron = Objects.findNearest(10, 2142);
					cauldron[0].click("Use");
					General.sleep(General.random(600, 1200));

				}

				if (Inventory.find(2136).length > 0) {

					println("Using raw bear meat on caudlron.");

					RSItem[] bearMeat = Inventory.find(2136);
					bearMeat[0].click("Use");
					General.sleep(General.random(600, 1200));
					RSObject[] cauldron = Objects.findNearest(10, 2142);
					cauldron[0].click("Use");
					General.sleep(General.random(600, 1200));

				}

				if (Inventory.find(2134).length > 0) {

					println("Using raw rat meat on caudlron.");

					RSItem[] ratMeat = Inventory.find(2134);
					ratMeat[0].click("Use");
					General.sleep(General.random(600, 1200));
					RSObject[] cauldron = Objects.findNearest(10, 2142);
					cauldron[0].click("Use");
					General.sleep(General.random(600, 1200));

				}

				if (Inventory.find(2132).length > 0) {

					println("Using raw beef on caudlron.");

					RSItem[] beef = Inventory.find(2132);
					beef[0].click("Use");
					General.sleep(General.random(600, 1200));
					RSObject[] cauldron = Objects.findNearest(10, 2142);
					cauldron[0].click("Use");
					General.sleep(General.random(600, 1200));

				}

				if (Inventory.find(2132, 2134, 2136, 2138).length == 0) {
					hasInteractedWithCauldron = true;
				}
			}

			break;

		case RETURNTOSANFEW:

			if (!hasReturnedToSanfew) {

				println("Returning to Sanfew.");

				RSItem[] GamesNecks = Inventory.find(3855);
				RSItem GamesNeck = GamesNecks[0];

				GamesNeck.click("Rub");
				General.sleep(600, 1200);
				String[] Options = NPCChat.getOptions();
				NPCChat.selectOption("Burthorpe", true);
				General.sleep(3000, 6000);

				WebWalking.walkTo(sanfewsRoom.getRandomTile());
				General.sleep(1800, 2400);

				if (sanfewsRoom.contains(Player.getPosition())) {

					RSObject[] staircases = Objects.findNearest(20, 16671);
					staircases[0].click("Climb-up");

					General.sleep(General.randomSD(2400, 300));

				}

				if (sanfewsRoom2.contains(Player.getPosition())) {

					println("Talking to Sanfew.");

					RSNPC[] Sanfews = NPCs.findNearest(5044);
					RSNPC Sanfew = Sanfews[0];
					Sanfew.adjustCameraTo();
					Sanfew.click("Talk-to");
					General.sleep(2000, 4000);
					NPCChat.clickContinue(false);
					General.sleep(General.randomSD(2400, 300));
					NPCChat.clickContinue(false);
					General.sleep(General.randomSD(2400, 300));
					NPCChat.clickContinue(false);
					General.sleep(General.randomSD(2400, 300));
					NPCChat.clickContinue(false);
					General.sleep(General.randomSD(2400, 300));

					hasReturnedToSanfew = true;

					General.sleep(1200, 1800);

				}
			}

			break;

		case RETURNTOKAQEMEEX:

			if (!hasReturnedToKaqemeex) {

				println("Returning to Kaqemeex.");

				if (sanfewsRoom2.contains(Player.getPosition())) {

					RSObject[] staircases = Objects.findNearest(20, 16673);
					staircases[0].click("Climb-down");
					General.sleep(2400, 3000);

				}

				WebWalking.walkTo(druidsCircle.getRandomTile());
				General.sleep(1800, 2400);

				if (druidsCircle.contains(Player.getPosition()) && Game.getSetting(80) == 3) {

					println("Talking to Kaqemeex.");

					RSNPC[] Kaqemeexs = NPCs.findNearest(5045);
					RSNPC Kaqemeex = Kaqemeexs[0];
					Kaqemeex.adjustCameraTo();
					Kaqemeex.click("Talk-to");
					General.sleep(2000, 4000);
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
					NPCChat.clickContinue(false);
					General.sleep(General.randomSD(2400, 300));
					NPCChat.clickContinue(false);
					General.sleep(General.randomSD(2400, 300));
					NPCChat.clickContinue(false);
					General.sleep(General.randomSD(2400, 300));
					NPCChat.clickContinue(false);
					General.sleep(General.randomSD(2400, 300));

				}

				if (Game.getSetting(80) == 4)
					;

				hasReturnedToKaqemeex = true;

			}

			if (hasReturnedToKaqemeex = true) {

				return -1;

			}

			break;

		}
		return 50;
	}

	public void onStop() {
		// TODO Auto-generated method stub
		println("Stopping Script");
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
		BANKING, WALKTOKAQEMEEX, TALKTOKAQEMEEX, WALKTOSANFEW, TALKTOSANFEW, WALKTOCAULDRON, INTERACTWITHCAULDRON,
		RETURNTOSANFEW, RETURNTOKAQEMEEX

	}

	private State getState() {

		if (!banking2) {
			state = State.BANKING;
		} else if (!hasWalkedToKaqemeex && banking2) {
			state = State.WALKTOKAQEMEEX;
		} else if (!hasTalkedToKaqemeex && hasWalkedToKaqemeex) {
			state = State.TALKTOKAQEMEEX;
		} else if (!hasWalkedToSanfew && hasTalkedToKaqemeex) {
			state = State.WALKTOSANFEW;
		} else if (!hasTalkedToSanfew && hasWalkedToSanfew) {
			state = State.TALKTOSANFEW;
		} else if (!hasWalkedToCauldron && hasTalkedToSanfew) {
			state = State.WALKTOCAULDRON;
		} else if (!hasInteractedWithCauldron && hasWalkedToCauldron) {
			state = State.INTERACTWITHCAULDRON;
		} else if (!hasReturnedToSanfew && hasInteractedWithCauldron) {
			state = State.RETURNTOSANFEW;
		} else if (!hasReturnedToKaqemeex && hasReturnedToSanfew) {
			state = State.RETURNTOKAQEMEEX;
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
			General.sleep(General.randomSD(600, 300));

			i++;
		}

		return (false);

	}

}