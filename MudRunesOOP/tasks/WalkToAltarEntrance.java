package scripts.MudRunesOOP.tasks;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;

import scripts.MudRunesOOP.data.Constants;
import scripts.MudRunesOOP.framework.AbstractTask;
import scripts.dax_api.api_lib.DaxWalker;

public class WalkToAltarEntrance implements AbstractTask {

	@Override
	public String info() {
		return "Walking to altar area.";
	}

	@Override
	public boolean shouldExecute() {
		return Inventory.isFull() && !Constants.altarEntrance.contains(Player.getPosition());
	}

	@Override
	public void execute() {
		DaxWalker.walkTo(Constants.altarEntrance.getRandomTile());
	}

}
