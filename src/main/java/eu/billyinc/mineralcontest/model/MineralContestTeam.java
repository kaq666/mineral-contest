package eu.billyinc.mineralcontest.model;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import eu.billyinc.mineralcontest.manager.MineralContestManager;

import java.util.ArrayList;
import java.util.List;

public class MineralContestTeam {

    private String name;
    private int score;
    private ChatColor color;
    private Location spawn;

    public MineralContestTeam(String name, ChatColor chatColor, Location location) {
        this.name = name;
        this.color = chatColor;
        this.spawn = location;
        this.score = 0;
    }

    public List<Player> getPlayers() {
    	List<Player> players = new ArrayList<Player>();
    	for(MineralContestPlayer mineralContestPlayer : MineralContestManager.getMineralContestPlayerManager().getPlayers()) {
    		if (this.name.equalsIgnoreCase(mineralContestPlayer.getTeamName())) {
    			players.add(mineralContestPlayer.getPlayer());
    		}
    	}
    	
        return players;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getColoredName() {
        return this.color + this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChatColor getColor() {
        return color;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }
}
