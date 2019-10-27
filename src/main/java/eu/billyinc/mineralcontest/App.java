package eu.billyinc.mineralcontest;

import eu.billyinc.mineralcontest.command.TeamCommandExecutor;
import eu.billyinc.mineralcontest.listener.SpawnListener;
import eu.billyinc.mineralcontest.model.PlayerTeam;
import eu.billyinc.mineralcontest.model.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App extends JavaPlugin {

    private List<Team> teams = new ArrayList<Team>();

	@Override
    public void onEnable() {
        super.onEnable();
        System.out.println("Mineral Contest Enabled");
        SpawnListener spawnListener = new SpawnListener(this);
        this.getServer().getPluginManager().registerEvents(spawnListener, this);
        this.registerTeams();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        System.out.println("Mineral Contest Disabled");
    }

    private void registerTeams() {
	    this.teams.add(new Team("Team Bleue", ChatColor.BLUE,  new Location(Bukkit.getServer().getWorld("World"),-292.544, 65, -342.622, 180f, 1.9f)));
	    this.teams.add(new Team("Team Jaune", ChatColor.YELLOW, new Location(Bukkit.getServer().getWorld("World"),-267, 65, -341.628, 90f, 1.5f)));
	    this.teams.add(new Team("Team Rouge", ChatColor.RED, new Location(Bukkit.getServer().getWorld("World"),-279.110, 65, -353.155, -90f, 0f)));
    }

    public Team getTeamByName(String name) {
	    for (Team team : this.teams) {
	        if (team.getName().equals(name)) {
	            return team;
            }
        }
	    return null;
    }
}
