package eu.billyinc.mineralcontest.manager;

import java.util.LinkedHashMap;

import org.bukkit.block.Chest;

import eu.billyinc.mineralcontest.model.MineralContestChest;

public class MineralContestChestManager {
	
	private LinkedHashMap<Chest, MineralContestChest> chestHashedMap = new LinkedHashMap<Chest, MineralContestChest>();

	private LinkedHashMap<Chest, MineralContestChest> getChestHashedMap() {
		return chestHashedMap;
	}
	
	public MineralContestChest getMineralContestChestByChest(Chest chest) {
		return this.getChestHashedMap().get(chest);
	}
	
	public void addMineralContestChest(Chest chest, MineralContestChest mcChest) {
		this.getChestHashedMap().put(chest, mcChest);
	}
	
	public void removeMineralContestChest(Chest chest) {
		this.getChestHashedMap().remove(chest);
	}
	
	public MineralContestChest getMineralContestArenaChest() {
		for (Chest chest : this.getChestHashedMap().keySet()) {
			MineralContestChest mineralContestChest = this.getMineralContestChestByChest(chest);
			if (mineralContestChest instanceof MineralContestChest && mineralContestChest.isArenaChest()) {
				return mineralContestChest;
			}
		}
		
		return null;
	}
	
}
