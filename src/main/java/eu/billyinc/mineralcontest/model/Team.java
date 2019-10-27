package eu.billyinc.mineralcontest.model;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private String name;
    private List<Player> players;
    private int score;
    private ChatColor color;
    private Location spawn;

    public Team(String name, ChatColor chatColor, Location location) {
        this.name = name;
        this.players = new ArrayList<Player>();
        this.color = chatColor;
        this.spawn = location;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public List<Player> getPlayers() {
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
