package eu.billyinc.mineralcontest.model;

import org.bukkit.entity.Player;

public class PlayerTeam {

    private Player player;

    private Team team;

    public PlayerTeam(Player player) {
        this.player = player;
    }

    public void setTeam(Team team) {
        this.team = team;
        team.addPlayer(this.player);
    }

    public Team getTeam() {
        return team;
    }

    public Player getPlayer() {
        return player;
    }

}
