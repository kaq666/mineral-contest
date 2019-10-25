package eu.billyinc.mineralcontest;

import eu.billyinc.mineralcontest.listener.SpawnListener;
import eu.billyinc.mineralcontest.model.Team;
import org.bukkit.ChatColor;
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
        this.getServer().getPluginManager().registerEvents(new SpawnListener(this), this);
        this.registerTeams();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        System.out.println("Mineral Contest Disabled");
    }

    private void registerTeams() {
	    this.teams.add(new Team("Team Bleue", ChatColor.BLUE));
	    this.teams.add(new Team("Team Jaune", ChatColor.YELLOW));
	    this.teams.add(new Team("Team Rouge", ChatColor.RED));
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
