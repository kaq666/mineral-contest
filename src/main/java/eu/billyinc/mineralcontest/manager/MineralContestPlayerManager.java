package eu.billyinc.mineralcontest.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import eu.billyinc.mineralcontest.model.MineralContestPlayer;

public class MineralContestPlayerManager {
	private List<MineralContestPlayer> mineralContestPlayerList = new ArrayList<MineralContestPlayer>();

	public List<MineralContestPlayer> getPlayers() {
		return this.mineralContestPlayerList;
	}

	public void setPlayers(List<MineralContestPlayer> players) {
		this.mineralContestPlayerList = players;
	}
	
	public void addMineralContestPlayer(MineralContestPlayer mineralContestPlayer) {
		this.mineralContestPlayerList.add(mineralContestPlayer);
	}
	
	public void removeMineralContestPlayer(MineralContestPlayer mineralContestPlayer) {
		this.mineralContestPlayerList.remove(mineralContestPlayer);
	}
	
	public MineralContestPlayer getMineralContestPlayerByUUID(UUID ID) {
		for (MineralContestPlayer mineralContestPlayer : this.mineralContestPlayerList) {
			if(mineralContestPlayer.getID().equals(ID)) {
				return mineralContestPlayer;
			}
		}
		
		return null;
	}
}
