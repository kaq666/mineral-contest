package eu.billyinc.mineralcontest.manager;

import org.bukkit.Location;

public class MineralContestGameManager {
	private Location spawn;
	private Location arenaLocation;
	private int mapSize;
	
	public MineralContestGameManager() {
	}
	
	public Location getSpawn() {
		return spawn;
	}
	public void setSpawn(Location spawn) {
		this.spawn = spawn;
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