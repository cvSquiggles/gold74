package scripts.StarterScripts;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSItem;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;

@SuppressWarnings("deprecation")
@ScriptManifest(authors = {
		"Slippi, Sneakles" }, category = "template", name = "TriBot_Start", version = 1, description = "Initial template.")
public class TriBot_Start extends Script {

	State state;

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
		// TODO Auto-generated method stub
		println("Hello world.");

		DaxWalker.setCredentials(new DaxCredentialsProvider() {
			@Override
			public DaxCredentials getDaxCredentials() {
				return new DaxCredentials("sub_IOoPK5QRFwMbww", "9b8ce8a6-9604-481e-8dff-d665db473d8f");
			}
		});
	}

	public int onLoop() {
		// TODO Auto-generated method stub
		switch (getState()) {

		}
		return 5;
	}

	public void onStop() {
		// TODO Auto-generated method stub
		println("Goodbye world.");
	}

	// State names
	private enum State {

	}

	// Checks if a certain condition is met, then return that state.
	private State getState() {

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

	@SuppressWarnings("unused")
	private boolean withdrawItems(int[] x, int y) {

		int i = 0;

		while (i <= x.length - 1) {

			Banking.withdraw(y, x[i]);
			General.sleep(General.randomSD(600, 300));

			i++;
		}

		return (false);

	}

	@SuppressWarnings("unused")
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

}