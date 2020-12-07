package scripts.MudRunesOOP.tasks;

import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.types.RSItem;

import scripts.dax_api.api_lib.DaxWalker;

import scripts.MudRunesOOP.data.Vars;
import scripts.MudRunesOOP.framework.AbstractTask;


public class WalkToBank implements AbstractTask {

	@Override
	public String info() {
		return "Walking to bank";
	}

	@Override
	public boolean shouldExecute() {
		return Vars.get().bankRunes && !Inventory.isFull() && !Banking.isInBank();
	}

	@Override
	public void execute() {
		
		RSItem[] ringOfDuelings = Equipment.find(SLOTS.RING);

		ringOfDuelings[0].click("Castle Wars");
		Timing.waitUptext("Your ring of dueling has", 6000);
		
		DaxWalker.walkToBank();
	}

}