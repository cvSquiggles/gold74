package scripts.MudRunesOOP.data;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public interface Constants {
	
//	Runes
	int WATER_RUNE = 555;
	int MUD_RUNE = 4698;
	
//	Talismans
	int WATER_TALISMAN = 1444;
	
//	Altars
	int ALTAR_ENTRANCE = 34816;
	int ALTAR = 34763;
	
//	Areas
	RSArea altarEntrance = new RSArea(new RSTile(3305, 3472, 0), new RSTile(3301, 3469, 0));
	RSArea altarArea = new RSArea(new RSTile(2656, 4839, 0), new RSTile(2660, 4843, 0));
}
