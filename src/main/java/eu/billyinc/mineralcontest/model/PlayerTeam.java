package eu.billyinc.mineralcontest.model;

import org.bukkit.ChatColor;
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
        // TODO : phrase d'accroche random en description
        player.sendTitle(team.getColor() + this.team.getName(), ChatColor.WHITE + "Prepare for battle", 10, 70, 20);
    }

    public Team getTeam() {
        return team;
    }

    public Player getPlayer() {
        return player;
    }

}
