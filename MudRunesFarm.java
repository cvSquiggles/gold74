package scripts;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;

@ScriptManifest(authors = {
		"Slippi, Sneakles" }, category = "Mud Runes", name = "Mud Runes Farm", version = 1, description = "Farms mud runes.")
public class MudRunesFarm extends Script implements Loopable {

	State state;

	boolean shouldTeleDigsite;
	boolean shouldWalkToRuin;
	boolean shouldCraftMudRunes;
	boolean shouldReturnToBank;
	boolean shouldBankMudRunes = true;

	int digsitePendantChargesPre = 0;
	int digsitePendantChargesPost = 0;

	RSArea digsiteStart = new RSArea(new RSTile(3327, 3449, 0), new RSTile(3347, 3438, 0));
	RSArea ruin = new RSArea(new RSTile(3311, 3470, 0), new RSTile(3315, 3468, 0));
	RSArea altarRoom = new RSArea(new RSTile(2655, 4832, 0), new RSTile(2662, 4827, 0));
	RSArea castleBankRoom = new RSArea(new RSTile(2437, 3093, 0), new RSTile(2443, 3082, 0));

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

		DaxWalker.setCredentials(new DaxCredentialsProvider() {
			@Override
			public DaxCredentials getDaxCredentials() {
				return new DaxCredentials("sub_IOoPK5QRFwMbww", "9b8ce8a6-9604-481e-8dff-d665db473d8f");
			}
		});
	}

	@SuppressWarnings("deprecation")
	@Override
	public int onLoop() {
		// TODO Auto-generated method stub
		switch (getState()) {

		case TELEPORTTODIGSITE:
			println("Teleporting to the digsite.");

			if (Inventory.find(11194).length > 0) {
				RSItem[] digsitePendants = Inventory.find(11194);
				digsitePendants[0].click("Rub");
				digsitePendantChargesPre = 5;
				digsitePendantChargesPost = 4;
			} else if (Inventory.find(11193).length > 0) {
				RSItem[] digsitePendants = Inventory.find(11193);
				digsitePendants[0].click("Rub");
				digsitePendantChargesPre = 4;
				digsitePendantChargesPost = 3;
			} else if (Inventory.find(11192).length > 0) {
				RSItem[] digsitePendants = Inventory.find(11192);
				digsitePendants[0].click("Rub");
				digsitePendantChargesPre = 3;
				digsitePendantChargesPost = 2;
			} else if (Inventory.find(11191).length > 0) {
				RSItem[] digsitePendants = Inventory.find(11191);
				digsitePendants[0].click("Rub");
				digsitePendantChargesPre = 2;
				digsitePendantChargesPost = 1;
			} else if (Inventory.find(11190).length > 0) {
				shouldTeleDigsite = false;
				shouldBankMudRunes = true;
				break;
			}

			if (!Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {

					return (Player.getAnimation() == 714);

				}

			}, General.random(15000, 20000)))
				;

			if (!Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {

					return (Player.getAnimation() == -1);

				}

			}, General.random(15000, 20000)))
				;

			if ((digsitePendantChargesPre - 1) == digsitePendantChargesPost) {
				shouldTeleDigsite = false;
				shouldWalkToRuin = true;
			}

			break;

		case WALKTORUIN:
			println("Walking to ruin.");

			DaxWalker.walkTo(ruin.getRandomTile());

			areaWait(ruin);

			if (ruin.contains(Player.getPosition())) {

				RSObject[] ruins = Objects.findNearest(10, 34816);
				ruins[0].click("Enter");
				General.sleep(General.randomSD(900, 300));

				areaWait(altarRoom);
				if (altarRoom.contains(Player.getPosition())) {
					shouldWalkToRuin = false;
					shouldCraftMudRunes = true;
				} else
					break;
			}

			break;

		case CRAFTMUDRUNES:

			println("Crafting mud runes.");

			RSItem[] waterTalismen = Inventory.find(1444);
			if (waterTalismen.length == 0) {
				shouldCraftMudRunes = false;
				break;
			}

			waterTalismen[0].click("Use");
			General.sleep(General.randomSD(1200, 300));

			RSObject[] altars = Objects.findNearest(20, 34763);
			if (General.random(1, 100) < 17) {
				altars[0].adjustCameraTo();
			}
			altars[0].click("Use");

			if (!Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {

					return (Player.getAnimation() == 791);

				}

			}, General.random(15000, 20000)))
				;

			if (!Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {

					return (Player.getAnimation() == -1);

				}

			}, General.random(15000, 20000)))
				;

			General.sleep(General.randomSD(2400, 600));

			if (Inventory.find(4698).length > 0) {
				shouldCraftMudRunes = false;
				shouldReturnToBank = true;
			}
			
			break;

		case RETURNTOBANK:

			println("Returning to the bank.");

			RSItem[] ringOfDuelings = Equipment.find(SLOTS.RING);

			ringOfDuelings[0].click("Castle");

			if (!Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {

					return (Player.getAnimation() == 714);

				}

			}, General.random(15000, 20000)))
				;

			if (!Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {

					return (Player.getAnimation() == -1);

				}

			}, General.random(15000, 20000)))
				;

			General.sleep(General.randomSD(1200, 300));

			if (castleBankRoom.contains(Player.getPosition())) {

				shouldReturnToBank = false;
				shouldBankMudRunes = true;

			}

			break;

		case BANKMUDRUNES:

			println("Banking mud runes and restocking.");

			Banking.openBank();

			if (!Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {

					return (Banking.isBankLoaded());

				}

			}, General.random(15000, 20000)))
				;

			if (!Banking.isBankLoaded()) {
				break;
			}

			Banking.depositAll();

			if (!equipmentBankCheck(SLOTS.RING, 2552)) {
				break;
			}

			if (!equipmentBankCheck(SLOTS.AMULET, 5521)) {
				break;
			}

			if (!itemBankCheck(1444)) {
				break;
			}

			if (Inventory.findList(11191, 11192, 11193, 11194).size() == 0) {
				itemBankCheck(11194);
			}

			if (!itemBankCheckAll(7936)) {
				break;
			}

			Banking.close();

			if (!Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {

					return (!Banking.isBankLoaded());

				}

			}, General.random(15000, 20000)))
				;

			if (Banking.isBankLoaded()) {
				break;
			} else {
				shouldBankMudRunes = false;
				shouldTeleDigsite = true;
			}

			General.sleep(1500);

			break;

		case WAITING:
			println("Waiting.");

			General.sleep(15000);

			break;

		}
		return 5;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		println("Goodbye world.");
	}

	// State names
	private enum State {
		TELEPORTTODIGSITE, WAITING, WALKTORUIN, CRAFTMUDRUNES, RETURNTOBANK, BANKMUDRUNES
	}

	// Checks if a certain condition is met, then return that state.
	private State getState() {
		if (shouldTeleDigsite) {
			state = State.TELEPORTTODIGSITE;
		} else if (shouldWalkToRuin) {
			state = State.WALKTORUIN;
		} else if (shouldCraftMudRunes) {
			state = State.CRAFTMUDRUNES;
		} else if (shouldReturnToBank) {
			state = State.RETURNTOBANK;
		} else if (shouldBankMudRunes) {
			state = State.BANKMUDRUNES;
		} else {
			state = State.WAITING;
		}

		return state;
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
//		 Set string x = to "c" if you would like to simply click continue, and int y
//		 is the # of times to do so.
//		 Set string x = to whatever text is contained in the NPCChat.getOptions()
//		 option that you'd like to click, be sure to still enter a y value, good default is 0.
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

	public boolean itemBankCheck(int itemID) {
		if (Inventory.find(itemID).length == 0) {
			Banking.withdraw(1, itemID);

			if (!Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {

					return (Inventory.find(itemID).length > 0);

				}

			}, General.random(15000, 20000)))
				;

			if (Inventory.find(itemID).length == 0) {
				return false;
			} else
				return true;
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	public boolean itemBankCheckAll(int itemID) {
		if (Inventory.find(itemID).length == 0) {
			Banking.withdraw(0, itemID);

			if (!Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {

					return (Inventory.find(itemID).length > 0);

				}

			}, General.random(15000, 20000)))
				;

			if (Inventory.find(itemID).length == 0) {
				return false;
			} else
				return true;
		}
		return true;
	}

	public boolean equipmentBankCheck(SLOTS slot, int itemID) {
		if (Equipment.getItem(slot) == null) {
			Banking.withdraw(1, itemID);

			if (!Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {

					return (Inventory.find(itemID).length > 0);

				}

			}, General.random(15000, 20000)))
				;

			RSItem[] items = Inventory.find(itemID);
			items[0].click("Wear");

			if (!Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {

					return (Equipment.getItem(slot) != null);

				}

			}, General.random(15000, 20000)))
				;

			if (Equipment.getItem(slot) == null) {
				return false;
			} else
				return true;
		}
		return true;
	}

	public boolean areaWait(RSArea area) {
		if (!Timing.waitCondition(new Condition() {

			@Override
			public boolean active() {

				return (area.contains(Player.getPosition()));

			}

		}, General.random(25000, 30000)))
			;
		return true;
	}
}