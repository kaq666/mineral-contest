package eu.billyinc.mineralcontest.model;

import org.bukkit.entity.Player;

import java.util.List;

public class Team {

    private String name;
    private List<Player> players;
    private int score;

    public Team(String name) {
        this.name = name;
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
}
