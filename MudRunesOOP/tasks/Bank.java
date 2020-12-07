package scripts.MudRunesOOP.tasks;

import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import scripts.MudRunesOOP.framework.AbstractTask;

public class Bank implements AbstractTask {

	@Override
	public String info() {
		return "Banking";
	}

	@Override
	public boolean shouldExecute() {
		return Inventory.isFull() && Banking.isInBank();
	}

	@Override
	public void execute() {
		if (!Banking.isBankScreenOpen())
			Banking.openBank();
		else
			Banking.depositAll();
		
	}
}
