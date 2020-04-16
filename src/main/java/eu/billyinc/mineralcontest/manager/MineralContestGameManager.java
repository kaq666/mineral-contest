package eu.billyinc.mineralcontest.manager;

import org.bukkit.Location;

public class MineralContestGameManager {
	private Location arenaLocation;
	private Location arenaChestLocation;
	private int mapSize;
	
	public MineralContestGameManager() {
	}

	public Location getArenaChestLocation() {
		return arenaChestLocation;
	}

	public void setArenaChestLocation(Location arenaChestLocation) {
		this.arenaChestLocation = arenaChestLocation;
	}

	public Location getArenaLocation() {
		return arenaLocation;
	}
	public void setArenaLocation(Location arenaLocation) {
		this.arenaLocation = arenaLocation;
	}
	public int getMapSize() {
		return mapSize;
	}
	public void setMapSize(int mapSize) {
		this.mapSize = mapSize;
	}
	
	
}
