package scripts.MudRunesOOP.tasks;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.MudRunesOOP.data.Constants;
import scripts.MudRunesOOP.framework.AbstractTask;


public class WalkToAltar implements AbstractTask {

	@Override
	public String info() {
		return "Walking to woodcutting area.";
	}

	@Override
	public boolean shouldExecute() {
		return !Inventory.isFull() && !Constants.altarArea.contains(Player.getPosition()); 
	}

	@Override
	public void execute() {
		DaxWalker.walkTo(Constants.altarArea.getRandomTile());
	}

}