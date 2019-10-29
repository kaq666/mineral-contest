package eu.billyinc.mineralcontest;

import eu.billyinc.mineralcontest.listener.SpawnListener;
import eu.billyinc.mineralcontest.model.Team;
import eu.billyinc.mineralcontest.utils.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import eu.billyinc.mineralcontest.command.MineralContestCommand;
import eu.billyinc.mineralcontest.listener.MineralContestListener;
import eu.billyinc.mineralcontest.manager.MineralContestManager;

/**
 * Hello world!
 *
 */
public class App extends JavaPlugin {

    private List<Team> teams = new ArrayList<Team>();
    private final Map<UUID, FastBoard> boards = new HashMap<>();
    private GameState gameState = GameState.WAITING;

	@Override
    public void onEnable() {
        super.onEnable();
        System.out.println("Mineral Contest Enabled");
        SpawnListener spawnListener = new SpawnListener(this);
        this.getServer().getPluginManager().registerEvents(spawnListener, this);
        this.registerTeams();
        MineralContestManager.setApp(this);
        getCommand("mc").setExecutor(new MineralContestCommand(this));
        getServer().getPluginManager().registerEvents(new MineralContestListener(), this);
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

    public boolean allTeamAsAPlayer() {
	    //TODO : remove always true condition
        return true;
//	    for (Team team : this.teams) {
//	        if (team.getPlayers().isEmpty()) {
//	            return false;
//            }
//        }
//	    return true;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Map<UUID, FastBoard> getBoards() {
        return boards;
    }

    public void finishGame() {
        this.gameState = GameState.FINISH;
        for (Team team : this.teams) {
            team.getPlayers().clear();
        }
        this.boards.clear();
    }
}
